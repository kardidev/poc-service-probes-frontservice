package com.kardidev.poc.cloud.probes.front.service.health;


import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import com.kardidev.poc.cloud.probes.front.service.processor.LoadManager;

@Component
public class RequestPoolHealthIndicator extends AbstractHealthIndicator {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int RESOURCES_AVAILABLE = 3;

    private final LoadManager loadManager;

    @Autowired
    public RequestPoolHealthIndicator(LoadManager loadManager) {
        this.loadManager = loadManager;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        if (isLackOfResources()) {
            builder.down();
        } else {
            builder.up();
        }
    }

    private boolean isLackOfResources() {
        return loadManager.getActiveCount() > RESOURCES_AVAILABLE;
    }
}
