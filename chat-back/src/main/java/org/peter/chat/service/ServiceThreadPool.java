package org.peter.chat.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class ServiceThreadPool {
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
            100,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());

    public void submit(Runnable task) {
        threadPoolExecutor.submit(task);
    }
}
