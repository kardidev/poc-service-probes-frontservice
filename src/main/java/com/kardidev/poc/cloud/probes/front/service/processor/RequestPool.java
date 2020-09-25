package com.kardidev.poc.cloud.probes.front.service.processor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Component
public class RequestPool {

    private static final int QUEUE_SIZE = 5;
    private static final int CORE_SIZE = 5;
    private static final int MAX_SIZE = 5;
    private static final int KEEP_ALIVE_SEC = 10;

    private ThreadPoolExecutor executor;

    @PostConstruct
    public void init() {
        executor = createExecutor();
    }

    void submit(Runnable task) {
        executor.submit(task);
    }

    int getActiveTasks() {
        return executor.getActiveCount();
    }

    int getQueueSize() {
        return executor.getQueue().size();
    }

    private ThreadPoolExecutor createExecutor() {

        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);

        ThreadFactory namedThreadFactory =
                new ThreadFactoryBuilder().setNameFormat("request-processor").build();

        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(CORE_SIZE, MAX_SIZE, KEEP_ALIVE_SEC, TimeUnit.SECONDS, blockingQueue, namedThreadFactory);
        executor.allowCoreThreadTimeOut(true);

        return executor;
    }
}
