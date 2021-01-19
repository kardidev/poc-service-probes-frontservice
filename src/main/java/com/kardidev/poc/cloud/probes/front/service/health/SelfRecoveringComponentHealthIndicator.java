package com.kardidev.poc.cloud.probes.front.service.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import com.kardidev.poc.cloud.probes.front.service.modules.SelfRecoveringComponent;

@Component
public class SelfRecoveringComponentHealthIndicator extends AbstractHealthIndicator {

    private final SelfRecoveringComponent selfRecoveringComponent;

    @Autowired
    public SelfRecoveringComponentHealthIndicator(SelfRecoveringComponent selfRecoveringComponent) {
        this.selfRecoveringComponent = selfRecoveringComponent;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        if (selfRecoveringComponent.isOperable()) {
            builder.up();
        } else {
            builder.down();
        }
    }
}
