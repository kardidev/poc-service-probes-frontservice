package com.kardidev.poc.cloud.probes.front.service.processor;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Keeps information about active tasks.
 * Emulates resource sharing between tasks.
 */
@Component
public class LoadManager {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Map<Integer, ServiceRequest> activeRequests = new ConcurrentHashMap<>();

    @Autowired
    private RequestPool pool;

    /**
     * Register a request and return amount of active ones.
     * This value will be translated to amount of seconds, this request needs to be processed.
     */
    int registerRequest(ServiceRequest request) {
        activeRequests.put(request.getId(), request);
        return activeRequests.size();
    }

    /**
     * Remove a request from tracking
     */
    void withdrawRequest(int requestId) {
        activeRequests.remove(requestId);
    }

    public int getActiveCount() {
        return activeRequests.size();
    }

    @Scheduled(initialDelay = 1_000, fixedRate = 1_000)
    private void logStatistic() {

        int activeTasks = activeRequests.size();

        if (activeTasks == 0)
            return;

        log.info("########## Active tasks: " + activeTasks + "  Queued requests: " + pool.getQueueSize());
        activeRequests.values()
                .forEach(request -> log.info("# " + request.getId() + " : [" + request.getWeight() + "]"));
    }
}
