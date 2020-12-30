package com.zora.rpc.common.util;

import com.zora.rpc.common.thread.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.common.util</h4>
 * <p>时间戳工具 </p>
 *
 * @author zora
 * @since 2020.12.30
 */
@Slf4j
public class TimeUtil {
    private TimeUtil() {
    }

    public static long currentTimestamp() {
        return Timer.getCurrentTimeStamp();
    }

    private static class Timer {
        private static final ExecutorService TIMER_SINGLE_THREAD_POOL = new ThreadPoolExecutor(1, 1,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                new DefaultThreadFactory("common-timestamp-keeper", true, Thread.NORM_PRIORITY, true));
        private volatile static long currentTimeStamp = System.currentTimeMillis();

        static {
            TIMER_SINGLE_THREAD_POOL.execute(() -> {
                log.info("时间戳跟踪线程已启动，可以使用currentTimestamp()进行获取");
                while (true) {
                    currentTimeStamp = System.currentTimeMillis();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ignore) {
                    }
                }
            });
        }

        public static long getCurrentTimeStamp() {
            return currentTimeStamp;
        }
    }
}
