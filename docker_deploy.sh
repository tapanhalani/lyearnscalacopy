#!/bin/bash

echo "hello tapan"
pwd
DOCKER_API_VERSION=1.22 docker run --rm  -it tapanhalani/rancherpythonapi query 1s9
