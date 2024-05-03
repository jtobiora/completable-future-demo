package com.swiftfingers.async.demo2.transform_future_results;


/*The CompletableFuture.get() method is blocking. It waits until the Future is completed and returns
the result after its completion.

We can use thenApply() method to process and transform the result of a CompletableFuture when it
arrives. It takes a Function as an argument, which means that it takes an argument of type T and
returns a result of type R.

We can also write a sequence of transformations on the CompletableFuture by attaching a series of
thenApply() callback methods. The result of one thenApply() method is passed to the next in the
series.
CompletableFuture API exposes the join() method as a way of retrieving the value of the Future object
by blocking the thread until the execution is completed. We should notice that the caller thread will
 be blocked even if the execution of the CompletableFuture is happening on a different thread
*/

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThenApply {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
            // thenApply();
           // thenApplyAsync();
            thenApplyWithExecutor();

    }

    //The task inside thenApply() is executed in the same thread where the supplyAsync() task is executed,
    // or in the main thread if the supplyAsync() task completes immediately
    private static void thenApply () throws ExecutionException, InterruptedException {
        CompletableFuture<String> calculation = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " ...running");
            return " .. returning the result !!!";
        });

        // attach a callback

        CompletableFuture<String> processor = calculation.thenApply(result -> {
            System.out.println(Thread.currentThread().getName() + " ...running");
            return "Thanks for " + result;
        });

        System.out.println(Thread.currentThread().getName() + " ...running");

        // block and wait for the future to return
        System.out.println(processor.get());
        System.out.println(Thread.currentThread().getName() + " ...running");
    }

    //To have more control over the thread that executes the callback task, you can use async
    // callbacks. If you use thenApplyAsync() callback, then it will be executed in a different thread
    // obtained from ForkJoinPool.commonPool() -
    private static void thenApplyAsync () throws ExecutionException, InterruptedException {
        CompletableFuture<String> calculation = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " ...running");
            return " .. returning the result !!!";
        });

        // attach a callback

        CompletableFuture<String> processor = calculation.thenApplyAsync(result -> {
            System.out.println(Thread.currentThread().getName() + " ...running");
            return "Thanks for " + result;
        });

        System.out.println(Thread.currentThread().getName() + " ...running");

        // block and wait for the future to return
        System.out.println(processor.get());
        System.out.println(Thread.currentThread().getName() + " ...running");
    }

    //If you pass an Executor to the thenApplyAsync() callback then the task will be
    // executed in a thread obtained from the Executor’s thread pool.
    private static void thenApplyWithExecutor () throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture<String> calculation = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " ----- running");
            return " .. returning the result !!!";
        }).thenApplyAsync(result -> {
            System.out.println("thenApplyAsync is running in a " +Thread.currentThread().getName());
            return "Thanks for " + result;
        }, threadPool);

        System.out.println(calculation.get());
        System.out.println(Thread.currentThread().getName() + " ...running");
    }
}
