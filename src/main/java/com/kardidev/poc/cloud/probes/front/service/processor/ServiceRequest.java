package com.kardidev.poc.cloud.probes.front.service.processor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceRequest {

    private static final AtomicInteger requestCounter = new AtomicInteger(0);

    private int id;

    /**
     * The weight of the request defines a time, which will be spent on processing it.
     */
    private int weight;

    public static ServiceRequest build(int weight) {
        return new ServiceRequest(requestCounter.incrementAndGet(), weight);
    }
}
