package com.kardidev.poc.cloud.probes.front.service.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import com.kardidev.poc.cloud.probes.front.service.modules.FragileComponent;

@Component
public class FragileComponentHealthIndicator extends AbstractHealthIndicator {

    private final FragileComponent fragileComponent;

    @Autowired
    public FragileComponentHealthIndicator(FragileComponent fragileComponent) {
        this.fragileComponent = fragileComponent;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        if (fragileComponent.isOperable()) {
            builder.up();
        } else {
            builder.down();
        }
    }
}
