#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )

cd "$parent_path"

FOLDER=$parent_path/tmp
rm -r -f $FOLDER
mkdir $FOLDER
mkdir $FOLDER/webids
rapper -i turtle -o ntriples https://databus.dbpedia.org/system/api/accounts | cut -f1 -d " " | sed 's/<//;s/>//' > $FOLDER/webidURLs
k=1
for i in `cat $FOLDER/webidURLs` ; do 
   rapper -i turtle -o ntriples $i >> $FOLDER/webids/webid$k.ttl
   k=$((k+1))
done

echo $FOLDER/webids 
