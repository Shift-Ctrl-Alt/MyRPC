package com.oymn.rpc04.loadbalance;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

//轮询负载均衡
@Slf4j
public class RoundLoadBalance implements LoadBalance {

    private int choose = -1;

    @Override
    public String balance(List<String> addressList) {

        choose++;
        return addressList.get(choose % addressList.size());
    }
}
