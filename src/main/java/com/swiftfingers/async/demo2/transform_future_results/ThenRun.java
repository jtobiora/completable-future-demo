package com.swiftfingers.async.demo2.transform_future_results;

/*
If we neither need the value of the computation nor want to return some value at the end of the chain,
then we can use the thenRun() method. The thenRun() method accepts a Runnable and returns an instance
of the CompletableFuture.
Both thenAccept() and thenRun() are often used as the last callback in the callback chain
CompletableFuture API exposes the join() method as a way of retrieving the value of the Future object
by blocking the thread until the execution is completed. We should notice that the caller thread will
 be blocked even if the execution of the CompletableFuture is happening on a different thread
*/

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThenRun {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
         thenRun();
         thenRunAsync();
         thenRunAsyncWithExecutor();
    }


    //The task inside thenRun() is executed in the same thread where the runAsync() task is executed,
    // or in the main thread if the runAsync() task completes immediately
    private static void thenRun () throws ExecutionException, InterruptedException {
        CompletableFuture<Void> calculation = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " ...running");
            System.out.println(" .. I am done !!!");
        }).thenRun(() -> {
            System.out.println(Thread.currentThread().getName() + " ...running");
            System.out.println(" ..thanks, I am done too !!!");
        });

        calculation.get();
    }


    //To have more control over the thread that executes the callback task, you can use async
    // callbacks. If you use thenRunAsync() callback, then it will be executed in a different thread
    // obtained from ForkJoinPool.commonPool() -
    private static void thenRunAsync () throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " ...running");
            System.out.println(" .. I am done !!!");
        }).thenRunAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " ...running");
            System.out.println(" ..thanks, I am done too !!!");
        });

        future.get();
    }

    //If you pass an Executor to the thenRunAsync() callback then the task will be
    // executed in a thread obtained from the Executor’s thread pool.
    private static void thenRunAsyncWithExecutor () throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture<Void> calculation = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " ...running");
            System.out.println(" .. I am done !!!");
        }).thenRunAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " ...running");
            System.out.println(" ..thanks, I am done too !!!");
        }, threadPool);

        calculation.get();
        System.out.println("Finished!");
    }


}
