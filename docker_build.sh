#!/bin/bash

tar zcf lyearn-backend.tar.gz .
echo "hello tapan
"
curl -v  --unix-socket /var/run/docker.sock -H 'Content-Type: application/tar' --data-binary @lyearn-backend.tar.gz  -X POST "http:/build?dockerfile=target/docker/Dockerfile&t=tapanhalani/lyearn-backend"
