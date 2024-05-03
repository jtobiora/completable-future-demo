package com.swiftfingers.async.demo1;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/*If you want to return some result from your background task, then CompletableFuture.supplyAsync()
is your companion. It takes a Supplier<T> and returns CompletableFuture<T> where T is the type of
the value obtained by calling the given supplier -*/

@Slf4j
public class SupplyAsyncDemo {
    public List<Employee> getEmployees() throws ExecutionException, InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        CompletableFuture<List<Employee>> listCompletableFuture = CompletableFuture
                .supplyAsync(() -> {
                    log.info("Executed by : " + Thread.currentThread().getName());
                    return EmployeeDatabase.fetchEmployees();
                }, executor);
        return listCompletableFuture.get(); //blocking call
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SupplyAsyncDemo supplyAsyncDemo = new SupplyAsyncDemo();
        List<Employee> employees = supplyAsyncDemo.getEmployees();
        employees.stream().forEach(System.out::println);
    }
}
