package com.sivanagireddy.events;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        EventSender eventSender = new EventSender();

        EventConsumer eventConsumer = new EventConsumer();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(eventSender::sendEvents);
        executor.execute(eventConsumer::consumeEvents);

        executor.shutdown();

        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }
        System.out.println("Terminating jvm");
    }
}
