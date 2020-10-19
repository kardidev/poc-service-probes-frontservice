package com.kardidev.poc.cloud.probes.front.service.utils;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ServiceUtils {

    /**
     * In Kubernetes it's expected to have HOSTNAME variable set to pod internal name
     */
    public static String getHostName() {
        String hostName = System.getenv("HOSTNAME");
        return StringUtils.isEmpty(hostName) ? getLastRequestRemoteIp() : hostName;
    }

    private static String getLastRequestRemoteIp() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
        if (ip.equals("0:0:0:0:0:0:0:1"))
            ip = "127.0.0.1";
        return ip;
    }
}
