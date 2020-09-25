package com.kardidev.poc.cloud.probes.front.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kardidev.poc.cloud.probes.front.service.processor.RequestProcessor;
import com.kardidev.poc.cloud.probes.front.service.processor.ServiceRequest;

@RestController
public class ServiceController {

    private final RequestProcessor requestProcessor;

    @Autowired
    public ServiceController(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    @RequestMapping("/ping")
    public ResponseEntity index() {
        return ResponseEntity.ok("Me alive!");
    }

    @GetMapping("/process")
    public ResponseEntity process(@RequestParam(value = "weight", defaultValue = "1") Integer weight) {

        ServiceRequest request = ServiceRequest.build(weight);

        if (requestProcessor.isValid(request)) {

            return requestProcessor.process(request) ?
                    ResponseEntity.accepted().build() :
                    ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        return ResponseEntity.badRequest().build();
    }

}
