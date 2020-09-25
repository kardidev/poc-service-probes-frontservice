# poc-service-probes-frontservice

A front service, which should survive under high load.
The instance should be excluded from load balancing, if it's not healthy temporary.
The instance should be restarted, if there is no chance to have it recovered automatically.

This repository is a part of a project, which is meant to test spring-actuator health end-points.



curl 'http://localhost:8080/frontservice/process?weight=10'
curl 'http://localhost:8080/frontservice/manage/health/readiness' | json_pp
curl -X POST -i http://localhost:8080/frontservice/manage/shutdown
