package com.kardidev.poc.cloud.probes.front.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestPoolStats {

    /**
     * Amount of tasks, which are being processed at this moment of time
     */
    private int activeTasks;

    /**
     * Amount of tasks waiting in a queue
     */
    private int queuedTasks;

    /**
     * Total amount of finished tasks since this instance has been started
     */
    private long completedTasks;
}
