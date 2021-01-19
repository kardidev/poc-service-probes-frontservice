#!/bin/zsh

kubectl delete services frontservice

kubectl delete deployment frontservice

mvn spring-boot:build-image

kubectl apply -f health-service-deploy-config.yaml

sleep 3

kubectl get pod -o wide