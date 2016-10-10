#!/bin/bash

echo "hello tapan"
pwd
DOCKER_API_VERSION=1.22 docker run --rm tapanhalani/rancherpythonapi query 1s9
DOCKER_API_VERSION=1.22 docker run --rm tapanhalani/rancherpythonapi upgrade 1s9
