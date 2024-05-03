package com.swiftfingers.async.demo2.two_futures_combined;


/*
COMBINE DEPENDENT completableFutures USING thenCompose():
The thenCompose() method is used to combine the results of two dependent tasks when the second task
is dependent on the completion of the first.
If our callback function returns a CompletableFuture, and we want a flattened result from the
CompletableFuture chain, we need to use thenCompose().*

In earlier examples, the Supplier function passed to thenApply() callback would return a value.
What if our use-case is to return a CompletableFuture instead. That is the primary use-case of
thenCompose() method.

CompletableFuture API exposes the join() method as a way of retrieving the value of the Future object
by blocking the thread until the execution is completed. We should notice that the caller thread will
 be blocked even if the execution of the CompletableFuture is happening on a different thread
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class ThenCompose {
    static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static CompletableFuture<String> getUser(int userId) {
        
        return CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "..running");
            // long calculation
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "User-" + userId;
        }, threadPool);
    }

    public static CompletableFuture<String> getUserAccount(String user) {

        return CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "..running");
            // long calculation
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return user + "'s Account";
        }, threadPool);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int userId = 101;
//        CompletableFuture<String> userDetails =
//                getUser(userId)
//                        .thenCompose(user -> getUserAccount(user));
        CompletableFuture<String> userDetails =
                getUser(userId)
                        .thenComposeAsync(user -> getUserAccount(user),
                threadPool);
        System.out.println(userDetails.get());

        threadPool.shutdown();
    }
}
