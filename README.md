Prerequisites: 
-----------

1. Install Docker and Docker-Compose
2. Install necessary dependencies for WallOfFame: 

   1. Download and unzip "virt_jena3.jar" and "virtjdbc4.jar" from: http://vos.openlinksw.com/owiki/wiki/VOS/VOSDownload#Jena%20Provider
   2. Install them in your local maven repository:
 
            mvn install:install-file -q  -Dfile={pathToJar}/virt_jena3.jar  -DgroupId=com.openlink.virtuoso  -DartifactId=virt_jena3  -Dversion=3.0  -Dpackaging=jar  -DgeneratePom=true
            mvn install:install-file -q  -Dfile={pathToJar}/virtjdbc4.jar  -DgroupId=com.openlink.virtuoso  -DartifactId=virtjdbc4  -Dversion=4.0  -Dpackaging=jar  -DgeneratePom=true
    
--------------------------
    
    
Building and Running
--------------------

To prepare application for server (package), execute:

    ./prepare4upload.sh

The webapp can be run inside Tomcat using :

    docker-compose -f docker/docker-compose.yml up
    mvn spring-boot:run
    
Now you can either browse through the application, starting at:
    
    localhost:8080/         ,or
    localhost:8080/validate

Crawl and uniform all registered WebIds with:
    
    curl localhost:8080/getWebIds > webids.ttl