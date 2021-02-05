package com.zora.rpc.client.client;

import com.zora.rpc.common.model.RpcRequest;
import com.zora.rpc.common.util.ByteConvertUtil;
import com.zora.rpc.serialize.util.RpcSerializeUtil;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.UUID;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.client.client</h4>
 * <p></p>
 *
 * @author zora
 * @since 2021.02.04
 */
@Slf4j
public class ClientSocketManager {
    private final Socket socket ;
    private final OutputStream outputStream;
    public ClientSocketManager(String host,int port) throws IOException {

        socket = new Socket(host,port);
        log.info("socket建立连接,ip = {} , port = {}", socket.getInetAddress().toString(), socket.getPort());
       outputStream  = socket.getOutputStream();

    }

    public void call(String body) throws IOException {
        byte[] bytes = RpcSerializeUtil.serialize(RpcRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .requestTarget("targetIsMe")
                .requestBody(Collections.singleton(body).toArray())
                .build());
        outputStream.write(ByteConvertUtil.intToBytes(bytes.length));
        outputStream.write(bytes);
        outputStream.flush();
    }

    public synchronized void close() throws IOException {
        socket.close();
        log.info("socket链接关闭");
    }
}
