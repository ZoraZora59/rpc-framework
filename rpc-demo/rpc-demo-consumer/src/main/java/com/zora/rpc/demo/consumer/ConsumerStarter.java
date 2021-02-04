package com.zora.rpc.demo.consumer;

import com.zora.rpc.client.client.ClientSocketManager;
import com.zora.rpc.common.model.RpcRequest;
import com.zora.rpc.demo.api.Constants;
import com.zora.rpc.serialize.util.RpcSerializeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.UUID;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.demo.consumer</h4>
 * <p>消费者启动器</p>
 *
 * @author zora
 * @since 2021.01.05
 */
@Slf4j
public class ConsumerStarter {
    public static void main(String[] args) throws IOException {
        ClientSocketManager clientSocketManager=null;
        try {
            clientSocketManager = new ClientSocketManager("127.0.0.1",Constants.PORT);
            clientSocketManager.call("iambody");
            log.info("msg sended");
            clientSocketManager.call("iambody");
            log.info("msg sended");
//            clientSocketManager.call("iambody");
//            log.info("msg sended");
//            clientSocketManager.call("iambody");
//            log.info("msg sended");
        }finally {
            if (clientSocketManager!=null){
                clientSocketManager.close();
            }
        }
    }
}
