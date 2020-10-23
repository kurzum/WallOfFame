#!/bin/bash


cd ./webids
GROUPDIR=$(pwd)
VERSIONDIR=$(pwd)/uniformedWebids/$DATE

#prepare directories for calculated files
DATE=$(date +%y-%m-%d)
mvn versions:set -DnewVersion=$DATE
mkdir $VERSIONDIR

#run crawl and uniform process
set -e
cd "$( dirname "${BASH_SOURCE[0]}" )/../../"
mvn scala:run -e -Dlauncher="walloffame" -DaddArgs="$VERSIONDIR/uniformedWebids_webids.ttl"

#push data to DBpedia Databus
cd $GROUPDIR
mvn prepare-package 
mvn databus:package-export
mvn databus:deploy
