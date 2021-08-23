package org.myorg.modules.querypool.threadpool;

import java.util.concurrent.*;

public class DefaultThreadPoolExecutor extends ThreadPoolExecutor {

    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public DefaultThreadPoolExecutor(
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit timeUnit,
            BlockingQueue<Runnable> blockingQueue,
            String threadFactoryName,
            Thread.UncaughtExceptionHandler uncaughtExceptionHandler
    ) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, blockingQueue,
                new DefaultThreadFactory(threadFactoryName, uncaughtExceptionHandler));
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if (uncaughtExceptionHandler == null) {
            return;
        }

        if (t == null) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
            } catch (ExecutionException e) {
                t = e.getCause();
            } catch (Throwable e) {
                t = e;
            }
        }

        if (t != null) {
            uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), t);
        }
    }
}
