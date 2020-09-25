package com.kardidev.poc.cloud.probes.front.service.health;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReadinessStateListener {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @EventListener
    public void onStateChange(AvailabilityChangeEvent<ReadinessState> event) {
        log.info("Readiness state changed to: " + event.getState());
    }

}
