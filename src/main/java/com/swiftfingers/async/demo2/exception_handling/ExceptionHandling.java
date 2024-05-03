package com.swiftfingers.async.demo2.exception_handling;

/*The CompletableFuture class allows us to handle exceptions in a special handle() method.
The handle() method has two parameters - the result of a computation (if successful) and the
exception thrown (if the computation could not be completed normally).
In the "handle()" method, we can transform the result or recover the exception.

It is called irrespective of whether or not an exception occurs. If an exception occurs, the value
of ex argument will be non null, otherwise the value of res argument is non null in a success
scenario.*/

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ExceptionHandling {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String username = null;
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                    if (username == null)
                        throw new RuntimeException();
                    return "Username is: " + username;
                }).handle((res, ex) -> res == null ? "Invalid Username: " + ex.getMessage() : res)
                .thenApply(r -> r + " ...");

        System.out.println(completableFuture.get());
    }
}
