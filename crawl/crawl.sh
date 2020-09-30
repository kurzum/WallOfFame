FOLDER=./tmp
rm -r $FOLDER
mkdir $FOLDER
mkdir $FOLDER/webids
rapper -i turtle -o ntriples https://databus.dbpedia.org/system/api/accounts | cut -f1 -d " " | sed 's/<//;s/>//' > $FOLDER/webidURLs
k=1
for i in `cat $FOLDER/webidURLs` ; do 
   rapper -i turtle -o ntriples $i >> $FOLDER/webids/webid$k.ttl
   k=$((k+1))
done
