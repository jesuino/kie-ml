# kie-ml
Run Machine Learning Models using Kie API

## Building Everything

You must have Java 8 and latest maven version, then:

~~~
$ mvn clean install
~~~

It will take a while since it will download more then 400 dependencies, but after the first run all the other builds should be faster.


## Running

Our distribution uses a Wildfly Swarm to run our extension on top of Kie Server (based on [Maciej's example](http://mswiderski.blogspot.com.br/2016/03/are-you-ready-to-dive-into-wildfly-swarm.html) ).:
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

### Creating your first container

We prepared a test container that contains a few models. Go to kie-ml-test-models and run `mvn clean install` then you should be able to create a container using the following cURL command:
~~~
curl -X PUT -H 'Content-type: application/xml' -u 'kieserver:kieserver1!' --data '<kie-container container-id="test"><release-id><artifact-id>kie-ml-test-models</artifact-id><group-id>org.fxapps.ml</group-id><version>0.0.1-SNAPSHOT</version></release-id></kie-container>' http://localhost:8080/rest/server/containers/test
~~~

Once it is created, retrieve the container information using `curl -u 'kieserver:kieserver1!' http://localhost:8080/rest/server/containers/test`. The response should be:

~~~
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<response type="SUCCESS" msg="Info for container test">
    <kie-container container-id="test" status="STARTED">
        <messages>
            <content>Container test successfully created with module org.fxapps.ml:kie-ml-test-models:0.0.1-SNAPSHOT.</content>
            <severity>INFO</severity>
            <timestamp>2017-07-03T12:11:36.250-03:00</timestamp>
        </messages>
        <release-id>
            <artifact-id>kie-ml-test-models</artifact-id>
            <group-id>org.fxapps.ml</group-id>
            <version>0.0.1-SNAPSHOT</version>
        </release-id>
        <resolved-release-id>
            <artifact-id>kie-ml-test-models</artifact-id>
            <group-id>org.fxapps.ml</group-id>
            <version>0.0.1-SNAPSHOT</version>
        </resolved-release-id>
        <scanner status="DISPOSED"/>
    </kie-container>
</response>
~~~

Now that the container is created we can see all the models installed on it. At the time of this writing, the test container only had the model for mnist images (but it will change soon), see the response for `curl -u 'kieserver:kieserver1!'  http://localhost:8080/rest/server/containers/test/kieml/mnist`:
~~~
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<response type="SUCCESS" msg="Found model">
    <task-reassignment-list xsi:type="model" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
        <id>mnist</id>
        <labels>0</labels>
        <labels>1</labels>
        <labels>2</labels>
        <labels>3</labels>
        <labels>4</labels>
        <labels>5</labels>
        <labels>6</labels>
        <labels>7</labels>
        <labels>8</labels>
        <labels>9</labels>
        <modelBinPath>models/minist-model.zip</modelBinPath>
        <modelLabelsPath>labels/mnist_labels.txt</modelLabelsPath>
        <name>MNIST digit recognition</name>
        <provider>deeplearning4j</provider>
        <transformDescriptor>
            <name>ImageTransformer</name>
            <param>
                <name>height</name>
                <value>28</value>
            </param>
            <param>
                <name>width</name>
                <value>28</value>
            </param>
            <param>
                <name>channels</name>
                <value>1</value>
            </param>
        </transformDescriptor>
    </task-reassignment-list>
</response>
~~~

### Running your model

We have a container running on Kie Server, but now it is time to make an actual prediction. Let's try our mnist trained model. We have mnist on the WEB, but our model was trained against minist with black background, hence on this github we can find a few test images. Here's the request we should make:

~~~
curl -X POST -u 'kieserver:kieserver1!' -H 'Content-type: application/xml' --data '<input><url>https://raw.githubusercontent.com/gskielian/JPG-PNG-to-MNIST-NN-Format/master/training-images/1/im10007.png</url></input>' http://localhost:8080/rest/server/containers/test/kieml/mnist
~~~

And this should return the following XML:

~~~
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<response type="SUCCESS" msg="Success Running prediction">
    <query-definitions xsi:type="prediction" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
        <predictions>
            <entry>
                <key>0</key>
                <value xsi:type="xs:double" xmlns:xs="http://www.w3.org/2001/XMLSchema">0.0</value>
            </entry>
            <entry>
                <key>1</key>
                <value xsi:type="xs:double" xmlns:xs="http://www.w3.org/2001/XMLSchema">1.0</value>
            </entry>
            <entry>
                <key>2</key>
                <value xsi:type="xs:double" xmlns:xs="http://www.w3.org/2001/XMLSchema">0.0</value>
            </entry>
            <entry>
                <key>3</key>
                <value xsi:type="xs:double" xmlns:xs="http://www.w3.org/2001/XMLSchema">0.0</value>
            </entry>
            <entry>
                <key>4</key>
                <value xsi:type="xs:double" xmlns:xs="http://www.w3.org/2001/XMLSchema">0.0</value>
            </entry>
            <entry>
                <key>5</key>
                <value xsi:type="xs:double" xmlns:xs="http://www.w3.org/2001/XMLSchema">0.0</value>
            </entry>
            <entry>
                <key>6</key>
                <value xsi:type="xs:double" xmlns:xs="http://www.w3.org/2001/XMLSchema">0.0</value>
            </entry>
            <entry>
                <key>7</key>
                <value xsi:type="xs:double" xmlns:xs="http://www.w3.org/2001/XMLSchema">0.0</value>
            </entry>
            <entry>
                <key>8</key>
                <value xsi:type="xs:double" xmlns:xs="http://www.w3.org/2001/XMLSchema">0.0</value>
            </entry>
            <entry>
                <key>9</key>
                <value xsi:type="xs:double" xmlns:xs="http://www.w3.org/2001/XMLSchema">0.0</value>
            </entry>
        </predictions>
        <predictionsResult>[0.00, 1.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00]</predictionsResult>
    </query-definitions>
</response>
~~~

As you can see on *predictionsResult* it classified the image as being an 1.
