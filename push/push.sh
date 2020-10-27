#!/bin/bash

cd "$( dirname "${BASH_SOURCE[0]}" )"
cd ./webids



#prepare directories for calculated files
DATE=$(date +%y-%m-%d)
GROUPDIR=$(pwd)
VERSIONDIR=$(pwd)/uniformedWebids/$DATE
mkdir $VERSIONDIR
mvn versions:set -DnewVersion=$DATE

#run crawl and uniform process
cd "$( dirname "${BASH_SOURCE[0]}" )/../"
set -e
mvn scala:run -e -Dlauncher="walloffame" -DaddArgs="$VERSIONDIR/uniformedWebids_webids.ttl"

#push data to DBpedia Databus
cd $GROUPDIR
mvn prepare-package 
mvn databus:package-export
mvn databus:deploy
