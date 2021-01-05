package com.zora.rpc.demo.consumer;

import com.zora.rpc.demo.api.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

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
        socket.close();

    }
}
