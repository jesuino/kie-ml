# kie-ml
Run Machine Learning Models using Kie API

## Building Everything

You must have Java 8 and latest maven version, then:

~~~
$ mvn clean install
~~~

It will take a while since it will download more then 400 dependencies, but after the first run all the other builds should be faster.


## Running

~~~
$ cd kie-ml-dist
$ mvn clean install | java -Dorg.kie.server.id=swarm-kie-server -Dorg.kie.server.location=http://localhost:8080/server -jar target/kie-ml-dist-0.0.1-SNAPSHOT-swarm.jar
~~~
