Building and Running
--------------------

Assuming you already have Maven installed, the webapp can be built by running:

    mvn clean install

The webapp can be run inside Jetty using the Maven plugin:

    mvn jetty:run

Crawl and uniform all registered WebIds with:
    
    curl localhost:9090/webids.html > webids.ttl

    
TODO:

./push/webids/pom.xml
->  <databus.absoluteDCATDownloadUrlPath>
->  <databus.pkcs12File>
