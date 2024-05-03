package com.swiftfingers.async.demo2.background_tasks;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*
1) Run a background task asynchronously
If you want to run some background task asynchronously and don’t want to return anything from the
task, then you can use CompletableFuture.runAsync() method. It takes a Runnable object and returns
CompletableFuture<Void>.
runAsync() and supplyAsync() methods execute their tasks in a separate thread which was never
explicitly created by us.
CompletableFuture executes these tasks in a thread obtained from the global ForkJoinPool.commonPool().
But we can also create a Thread Pool and pass it to runAsync() and supplyAsync() methods to let them
execute their tasks in a thread obtained from our own thread pool.

CompletableFuture API exposes the join() method as a way of retrieving the value of the Future object
by blocking the thread until the execution is completed. We should notice that the caller thread will
 be blocked even if the execution of the CompletableFuture is happening on a different thread
*/
public class RunAsync {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            //simulate a long-running Job here
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " ... long running job completed !!!");
        });

        System.out.println(Thread.currentThread().getName() + " running..");

        // block and wait for the future to complete
        completableFuture.get();
        System.out.println(Thread.currentThread().getName() + " running..");
    }
}
