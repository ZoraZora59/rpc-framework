package com.zora.rpc.client.server;

import com.zora.rpc.common.exception.RpcException;
import com.zora.rpc.common.thread.DefaultThreadFactory;
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
 * <h4>com.zora.rpc.client.server</h4>
 * <p></p>
 *
 * @author zora
 * @since 2021.02.05
 */
@Slf4j
public class ServerSocketRunner implements Runnable {
    Semaphore semaphore;
    int port;
    ExecutorService socketExecutor;

    ServerSocketRunner(Semaphore semaphore, int port) {
        this(semaphore,port,2,8,4);
    }

    ServerSocketRunner(Semaphore semaphore, int port,int core,int max,int queueLength) {
        this.semaphore = semaphore;
        this.port = port;
        socketExecutor= new ThreadPoolExecutor(core, max,
                60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueLength),
                new DefaultThreadFactory.Builder().namePrefix("socket-pool").build());
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            AtomicInteger counter = new AtomicInteger(0);
            while (true) {
                Socket socket = serverSocket.accept();
                socketExecutor.execute(() -> {
                    String uuid = UUID.randomUUID().toString();
                    try {
                        log.info("socket建立连接，id[{}]，连接信息「ip = {} , port = {} , hashcode = {}」", uuid, socket.getInetAddress().toString(), socket.getPort(), socket.hashCode());
                        while (true) {
                            log.info("socket收到数据[{}]", RpcSerializeUtil.deserializeRequest(socket.getInputStream()).toString());
                        }
                    } catch (IOException | RpcException ex) {
                        if (ex instanceof RpcException && ((RpcException) ex).getRpcCode() == -1) {
                            log.warn("socket连接被远端关闭");
                        } else {
                            log.error("socket连接意外中断", ex);
                        }
                    } finally {
                        try {
                            socket.close();
                            log.info("连接[{}]释放", uuid);
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
