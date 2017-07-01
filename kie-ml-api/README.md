Kie ML API
--
Allow machine learning pre-trained models to be used on top of [Kie  API](http://www.kiegroup.org/).

Once you have your kjar packaged with your models, can run it using:

~~~
Input input = new Input(FILE_TO_PREDICT);
Prediction prediction = KieMLContainer.newContainer(GAV).getService().predict("mnist", input);
System.out.println(prediction.getPredictions());
~~~

Make sure to have the correct API implementation in your classpath.

## Usage

It accepts a new maven project with the following structure:

~~~
kie-ml-test-models/
├── pom.xml
└── src
    └── main
        ├── java
        └── resources
            └── META-INF
                ├── kmodule.xml
                └── models-descriptor.xml

~~~

In models-description you should described where you find your model binaries and how to convert them. Make sure to use an existing implementation.

An example can be found in [kie-ml-test-models](../kie-ml-test-models) project. Once you finish your JAR you save it to your maven repository using `mvn clean install` and then you can use the Kie API to classify new data.


## Why

Kie API has good integration with maven and and using it makes easy to integrate with any other Kie API umbrella, like Kie Server.
