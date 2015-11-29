#!/usr/bin/env bash

if [ "$DOCKER_MACHINE_NAME" = "" ]; then
    echo "warning your DOCKER_MACHINE_NAME environment variable was not set. have you run: echo \"\$(docker-machine env <MACHINE-NAME)\"?"
    DOCKER_MACHINE_NAME=default
fi

if [ "$DEBUG_JAVA" = "" ]; then
    DEBUG_JAVA=false
fi

if [ "$1" = "-debug" ]; then
    DEBUG_JAVA=true
fi

CWD=$(dirname $0)
if [ `basename $(pwd)` = 'scripts' ]; then
    cd ../
else
    cd `dirname $CWD`
fi

mkdir -p `pwd`/logs/core

GIT_SHA=`git rev-parse --short HEAD`

image="onoffswitch/francois:${GIT_SHA}_dev"

echo ${image}
docker run -it \
    -e HOST_IPADDR=`docker-machine ip $DOCKER_MACHINE_NAME` \
    -e JENKINS_URL=$JENKINS_URL \
    -e JENKINS_USER=francois \
    -e JENKINS_TOKEN=$JENKINS_TOKEN \
    -p 9090:9090 \
    -p 9099:9099 \
    -p 1044:1044 \
    -p 1898:1898 \
    -v `pwd`/logs/core:/data/logs \
    ${image} "$@"
