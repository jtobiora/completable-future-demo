package com.swiftfingers.async.demo2.background_tasks;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/*2) Run a background task asynchronously and RETURN RESULTs
If we want to run some background task asynchronously and want to return anything from that task,
we should use CompletableFuture.supplyAsync(). It takes a Supplier<T> and returns CompletableFuture<T>
where T is the type of the value obtained by calling the given supplier.
A Supplier<T> is a simple functional interface which represents a supplier of results. It has a
single get() method where we can write our background task and return the result.

CompletableFuture API exposes the join() method as a way of retrieving the value of the Future object
by blocking the thread until the execution is completed. We should notice that the caller thread will
 be blocked even if the execution of the CompletableFuture is happening on a different thread
*/

public class SupplyAsync {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                //a long running task
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Result of the asynchronous computation";
        });

        // block and wait for the future to return
        System.out.println(completableFuture.get());
    }
}
