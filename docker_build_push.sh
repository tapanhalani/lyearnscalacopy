#!/bin/bash

echo "hello tapan"
pwd
DOCKER_API_VERSION=1.22 sbt docker
curl -v  --unix-socket /var/run/docker.sock -H 'X-Registry-Auth: eyAidXNlcm5hbWUiOiAidGFwYW5oYWxhbmkiLCAicGFzc3dvcmQiOiAiMjhtYXJjaDE5OTQiLCAiZW1haWwiOiAidGFwYW5oYWxhbmkyMzFAZ21haWwuY29tIiB9Cg==' -X POST http:/images/tapanhalani/node-hello-world/push?tag=latest
