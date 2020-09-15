FOLDER=./tmp/webids
rm -r $FOLDER
mkdir $FOLDER
rapper -i turtle -o ntriples https://databus.dbpedia.org/system/api/accounts | cut -f1 -d " " | sed 's/<//;s/>//' > $FOLDER/webids
for i in `cat $FOLDER/webids` ; do 
   rapper -i turtle -o ntriples $i >> $FOLDER/webid.ttl
done
