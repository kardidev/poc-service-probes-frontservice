package com.kardidev.poc.cloud.probes.front.service.modules;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.kardidev.poc.cloud.probes.front.service.dto.RequestPoolStats;

/**
 * A critical component, which can recover itself after some time, once it gets inoperable.
 * Such a module should be considered as a part of readiness group.
 */
@Component
public class RequestPool {

    public static final int QUEUE_SIZE = 5;
    private static final int CORE_SIZE = 5;
    private static final int MAX_SIZE = 5;
    private static final int KEEP_ALIVE_SEC = 10;

    private ThreadPoolExecutor executor;

    private AtomicLong rejected;

    @PostConstruct
    public void init() {
        rejected = new AtomicLong(0);
        executor = createExecutor();
    }

    /**
     * Submit a task to execute
     *
     * @param task instance of Runnable
     */
    void submit(Runnable task) {
        try {
            executor.submit(task);
        } catch (RejectedExecutionException e) {
            rejected.incrementAndGet();
            throw e;
        }
    }

    /**
     * @return RequestPoolStats with active, queued and completed tasks counters
     */
    public RequestPoolStats getStats() {
        return new RequestPoolStats(
                executor.getActiveCount(),
                executor.getQueue().size(),
                executor.getCompletedTaskCount(),
                rejected.get()
        );
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
