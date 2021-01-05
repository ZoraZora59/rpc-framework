package com.zora.rpc.demo.api;

import java.util.List;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.demo.api</h4>
 * <p>调用接口</p>
 *
 * @author zora
 * @since 2021.01.05
 */
public interface RpcDemoApi {
    /**
     * 获取数字列表
     *
     * @param startAt 开始数字
     * @param count   列表长度
     * @return 连续的数字列表
     */
    List<Integer> getIntegerList(Integer startAt, Integer count);
}
