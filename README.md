Building and Running
--------------------

Assuming you already have Maven installed, the webapp can be built by running:

    mvn clean install

The webapp can be run inside Jetty using the Maven plugin:

    mvn jetty:run

Crawl and uniform all registered WebIds with:
    
    curl localhost:9090/webids.html > webids.ttl

Prerequisites: 
-----------

1. Install and start Virtuoso DB: http://vos.openlinksw.com/owiki/wiki/VOS/VOSMake
2. Install necessary dependencies for WallOfFame: 

   1. Download and unzip "virt_jena3.jar" and "virtjdbc4.jar" from: http://vos.openlinksw.com/owiki/wiki/VOS/VOSDownload#Jena%20Provider
   2. Install them in your local maven repository:
 
            mvn install:install-file -q  -Dfile={pathToJar}/virt_jena3.jar  -DgroupId=com.openlink.virtuoso  -DartifactId=virt_jena3  -Dversion=3.0  -Dpackaging=jar  -DgeneratePom=true
            mvn install:install-file -q  -Dfile={pathToJar}/virtjdbc4.jar  -DgroupId=com.openlink.virtuoso  -DartifactId=virtjdbc4  -Dversion=4.0  -Dpackaging=jar  -DgeneratePom=true
    
   
    

-----------   

TODO:

./push/webids/pom.xml
->  <databus.absoluteDCATDownloadUrlPath>
->  <databus.pkcs12File>
