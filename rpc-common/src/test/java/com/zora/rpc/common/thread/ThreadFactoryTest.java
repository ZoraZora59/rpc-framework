package com.zora.rpc.common.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.common.thread</h4>
 * <p>线程工厂测试</p>
 *
 * @author zora
 * @since 2020.12.30
 */
@Slf4j
public class ThreadFactoryTest {
    @Test
    public void nameTest() {
        log.info("多线程线程池测试：");
        multiThread();
        log.info("单线程线程池测试：");
        singleThread();
    }

    private void multiThread() {
        int core = 3;
        int max = 6;
        int aliveTimeSecond = 1;
        int queueLength = 1;
        String threadNamePrefix = "hello";
        CountDownLatch countDownLatch = new CountDownLatch(max + queueLength);
        ExecutorService executorService = new ThreadPoolExecutor(core, max, aliveTimeSecond, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueLength), new DefaultThreadFactory.Builder().namePrefix(threadNamePrefix).single(false).build());
        for (int i = 0; i < max + queueLength; i++) {
            executorService.execute(() -> {
                log.info("线程{}打印", Thread.currentThread().getName());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignore) {
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException ignore) {
        }
    }

    private void singleThread() {
        int count = 1;
        String threadNamePrefix = "hello";
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService executorService = new ThreadPoolExecutor(count, count, 0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), new DefaultThreadFactory.Builder().namePrefix(threadNamePrefix).single(true).build());
        executorService.execute(() -> {
            log.info("线程{}打印", Thread.currentThread().getName());
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignore) {
            }
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException ignore) {
        }
    }
}
