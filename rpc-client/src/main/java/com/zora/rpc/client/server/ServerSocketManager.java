package com.zora.rpc.client.server;

import com.zora.rpc.common.thread.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.client.server</h4>
 * <p>服务端管理</p>
 *
 * @author zora
 * @since 2021.02.05
 */
@Slf4j
public class ServerSocketManager {
    private ExecutorService SERVER_SOCKET_EXECUTOR = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1),
            new DefaultThreadFactory.Builder().namePrefix("SERVER_SOCKET").single(true).build());
    private final Semaphore semaphore = new Semaphore(0);

    public ServerSocketManager(int port) {
        SERVER_SOCKET_EXECUTOR.execute(new ServerSocketRunner(semaphore, port));
    }

    public ServerSocketManager(int port, int core, int max, int queueLength) {
        SERVER_SOCKET_EXECUTOR.execute(new ServerSocketRunner(semaphore, port, core, max, queueLength));
    }

    public synchronized void close() {
        if (Objects.nonNull(SERVER_SOCKET_EXECUTOR) && !SERVER_SOCKET_EXECUTOR.isTerminated()) {
            SERVER_SOCKET_EXECUTOR.shutdownNow();
            try {
                semaphore.acquire();
            }catch (InterruptedException timeout){
                log.error("服务管理器关闭超时");
            }
        }
    }

}

