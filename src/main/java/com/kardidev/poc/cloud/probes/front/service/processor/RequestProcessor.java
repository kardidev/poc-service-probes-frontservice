package com.kardidev.poc.cloud.probes.front.service.processor;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RequestPool requestPool;
    private final LoadManager loadManager;

    @Autowired
    public RequestProcessor(RequestPool requestPool, LoadManager loadManager) {
        this.requestPool = requestPool;
        this.loadManager = loadManager;
    }

    public boolean isValid(ServiceRequest request) {

        return request.getWeight() > 0 && request.getWeight() < 60;
    }

    public boolean process(ServiceRequest request) {
        Task task = new Task(request, loadManager);
        try {
            requestPool.submit(task);
            log.info("Request " + request.getId() + " was accepted" + getPoolStats());
            return true;

        } catch (RejectedExecutionException e) {
            log.warn("Request " + request.getId() + " was rejected" + getPoolStats());
            return false;
        }
    }

    private String getPoolStats() {
        return " (Pool: " + requestPool.getActiveTasks() + "  Queue: " + requestPool.getQueueSize() + ")";
    }

    private static class Task implements Runnable {

        private final ServiceRequest request;
        private final LoadManager loadManager;

        Task(ServiceRequest request, LoadManager loadManager) {
            this.request = request;
            this.loadManager = loadManager;
        }

        @Override
        public void run() {

            int timeToProcess = request.getWeight();

            timeToProcess += loadManager.registerRequest(request);

            try {
                Thread.sleep(timeToProcess * 1000);
            } catch (InterruptedException e) {
                // do nothing
            }

            loadManager.withdrawRequest(request.getId());

        }
    }
}
