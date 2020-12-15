package com.kardidev.poc.cloud.probes.front.service.modules;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kardidev.poc.cloud.probes.front.service.dto.RequestPoolStats;
import com.kardidev.poc.cloud.probes.front.service.dto.ServiceRequest;

@Component
public class RequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private FragileComponent fragileComponent;
    private final RequestPool requestPool;

    @Autowired
    public RequestProcessor(FragileComponent fragileComponent, RequestPool requestPool) {
        this.fragileComponent = fragileComponent;
        this.requestPool = requestPool;
    }

    /**
     * Checks if a request is valid before starting processing it.
     *
     * @param request an instance of ServiceRequest
     * @return true, if the request is valid, false otherwise
     */
    public boolean isValid(ServiceRequest request) {

        return request.getWeight() > 0 && request.getWeight() < 60_000;
    }

    /**
     * Submits a request to a request pool to execute.
     *
     * @param request an instance of ServiceRequest
     * @return true, if there are available resources and the request was accepted for execution
     */
    public boolean process(ServiceRequest request) {

        if (!fragileComponent.isOperable()) {
            throw new RuntimeException("Critical module error!");
        }

        Task task = new Task(request, requestPool);
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
        RequestPoolStats stats = requestPool.getStats();
        return " (Pool: " + stats.getActiveTasks() + "  Queue: " + stats.getQueuedTasks() + ")";
    }

    private static class Task implements Runnable {

        private final ServiceRequest request;
        private final RequestPool requestPool;

        Task(ServiceRequest request, RequestPool requestPool) {
            this.request = request;
            this.requestPool = requestPool;
        }

        @Override
        public void run() {

            // expected processing time based on request weight
            int timeToProcess = request.getWeight();

            timeToProcess += requestPool.getStats().getActiveTasks() * 1000;

            try {
                Thread.sleep(timeToProcess);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }
}
