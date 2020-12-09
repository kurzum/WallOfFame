#!/bin/bash
SCRIPT=$(readlink -f "$0")
# Absolute path this script is in, thus /home/user/bin
SCRIPTPATH="$(dirname "$SCRIPT")"

cd "$SCRIPTPATH"

FOLDER="$SCRIPTPATH/tmp"
rm -r -f "$FOLDER"
mkdir "$FOLDER"
mkdir "$FOLDER/webids"

#download and unpack raptor
curl http://download.librdf.org/source/raptor2-2.0.15.tar.gz | tar -xvzf -

#get dependencies
apt install libtool
apt install autoconf
apt install automake 
apt install gtk-doc-tools
apt install flex
apt install bison

#install rapper
cd raptor2-2.0.15
./autogen.sh
./configure
make
make install

rapper -i turtle -o ntriples https://databus.dbpedia.org/system/api/accounts \
    | cut -f3,1 -d " " \
    | tr -d \> \
    | tr -d \< \
    | sed 's/https:\/\/databus.dbpedia.org\///g' \
    | sed 's/ /\,/g' \
    > "$FOLDER/webidURLs.csv"


input="$FOLDER/webidURLs.csv"
while IFS= read -r line
do
   URL=$(echo $line | cut -f1 -d",")
   NAME=$(echo $line | cut -f2 -d",")
   rapper -i turtle -o ntriples $URL >> "$FOLDER/webids/$NAME.ttl"
done < "$input"

echo "$FOLDER/webids" 
# k=1
# for i in `cat "$FOLDER/webidURLs"` ; do 
#     echo $i
#    URL=$i cut -f1 -d " "
#    echo $URL
#    NAME="$i cut -f2 -d " ""
#    rapper -i turtle -o ntriples $i >> "$FOLDER/webids/webid$k.ttl"
#    k=$((k+1))
# done
# 
# echo "$FOLDER/webids" 
