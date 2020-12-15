package com.kardidev.poc.cloud.probes.front.service.health;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import com.kardidev.poc.cloud.probes.front.service.modules.RequestPool;

/**
 * Custom Request Pool health indicator.
 * When there are no available threads to process tasks, module state changes to DOWN.
 * It's meant to be a part of readiness group, as the module can get back to operable state without any need to be restarted.
 */
@Component
public class RequestPoolHealthIndicator extends AbstractHealthIndicator {

    private final RequestPool requestPool;

    @Autowired
    public RequestPoolHealthIndicator(RequestPool requestPool) {
        this.requestPool = requestPool;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        if (isLackOfResources()) {
            builder.down();
        } else {
            builder.up();
        }
    }

    /**
     * If there are some tasks waiting in the queue, there is a risk that the next request will be rejected
     */
    private boolean isLackOfResources() {
        return requestPool.getStats().getQueuedTasks() == RequestPool.QUEUE_SIZE;
    }
}
