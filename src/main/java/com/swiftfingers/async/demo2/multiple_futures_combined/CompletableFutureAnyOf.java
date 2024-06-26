package com.swiftfingers.async.demo2.multiple_futures_combined;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*CompletableFuture.anyOf() as the name suggests, returns a new CompletableFuture which is completed
when any of the given CompletableFutures complete, with the same result.
The anyOf() static method returns a new CompletableFuture<Object> as soon as any of the given
CompletableFutures completes.
The problem with CompletableFuture.anyOf() is that if you have CompletableFutures that return
results of different types, then you won’t know the type of your final CompletableFuture.

CompletableFuture API exposes the join() method as a way of retrieving the value of the Future object
by blocking the thread until the execution is completed. We should notice that the caller thread will
 be blocked even if the execution of the CompletableFuture is happening on a different thread
*/
public class CompletableFutureAnyOf {
    static ExecutorService threadPool = Executors.newFixedThreadPool(5);

    public static CompletableFuture<String> searchA(String term) {

        return CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "..running");
            // long calculation
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "I got it in a..." + term;
        }, threadPool);
    }

    public static CompletableFuture<String> searchB(String term) {

        return CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "..running");
            // long calculation
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "I got it in b..." + term;
        }, threadPool);
    }

    public static CompletableFuture<String> searchC(String term) {

        return CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "..running");
            // long calculation
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "I got it in c..." + term;
        }, threadPool);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String term = "a good developer";

        CompletableFuture<String> searchA = searchA(term);
        CompletableFuture<String> searchB = searchB(term);
        CompletableFuture<String> searchC = searchC(term);

        CompletableFuture<Object> goodDeveloper = CompletableFuture.anyOf(searchA,
                searchB, searchC);

        System.out.println(goodDeveloper.get());
        threadPool.shutdown();
    }
}
