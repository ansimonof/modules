package org.myorg.modules.querypool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class QueryFuture<T> {

    private final CompletableFuture<T> future;

    private final QueryPool queryPool;

    public QueryFuture(QueryPool queryPool, CompletableFuture<T> future) {
        this.queryPool = queryPool;
        this.future = future;
    }

    protected void complete(T result) {
        future.complete(result);
    }

    protected void completeExceptionally(Throwable e) {
        future.completeExceptionally(e);
    }

    protected void cancel(boolean mayInterruptIfRunning) {
        future.cancel(mayInterruptIfRunning);
    }

    public T get() throws ExecutionException, InterruptedException {
        return future.get();
    }

    public T join() {
        return future.join();
    }

    public CompletableFuture<T> getFuture() {
        return future;
    }

    public <U> QueryFuture<U> thenApply(Function<? super T, Query<U>> fun) {
        QueryFuture<U> queryFuture = new QueryFuture<>(queryPool, new CompletableFuture<>());
        future.thenApply(t -> {
            Query<U> nextQuery = fun.apply(t);
            queryPool.execute(queryFuture, nextQuery);
            return null;
        }).exceptionally(e -> {
            queryFuture.future.completeExceptionally((Throwable) e);
            return null;
        });
        return queryFuture;
    }
}
