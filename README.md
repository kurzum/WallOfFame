Prerequisites: 
-----------
1. Install Maven 3.3.9
2. Install Docker and Docker-Compose    
--------------------------
    
    
Building and Running
--------------------

The webapp can be run inside Tomcat using :

    ./execute.sh
      
Now you can either browse through the application, starting at:
    
    localhost:8080/         ,or
    localhost:8080/validate

Crawl and uniform all registered WebIds with:
    
    curl localhost:8080/getWebIds > webids.ttl
    
