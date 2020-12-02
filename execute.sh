#!/bin/bash

mvn package
cp target/wof.war docker/wof/wof.war

docker-compose -f docker/docker-compose.yml up
