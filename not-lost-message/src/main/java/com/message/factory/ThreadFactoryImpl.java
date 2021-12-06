package com.message.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 自定义线程工厂
 */
public class ThreadFactoryImpl implements ThreadFactory {
    private final AtomicLong threadIndex = new AtomicLong(0);
    private final String threadNamePrefix;
    private final boolean daemon;

    public ThreadFactoryImpl(String threadNamePrefix) {
        this(threadNamePrefix,false);
    }

    public ThreadFactoryImpl(String threadNamePrefix, boolean daemon) {
        this.threadNamePrefix = threadNamePrefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, threadNamePrefix + "["+this.threadIndex.incrementAndGet()+"]");
        thread.setDaemon(daemon);
        return thread;
    }
}
