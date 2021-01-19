package com.kardidev.poc.cloud.probes.front.service.modules;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * A non-critical component, which can be repaired automatically after some time.
 * Such a module should be considered as a part of readiness group.
 */
@Component
public class SelfRecoveringComponent {

    private final AtomicBoolean broken = new AtomicBoolean(false);

    /**
     * Mark this module as broken
     */
    public void breakDown() {
        broken.set(true);
    }

    /**
     * @return true, if the module is operable
     */
    public boolean isOperable() {
        return !broken.get();
    }

    @Scheduled(initialDelay = 15_000, fixedRate = 30_000)
    private void recoverIfBroken() {
        broken.set(false);
    }

}
