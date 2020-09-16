# poc-service-probes-frontserver

A front service, which should survive under high load.
The instance should be excluded from load balancing, if it's not healthy temporary.
The instance should be restarted, if there is no chance to have it recovered automatically.

This repository is a part of a project, which is meant to test spring-actuator health end-points.
