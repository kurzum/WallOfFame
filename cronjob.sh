#!/bin/bash 

export PATH=$PATH:/opt/maven3.3.9/bin:/usr/bin
export M2_HOME=/opt/maven3.3.9
export M2=/opt/maven3.3.9/bin
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64

cd "$( dirname "${BASH_SOURCE[0]}" )/"
set -e
mvn exec:exec -Ppushdata
