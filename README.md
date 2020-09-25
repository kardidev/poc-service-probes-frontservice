# poc-service-probes-frontservice

A front service, which should survive under high load.
The instance should be excluded from load balancing, if it's not healthy temporary.
The instance should be restarted, if there is no chance to have it recovered automatically.

This repository is a part of a project, which is meant to test spring-actuator health end-points.

## API to test:

### Submit a request with a given weight

The service accepts a request and processes it during a given time, which is adjusted based on amount of resources, 
that is concurrent tasks, used simultaneously. The more requests are being processed at the time of submission the more seconds will be added. 

* URI: `frontservice/process`
* URL Parameters:  
    * **`weight`** minimum amount of seconds service should spend on this request, default is 1
* Response:
    * HTTP Status `202` if the request was accepted successfully
    * HTTP Status `429` if the request was rejected due to lack of resources

* Example:
```$xslt
curl -i 'http://localhost:8080/frontservice/process?weight=10'
```

### Check service readiness state

The service provides its readiness state, which depends on many components.
If state differs from UP (200), the node is supposed to be excluded from load balancing list by cloud orchestrator.

* URI: `frontservice/manage/health/readiness`
* Response:
    * JSON Body with current state
    * HTTP Status `200` if state is UP
    * HTTP Status `503` if state is either DOWN or OUT_OF_SERVICE 

* Example:
```$xslt
curl 'http://localhost:8080/frontservice/manage/health/readiness' | json_pp
```

### Check service liveness state

The service provides its liveness state, which depends on many critical components.
If state is DOWN, the node is supposed to be restarted by cloud orchestrator.

* URI: `frontservice/manage/health/liveness`
* Response:
    * JSON Body with current state
    * HTTP Status `200` if state is UP
    * HTTP Status `503` if state is DOWN 

* Example:
```$xslt
curl 'http://localhost:8080/frontservice/manage/health/liveness' | json_pp
```

### Shutdown service

* URI: `[POST] frontservice/manage/shutdown`

* Example:
```$xslt
curl -X POST http://localhost:8080/frontservice/manage/shutdown
```

