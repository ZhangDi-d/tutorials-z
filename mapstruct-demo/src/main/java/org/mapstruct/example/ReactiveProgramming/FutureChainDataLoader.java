package org.mapstruct.example.ReactiveProgramming;

import java.util.concurrent.CompletableFuture;

/**
 * @author dizhang
 * @date 2021-03-17
 */
public class FutureChainDataLoader extends DataLoader {

    protected void doLoad() {
        CompletableFuture
                .runAsync(super::loadConfigurations)
                .thenRun(super::loadUsers)
                .thenRun(super::loadOrders)
                .whenComplete((result, throwable) -> { // 完成时回调
                    System.out.println("加载完成");
                })
                .join(); // 等待完成
    }

    public static void main(String[] args) {
        new FutureChainDataLoader().load();
    }
}