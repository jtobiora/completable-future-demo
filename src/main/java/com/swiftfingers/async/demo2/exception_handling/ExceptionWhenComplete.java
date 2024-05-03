package com.swiftfingers.async.demo2.exception_handling;

/*In the whenComplete() method, we have access to the result and exception of the current completable
future as arguments, which you can consume and perform your desired action.
However, we cannot transform the current result or exception into another result.
We cannot return a value from the whenComplete() method.*/


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ExceptionWhenComplete {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String username = null;
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            if (username == null)
                throw new RuntimeException();
            return "Username is: " + username;
        }).whenComplete((r, e) -> {
            if (r == null)
                System.out.println("Exception occurred: " + e);
            else
                System.out.println(r);
        });

        try {
            completableFuture.get();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}