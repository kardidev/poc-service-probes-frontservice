package com.kardidev.poc.cloud.probes.front.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kardidev.poc.cloud.probes.front.service.dto.RequestPoolStats;
import com.kardidev.poc.cloud.probes.front.service.dto.ServiceRequest;
import com.kardidev.poc.cloud.probes.front.service.dto.ServiceResponse;
import com.kardidev.poc.cloud.probes.front.service.modules.FragileComponent;
import com.kardidev.poc.cloud.probes.front.service.modules.RequestPool;
import com.kardidev.poc.cloud.probes.front.service.modules.RequestProcessor;
import com.kardidev.poc.cloud.probes.front.service.modules.SelfRecoveringComponent;
import com.kardidev.poc.cloud.probes.front.service.utils.ServiceUtils;

@RestController
public class ServiceController {

    private final RequestProcessor requestProcessor;
    private final RequestPool requestPool;
    private final FragileComponent fragileComponent;
    private final SelfRecoveringComponent selfRecoveringComponent;

    @Autowired
    public ServiceController(RequestProcessor requestProcessor, RequestPool requestPool,
            FragileComponent fragileComponent, SelfRecoveringComponent selfRecoveringComponent) {
        this.requestProcessor = requestProcessor;
        this.requestPool = requestPool;
        this.fragileComponent = fragileComponent;
        this.selfRecoveringComponent = selfRecoveringComponent;
    }

    /**
     * Just to check, if web service works
     */
    @RequestMapping("/ping")
    public ResponseEntity ping() {
        return ResponseEntity.ok("Me alive!  [Host: " + ServiceUtils.getHostName() + "]");
    }

    /**
     * The service accepts a request and processes it during a given time, which is adjusted based on amount of resources,
     * that is concurrent tasks, used simultaneously. The more requests are being processed at the time of submission the more seconds will be added.
     *
     * @param weight expected amount of milliseconds to process the request
     */
    @GetMapping("/process")
    public ResponseEntity<ServiceResponse> process(@RequestParam(value = "weight", defaultValue = "1000") Integer weight) {

        ServiceRequest request = ServiceRequest.build(weight);

        if (!requestProcessor.isValid(request))
            return ResponseEntity.badRequest().build();

        return requestProcessor.process(request) ?
                ResponseEntity.accepted().body(ServiceResponse.of("ACCEPTED")) :
                ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ServiceResponse.of("REJECTED"));
    }

    /**
     * Breaks a critical component without any chance to have it recovered.
     * This should be a reason to have liveness check fail.
     */
    @GetMapping("/break")
    public ResponseEntity<ServiceResponse> breakDown() {
        fragileComponent.breakDown();
        return ResponseEntity.accepted().body(ServiceResponse.of("ACCEPTED"));
    }

    /**
     * Breaks a self recovering component.
     * This should be a reason to have readiness check fail.
     */
    @GetMapping("/interrupt")
    public ResponseEntity<ServiceResponse> interrupt() {
        selfRecoveringComponent.breakDown();
        return ResponseEntity.accepted().body(ServiceResponse.of("ACCEPTED"));
    }

    /**
     * This end-point is supposed to be used by internal monitoring tool
     *
     * @return request pool statistic in JSON
     */
    @GetMapping("/stats")
    public ResponseEntity<RequestPoolStats> stats() {
        return ResponseEntity.ok().body(requestPool.getStats());
    }
}
