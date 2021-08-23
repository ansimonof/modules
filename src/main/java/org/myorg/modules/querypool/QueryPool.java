package org.myorg.modules.querypool;

import org.myorg.modules.exception.ModuleException;
import org.myorg.modules.querypool.database.PersistenceContext;
import org.myorg.modules.querypool.threadpool.DefaultThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class QueryPool {

    private final ThreadPoolExecutor threadPoolExecutor;

    private final EntityManagerFactory emf;

    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (t, e) -> {
    // Need Logging
        System.out.println("Crash");
        e.printStackTrace();
        System.exit(1);
    };

    @Autowired
    public QueryPool(EntityManagerFactory emf) {
        this.emf = emf;
        this.threadPoolExecutor = new DefaultThreadPoolExecutor(
                3,
                3,
                0,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(6),
                "QueryPool",
                uncaughtExceptionHandler
        );
    }


    private class QueryWrapper<T> {
        private Query<T> query;
        private QueryFuture<T> future;

        public QueryWrapper(Query<T> query, QueryFuture<T> future) {
            this.future = future;
            this.query = query;
        }

        public QueryWrapper(QueryPool queryPool, Query<T> query) {
            this(query, new QueryFuture<>(queryPool, new CompletableFuture<>()));
        }

        public void execute() {
            EntityTransaction et = null;
            try {
                EntityManager em = emf.createEntityManager();
                et = em.getTransaction();

                et.begin();
                T res = query.execute(new PersistenceContext(em));
                future.complete(res);
                et.commit();
                em.close();

            } catch (ModuleException e) {
                et.rollback();
                future.completeExceptionally(e);
            } catch (Throwable e) {
                et.rollback();
                future.cancel(true);
                throw new RuntimeException(e);
            }
        }
    }

    protected <T> void execute(QueryFuture<T> queryFuture, Query<T> query) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>(query, queryFuture);
        execute(queryWrapper);
    }

    protected <T> QueryFuture<T> execute(QueryWrapper<T> queryWrapper) {
        submitQuery(queryWrapper);
        return queryWrapper.future;
    }

    public <T> QueryFuture<T> execute(Query<T> query) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<T>(this, query);
        return execute(queryWrapper);
    }

    public <T> CompletableFuture<T> executeAndReturnFuture(Query<T> query) {
        return execute(query).getFuture();
    }

    private <T> void submitQuery(QueryWrapper<T> queryWrapper) {
        threadPoolExecutor.submit(queryWrapper::execute);
    }

}
