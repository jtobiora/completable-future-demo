package com.swiftfingers.async.demo2.exception_handling;


/*In the exceptionally() method, you only have access to the exception and not the result.
If the CompletableFuture was completed successfully, then the logic inside "exceptionally()"
will be skipped.*/


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ExceptionallyDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String name = null;
        CompletableFuture<String> completableFuture = CompletableFuture
                .supplyAsync(() -> {
                    if (name == null)
                        throw new RuntimeException();
                    return "Hello " + name + " !!!";
                }).thenApply(r -> "Hey, " + r)
                .exceptionally(e -> "Exception occurred: " + e);

        System.out.println(completableFuture.get());
    }
}