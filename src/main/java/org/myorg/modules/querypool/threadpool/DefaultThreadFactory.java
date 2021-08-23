package org.myorg.modules.querypool.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {

    private final ThreadGroup group;
    private final AtomicInteger threadId = new AtomicInteger(1);

    public DefaultThreadFactory(String factoryName, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.group = new DefaultThreadGroup(factoryName, uncaughtExceptionHandler);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, group.getName() + "-thread-" + threadId.getAndIncrement());
        return t;
    }

    private static class DefaultThreadGroup extends ThreadGroup {

        private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

        public DefaultThreadGroup(String name, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
            super(name);
            this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            uncaughtExceptionHandler.uncaughtException(t, e);
        }
    }

}
