#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )

cd "$parent_path"
mkdir war

mvn package

find ./target -name \*.war -exec cp {} jar \;

cp -r ./shacl ./war/shacl
cp -r ./docker ./war/docker
cp -r ./crawl ./war/crawl
