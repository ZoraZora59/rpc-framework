package com.zora.rpc.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h3>rpc-framework</h3>
 * <h4>com.zora.rpc.common.thread</h4>
 * <p>基础的线程工厂</p>
 *
 * @author zora
 * @since 2020.12.30
 */
public class DefaultThreadFactory implements ThreadFactory {
    private static final String DEFAULT_NAME_PREFIX = "default-pool";
    /**
     * 名称前缀，不需要添加后面的'-'或者下划线之类的分割符
     */
    private final String namePrefix;
    /**
     * 是否守护线程
     */
    private final boolean daemon;
    /**
     * 如果要让这个参数生效，需要保证值属于[1,10]
     */
    private final int priority;
    /**
     * 是否单线程用的？单线程在拼名字的时候不会拼index
     */
    private final boolean single;
    private AtomicInteger index = null;

    public DefaultThreadFactory() {
        this.namePrefix = DEFAULT_NAME_PREFIX;
        this.daemon = false;
        this.priority = Thread.NORM_PRIORITY;
        this.single = false;
        tryInitIndex();
    }

    public DefaultThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix;
        this.daemon = false;
        this.priority = Thread.NORM_PRIORITY;
        this.single = false;
        tryInitIndex();
    }

    public DefaultThreadFactory(String namePrefix, boolean daemon) {
        this.namePrefix = namePrefix;
        this.daemon = daemon;
        this.priority = Thread.NORM_PRIORITY;
        this.single = false;
        tryInitIndex();
    }

    public DefaultThreadFactory(String namePrefix, boolean daemon, int priority) {
        this.namePrefix = namePrefix;
        this.daemon = daemon;
        this.priority = priority;
        this.single = false;
        tryInitIndex();
    }

    public DefaultThreadFactory(String namePrefix, boolean daemon, int priority, boolean single) {
        this.namePrefix = namePrefix;
        this.daemon = daemon;
        this.priority = priority;
        this.single = single;
        tryInitIndex();
    }

    void tryInitIndex() {
        if (!single) {
            index = new AtomicInteger(0);
        }
    }

    private String getThreadName() {
        return single ? namePrefix : namePrefix + '-' + String.format("%02d", index.getAndIncrement());
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(getThreadName());
        thread.setDaemon(daemon);
        thread.setPriority(priority);
        return thread;
    }

    public static class Builder {
        private boolean builderDaemon = false;
        private String builderNamePrefix = DEFAULT_NAME_PREFIX;
        private int builderPriority = Thread.NORM_PRIORITY;
        private boolean builderSingle = false;

        public Builder() {
        }

        public DefaultThreadFactory.Builder daemon(boolean daemon) {
            this.builderDaemon = daemon;
            return this;
        }

        public DefaultThreadFactory.Builder single(boolean single) {
            this.builderSingle = single;
            return this;
        }

        public DefaultThreadFactory.Builder namePrefix(String namePrefix) {
            this.builderNamePrefix = namePrefix;
            return this;
        }

        public DefaultThreadFactory.Builder priority(int priority) {
            this.builderPriority = priority;
            return this;
        }

        public DefaultThreadFactory build() {
            return new DefaultThreadFactory(this.builderNamePrefix, this.builderDaemon, this.builderPriority, this.builderSingle);
        }
    }
}
