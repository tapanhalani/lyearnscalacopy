#!/bin/bash

echo "hello tapan"
pwd
DOCKER_API_VERSION=1.22 sbt docker
curl -v  --unix-socket /var/run/docker.sock -H 'X-Registry-Auth: eyB1c2VybmFtZTogdGFwYW5oYWxhbmksIHBhc3N3b3JkOiAyOG1hcmNoMTk5NCwgZW1haWw6IHRhcGFuaGFsYW5pMjMxQGdtYWlsLmNvbSB9Cg==' -X POST http:/images/tapanhalani/lyearnscalacopy/push?tag=latest
