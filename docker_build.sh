#!/bin/bash

tar zcf lyearn-backend.tar.gz .
curl -v  --unix-socket /var/run/docker.sock -H 'Content-Type: application/tar' --data-binary @lyearn-backend.tar.gz  -X POST http:/build?t=tapanhalani/lyearn-backend&dockerfile=target/docker/Dockerfile
