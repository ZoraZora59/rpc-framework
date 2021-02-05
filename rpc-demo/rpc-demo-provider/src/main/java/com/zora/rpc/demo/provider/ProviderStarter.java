package com.zora.rpc.demo.provider;

import com.zora.rpc.client.server.ServerSocketManager;
import com.zora.rpc.demo.api.Constants;
import lombok.extern.slf4j.Slf4j;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.demo.provider</h4>
 * <p>生产者启动器</p>
 *
 * @author zora
 * @since 2021.01.05
 */
@Slf4j
public class ProviderStarter {
    public static void main(String[] args) {
        ServerSocketManager manager = new ServerSocketManager(Constants.PORT);
    }


}
