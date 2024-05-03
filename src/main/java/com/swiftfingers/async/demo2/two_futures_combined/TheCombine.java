package com.swiftfingers.async.demo2.two_futures_combined;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/*COMBINE two INDEPENDENT completableFutures USING thenCombine():
The thenCombine() method is used to combine the results of two independent tasks when the two tasks
are independent of each other's completion.

While thenCompose() is used to combine two Futures where one future is dependent on the other,
thenCombine() is used when you want two Futures to run independently and do something after both
are complete.
The callback function passed to thenCombine() will be called when both the Futures are complete.*

CompletableFuture API exposes the join() method as a way of retrieving the value of the Future object
by blocking the thread until the execution is completed. We should notice that the caller thread will
 be blocked even if the execution of the CompletableFuture is happening on a different thread

/
 */
public class TheCombine {
    static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static CompletableFuture<UserAccount> getUserAccount(String user) {

        return CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "..running");
            // long calculation
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return new UserAccount("Richards Kerry","Lagos", 'M',21);
        }, threadPool);
    }

    public static CompletableFuture<Profile> getUserProfile(String user) {

        return CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "..running");
            // long calculation
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return new Profile("richards@abc.com",Arrays.asList("Bsc,M.Sc,PhD"));
        }, threadPool);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String user = "User";

        CompletableFuture<String> userDetails = getUserAccount(user).thenCombineAsync(
                getUserProfile(user),
                (account, profile) -> {
                    System.out.println("----" + account.toString());
                    System.out.println("----" + profile.toString());
                    return account + " and " + profile;
                },
                threadPool);


        System.out.println(userDetails.get());

        //Another example of how to use thenCombine()
        thenCombineExample2();

        threadPool.shutdown();
    }

    private static void thenCombineExample2 () throws ExecutionException, InterruptedException {
        System.out.println("Retrieving weight.");
        CompletableFuture<Double> weightInKgFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return 65.0;
        });

        System.out.println("Retrieving height.");
        CompletableFuture<Double> heightInCmFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return 177.8;
        });

        System.out.println("Calculating BMI.");
        CompletableFuture<Double> combinedFuture = weightInKgFuture
                .thenCombine(heightInCmFuture, (weightInKg, heightInCm) -> {
                    Double heightInMeter = heightInCm/100;
                    return weightInKg/(heightInMeter*heightInMeter);
                });

        System.out.println("Your BMI is - " + combinedFuture.get());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserAccount {
        private String name;
        private String address;
        private char gender;
        private int age;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Profile {
        private String username;
        private List<String> qualifications;
    }


}
