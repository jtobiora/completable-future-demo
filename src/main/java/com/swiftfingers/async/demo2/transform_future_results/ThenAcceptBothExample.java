package com.swiftfingers.async.demo2.transform_future_results;


import java.util.concurrent.CompletableFuture;

/*In Java, thenAcceptBoth() and runAfterBoth() are methods provided by the CompletableFuture class to
combine the results of two CompletableFuture instances when both of them complete. thenAcceptBoth()
allows you to perform a consumer action on the combined results, while runAfterBoth() is used for
running a runnable action without consuming the results.*/

public class ThenAcceptBothExample {
    public static void main(String[] args) {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> 20);

        // Using thenAcceptBoth to combine results and perform an action
        CompletableFuture<Void> combinedResult = future1.thenAcceptBoth(future2, (result1, result2) -> {
            int sum = result1 + result2;
            System.out.println("Combined Result: " + sum);
        });

        // Using runAfterBoth to run an action after both futures complete
        CompletableFuture<Void> actionAfterBoth = future1.runAfterBoth(future2, () -> {
            System.out.println("Both futures have completed.");
        });

        // Join the CompletableFuture to wait for the results
        combinedResult.join();
        actionAfterBoth.join();
    }
}
