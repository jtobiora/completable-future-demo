package com.swiftfingers.async.demo2.transform_future_results;


/*If you do not want to return anything from the callback function and just want to run some piece of
code after the completion of the CompletableFuture, then we can use thenAccept() method.
CompletableFuture.thenAccept() takes a Consumer<T> and returns CompletableFuture<Void>. It has
access to the result of the CompletableFuture on which it is attached.
CompletableFuture API exposes the join() method as a way of retrieving the value of the Future object
by blocking the thread until the execution is completed. We should notice that the caller thread will
 be blocked even if the execution of the CompletableFuture is happening on a different thread
*/

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThenAccept {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        thenAccept();
        thenAcceptAsync();
        thenAcceptAsyncWithExecutor();

    }

    //The task inside thenAccept() is executed in the same thread where the supplyAsync() task is executed,
    // or in the main thread if the supplyAsync() task completes immediately
    private static void thenAccept () throws ExecutionException, InterruptedException {
        CompletableFuture<Void> calculation = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " ...running");
            return " .. returning the result !!!";
        }).thenAccept(result -> {
            System.out.println(Thread.currentThread().getName() + " ...running");
            System.out.println("Thanks for " + result);
        });

        calculation.get();
    }

    //To have more control over the thread that executes the callback task, you can use async
    // callbacks. If you use thenAcceptAsync() callback, then it will be executed in a different thread
    // obtained from ForkJoinPool.commonPool() -
    private static void thenAcceptAsync () throws ExecutionException, InterruptedException {
        CompletableFuture<Void> calculation = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " ...running");
            return " .. returning the result !!!";
        }).thenAcceptAsync(result -> {
            System.out.println(Thread.currentThread().getName() + " ...running");
            System.out.println("Thanks for " + result);
        });

        calculation.get();
    }

    //If you pass an Executor to the thenAcceptAsync() callback then the task will be
    // executed in a thread obtained from the Executor’s thread pool.
    private static void thenAcceptAsyncWithExecutor () throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        CompletableFuture<Void> calculation = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " ...running");
            return " .. returning the result !!!";
        }).thenAcceptAsync(result -> {
            System.out.println(Thread.currentThread().getName() + " ...running");
            System.out.println("Thanks for " + result);
        }, threadPool);

        calculation.get();
    }
}
