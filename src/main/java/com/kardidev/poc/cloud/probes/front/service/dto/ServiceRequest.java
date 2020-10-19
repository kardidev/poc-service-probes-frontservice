package com.kardidev.poc.cloud.probes.front.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceRequest {

    private static final AtomicInteger requestCounter = new AtomicInteger(0);

    /**
     * Auto-generated request id
     */
    private int id;

    /**
     * The weight of the request defines a time, which is supposed to be spent on processing it.
     * Stating in another way, it reflects amount of work to do to get it processed.
     */
    private int weight;

    public static ServiceRequest build(int weight) {
        return new ServiceRequest(requestCounter.incrementAndGet(), weight);
    }
}
