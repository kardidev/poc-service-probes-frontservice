package com.kardidev.poc.cloud.probes.front.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.kardidev.poc.cloud.probes.front.service.utils.ServiceUtils;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceResponse {
    private String podName;
    private String status;

    public static ServiceResponse of(String status) {
        return new ServiceResponse(
                ServiceUtils.getHostName(),
                status);
    }
}
