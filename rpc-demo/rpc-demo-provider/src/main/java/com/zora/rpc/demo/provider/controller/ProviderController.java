package com.zora.rpc.demo.provider.controller;

import com.zora.rpc.common.model.RpcResponse;
import com.zora.rpc.demo.api.RpcDemoApi;
import com.zora.rpc.spring.annotion.RpcServiceImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.demo.provider.controller</h4>
 * <p>控制器</p>
 *
 * @author zora
 * @since 2021.01.05
 */
@Component
@RpcServiceImpl
public class ProviderController implements RpcDemoApi {
    @Override
    public RpcResponse<List<Integer>> getIntegerList(Integer startAt, Integer count) {
        List<Integer> integerList = new ArrayList<>(count);
        for (int i =startAt;i<startAt+count;i++){
            integerList.add(i);
        }
        RpcResponse<List<Integer>> response = new RpcResponse<>();
        response.setData(integerList);
        return response;
    }
}
