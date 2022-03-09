package com.oymn.rpc04.protocol;

import com.oymn.rpc04.common.Message;
import com.oymn.rpc04.config.Config;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
//该类用来编码和解码
public class MessageCodec extends MessageToMessageCodec<ByteBuf, Message> {

    //编码
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();

        //4字节表示魔数
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        //1字节表示版本号
        buffer.writeByte(1);
        //1字节表示序列方式：0表示jdk，1表示json
        buffer.writeByte(Config.getSerializerAlgorithm().ordinal());
        //1字节表示指令类型
        buffer.writeByte(msg.getMessageType());
        //4字节表示请求序号
        buffer.writeInt(msg.getSequenceId());

        //由于除了消息内容外为15个字节，一般需要为2的倍数，因此多写入一个无意义的字节，保证规范性，对齐填充
        buffer.writeByte(0xff);

        //获取消息的内容
        byte[] bytes = Config.getSerializerAlgorithm().serialize(msg);

        //4个字节表示消息长度
        buffer.writeInt(bytes.length);

        //写入内容
        buffer.writeBytes(bytes);

        out.add(buffer);

    }

    //解码
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        //获取魔数
        int magicNum = msg.readInt();
        //获取版本号
        byte version = msg.readByte();
        //获取序列化方式
        byte serializerType = msg.readByte();
        //获取消息类型
        byte messageType = msg.readByte();
        //获取消息序号
        int sequenceId = msg.readInt();
        //无意义的填充字符
        msg.readByte();
        //消息的长度
        int messageLength = msg.readInt();
        //获取消息内容
        byte[] bytes = new byte[messageLength];
        msg.readBytes(bytes,0,messageLength);

        //反序列化得到消息对象
        Class<? extends Message> messageClass = Message.getMessageClass(messageType);
        Message message = Config.getSerializerAlgorithm().deserialize(messageClass, bytes);

        log.debug("{},{},{},{},{},{}",magicNum,version,serializerType,messageType,serializerType,messageLength);
        log.debug("{}",message);

        out.add(message);

    }
}
