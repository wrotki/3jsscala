# Polyverse 3d visualization

## Get started

* brew install sbt   
* sbt
* fastOptJS
* project datasource
* run
* open browser at: http://localhost:12345/target/scala-2.11/classes/index-dev.html
* start Polyverse 
    
## Dockerized version

* docker run -it --rm -v /var/run/docker.sock:/var/run/docker.sock -p 8080:12345 polyverse/visualizer
* open browser at: http://localhost:8080/target/scala-2.11/classes/index-dev.html
