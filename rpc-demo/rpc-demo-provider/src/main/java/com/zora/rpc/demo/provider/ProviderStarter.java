package com.zora.rpc.demo.provider;

import com.zora.rpc.common.thread.DefaultThreadFactory;
import com.zora.rpc.demo.api.Constants;
import com.zora.rpc.serialize.util.RpcSerializeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    public static void main(String[] args) throws InterruptedException {
        ExecutorService serverSocketExecutor = new ThreadPoolExecutor(2, 2,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                new DefaultThreadFactory.Builder().namePrefix("SERVER_SOCKET").single(true).build());
        Semaphore semaphore = new Semaphore(0);

        serverSocketExecutor.execute(new ServerRunner(semaphore));
//        serverSocketExecutor.execute(new ServerRunner(semaphore));

        semaphore.acquire();

    }

    static class ServerRunner implements Runnable {
        Semaphore semaphore;
        ExecutorService socketExecutor = new ThreadPoolExecutor(2, 8,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(4),
                new DefaultThreadFactory.Builder().namePrefix("socket-pool").build());

        ServerRunner(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(Constants.PORT)) {
                AtomicInteger counter = new AtomicInteger(0);
                while (true) {
                    Socket socket = serverSocket.accept();
                    socketExecutor.execute(() -> {
                        String uuid = UUID.randomUUID().toString();
                        try {
                            log.info("socket建立连接，id[{}]，连接信息「ip = {} , port = {} , hashcode = {}」",uuid, socket.getInetAddress().toString(), socket.getPort(), socket.hashCode());
                            while (true){
                                log.info("socket收到数据[{}]", RpcSerializeUtil.deserializeRequest(socket.getInputStream()).toString());
                            }
                        } catch (IOException | InterruptedException ignore) {

                        }finally {
                            try {
                                socket.close();
                                log.info("连接[{}]释放",uuid);
                            } catch (IOException ignore) {

                            }
                        }
                    });
                    log.info("轮询-{}-完成", counter.incrementAndGet());
                }
            } catch (IOException e) {
                log.error("服务器线程遇到问题", e);
            } finally {
                semaphore.release();
            }
        }
    }
}
