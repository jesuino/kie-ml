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

Once the server is running, go to the following URL: `http://localhost:8080/rest/server`, provide the credentials user *kieserver* and password *kieserver1!* and you should see the following XML:

~~~
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<response type="SUCCESS" msg="Kie Server info">
    <kie-server-info>
        <capabilities>KieServer</capabilities>
        <capabilities>KieMLCapability</capabilities>
        <location>http://localhost:8080/server</location>
        <messages>
            <content>Server KieServerInfo{serverId='swarm-kie-server', version='7.0.0.Final', location='http://localhost:8080/server'}started successfully at Mon Jul 03 11:22:16 BRT 2017</content>
            <severity>INFO</severity>
            <timestamp>2017-07-03T11:22:16.776-03:00</timestamp>
        </messages>
        <name>swarm-kie-server</name>
        <id>swarm-kie-server</id>
        <version>7.0.0.Final</version>
    </kie-server-info>
</response>
~~~
