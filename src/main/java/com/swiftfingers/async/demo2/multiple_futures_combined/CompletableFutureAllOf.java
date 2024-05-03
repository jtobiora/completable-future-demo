package com.swiftfingers.async.demo2.multiple_futures_combined;


/*The thenCompose() and thenCombine() methods can be used to combine two CompletableFutures together.
In order to combine any number of CompletableFutures, we can use either of the following two methods.

static CompletableFuture<Void>	 allOf(CompletableFuture<?>... cfs)
static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs)

                             CompletableFuture.allOf()
    CompletableFuture.allOf is used in scenarios when you have a List of independent futures that
    you want to run in parallel and do something after all of them are complete.
    The allOf() static method is used to combine the results of a number of independent tasks, when
    "none" of the tasks are dependent on any other task. The return type of this method is
    CompletableFuture, hence it does not return the combined results of all CompletableFutures.
    But we can get the results of all the wrapped CompletableFutures with the help of
    CompletableFuture.join() method and the Java 8 Streams API.

CompletableFuture API exposes the join() method as a way of retrieving the value of the Future object
by blocking the thread until the execution is completed. We should notice that the caller thread will
 be blocked even if the execution of the CompletableFuture is happening on a different thread
*/


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompletableFutureAllOf {

    static ExecutorService threadPool = Executors.newFixedThreadPool(5);

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

    public static CompletableFuture<String> getUserProfile(String user) {

        return CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "..running");
            // long calculation
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return user + "'s Profile";
        }, threadPool);
    }

    public static CompletableFuture<String> getUserTransactions(String user) {

        return CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "..running");
            // long calculation
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return user + "'s Transactions";
        }, threadPool);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String user = "User";

        CompletableFuture<String> account = getUserAccount(user);
        CompletableFuture<String> profile = getUserProfile(user);
        CompletableFuture<String> transactions = getUserTransactions(user);

        CompletableFuture<Void> userDetailsFuture = CompletableFuture.allOf(account,
                profile, transactions);

        //The CompletableFuture.join() method is similar to the get() method; the only difference
        // is that it throws an unchecked exception if the Future does not complete normally.
        String userDetails = Stream.of(account, profile, transactions)
                .map(CompletableFuture::join)
                .collect(Collectors.joining(" "));

        System.out.println(userDetails);
        threadPool.shutdown();
    }

}
