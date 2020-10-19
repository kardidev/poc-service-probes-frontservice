package com.kardidev.poc.cloud.probes.front.service.modules;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

/**
 * A critical component, which cannot be self-recovered, once it gets broken.
 * Such a module should be considered as a part of liveness group.
 */
@Component
public class FragileComponent {

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

}
