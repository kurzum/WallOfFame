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

1. Install and open Virtuoso DB: http://vos.openlinksw.com/owiki/wiki/VOS/VOSMake
2. Install necessary dependencies for WallOfFame: http://vos.openlinksw.com/owiki/wiki/VOS/VirtJenaProvider

    Ensure your active CLASSPATH includes full paths to all of the following files

        virt_jenaX.jar - Virtuoso Jena Provider for the required version
        virtjdbcX.jar - Virtuoso JDBC Driver for the required version
    

-----------   

TODO:

./push/webids/pom.xml
->  <databus.absoluteDCATDownloadUrlPath>
->  <databus.pkcs12File>
