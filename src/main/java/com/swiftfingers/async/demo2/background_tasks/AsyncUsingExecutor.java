package com.swiftfingers.async.demo2.background_tasks;


/*A note about Executor and Thread Pool -
You might be wondering that - Well, I know that the runAsync() and supplyAsync() methods execute
their tasks in a separate thread. But, we never created a thread right?
Yes! CompletableFuture executes these tasks in a thread obtained from the global
ForkJoinPool.commonPool().
You can also create a Thread Pool and pass it to runAsync() and supplyAsync() methods to
let them execute their tasks in a thread obtained from your thread pool.
All the methods in the CompletableFuture API has two variants - One which accepts an Executor as an
argument and one which doesn’t -

// Variations of runAsync() and supplyAsync() methods
static CompletableFuture<Void>	runAsync(Runnable runnable)
static CompletableFuture<Void>	runAsync(Runnable runnable, Executor executor)
static <U> CompletableFuture<U>	supplyAsync(Supplier<U> supplier)
static <U> CompletableFuture<U>	supplyAsync(Supplier<U> supplier, Executor executor)

*/

import java.util.concurrent.*;

public class AsyncUsingExecutor {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Executor executor = Executors.newFixedThreadPool(10);
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            System.out.println(Thread.currentThread().getName() + " ... long running job completed !!!");
            return "Result of the asynchronous computation";
        }, executor);

        System.out.println(Thread.currentThread().getName() + " running..");

        future.get();
        System.out.println(Thread.currentThread().getName() + " running..");
    }
}
