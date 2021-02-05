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
import java.util.concurrent.*;

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
    public static void main(String[] args) throws IOException, InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(32,32,0, TimeUnit.SECONDS,new LinkedBlockingDeque<>(1024));
        final int count  =255;
        CountDownLatch cdt = new CountDownLatch(count);
        for (int i =0;i<count;i++){
            final int idx = i;
            executorService.execute(()->{
                try {
                    clientTest();
                    System.out.println("任务"+idx+"完成");
                    cdt.countDown();
                }catch (Exception ignore){}
            });
        }
        cdt.await(32,TimeUnit.SECONDS);
        executorService.shutdownNow();
    }
    static void clientTest() throws IOException {
        ClientSocketManager clientSocketManager=null;
        try {
            clientSocketManager = new ClientSocketManager("127.0.0.1",Constants.PORT);
            clientSocketManager.call("com.zora.rpc.demo.api.RpcDemoApi//com.zora.rpc.demo.api.RpcDemoApigetIntegerList",new Object[]{5,3});
//            log.info("msg sended");
            clientSocketManager.call("co//dsaf",new Object[]{1,2});
//            log.info("msg sended");
        }finally {
            if (clientSocketManager!=null){
                clientSocketManager.close();
            }
        }
    }
}
