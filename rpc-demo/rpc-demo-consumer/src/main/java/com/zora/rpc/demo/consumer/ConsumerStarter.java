package com.zora.rpc.demo.consumer;

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

        Socket socket = new Socket("127.0.0.1", Constants.PORT);
        log.info("socket建立连接,ip = {} , port = {}", socket.getInetAddress().toString(), socket.getPort());
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(RpcSerializeUtil.serialize(RpcRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .requestTarget("targetIsMe")
                .requestBody(Collections.singleton("iAmBody").toArray())
                .build()));
        outputStream.flush();
        socket.close();

    }
}
