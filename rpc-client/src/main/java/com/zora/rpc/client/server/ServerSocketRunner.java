package com.zora.rpc.client.server;

import com.zora.rpc.client.handler.IRpcHandler;
import com.zora.rpc.common.exception.RpcException;
import com.zora.rpc.common.model.RpcRequest;
import com.zora.rpc.common.model.RpcResponse;
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
    ExecutorService workerExecutor;
    IRpcHandler handler;

    ServerSocketRunner(IRpcHandler rpcHandler, Semaphore semaphore, int port) {
        this(rpcHandler, semaphore, port, Runtime.getRuntime().availableProcessors() + 1, Runtime.getRuntime().availableProcessors() + 1, 1);
    }

    ServerSocketRunner(IRpcHandler rpcHandler, Semaphore semaphore, int port, int core, int max, int queueLength) {
        this.semaphore = semaphore;
        this.handler = rpcHandler;
        this.port = port;
        socketExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors(),
                60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(Runtime.getRuntime().availableProcessors() + 1),
                new DefaultThreadFactory.Builder().namePrefix("socket-pool").build());
        workerExecutor = new ThreadPoolExecutor(core, max,
                60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueLength),
                new DefaultThreadFactory.Builder().namePrefix("worker-pool").build());
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            AtomicInteger counter = new AtomicInteger(0);
            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    socketExecutor.execute(() -> {
                        try {
                            workerExecutor.execute(() -> {
                                String uuid = UUID.randomUUID().toString();
                                try {
                                    log.debug("socket建立连接，id[{}]，连接信息「ip = {} , port = {} , hashcode = {}」", uuid, socket.getInetAddress().toString(), socket.getPort(), socket.hashCode());
                                    while (true) {
                                        RpcRequest request = RpcSerializeUtil.deserializeRequest(socket.getInputStream());
                                        log.debug("socket收到数据[{}]", request);
                                        RpcResponse<?> response = handler.handle(request);
                                        if (response.getCode() == 200) {
                                            log.debug("rpc调用完成，响应[{}]", response);
                                        } else {
                                            log.warn("rpc调用返回特殊状态码，响应[{}]", response);
                                        }
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
                                        log.debug("连接[{}]释放", uuid);
                                    } catch (IOException ignore) {

                                    }
                                }
                            });
                        } catch (RejectedExecutionException rejectedExecutionException) {
                            log.error("任务执行队列已满");
                        }
                    });
                } catch (RejectedExecutionException rejectedExecutionEx) {
                    log.error("任务在分发给Socket时被拒绝");
                }
                log.debug("轮询-{}-完成", counter.incrementAndGet());
            }
        } catch (IOException e) {
            log.error("服务器线程遇到问题", e);
        } finally {
            semaphore.release();
        }
    }
}
