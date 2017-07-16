
FROM docker.io/jboss/base-jdk:8

COPY kie-ml-dist/target/kie-ml-dist-0.0.1-SNAPSHOT-swarm.jar /tmp
COPY kie-ml-test-models/target/kie-ml-test-models-0.0.1-SNAPSHOT.jar /tmp
COPY pom.xml /tmp
COPY ./settings.xml /tmp

USER root

RUN mkdir -p /opt/kie-ml && \
	mkdir -p /opt/jboss/src/main/config && \
	cp /tmp/kie-ml-dist-0.0.1-SNAPSHOT-swarm.jar /opt/kie-ml/kie-ml.jar && \
	mv /tmp/pom.xml /tmp/kie-ml-parent-0.0.1-SNAPSHOT.pom && \
	chown 1001:100 -R /opt && \
	chmod g+rw -R /opt && \
	yum install -y maven && \
	yum clean all

COPY kie-ml-dist/src/main/config/* /opt/jboss/src/main/config/

USER 1001

RUN mvn install:install-file -DartifactId=kie-ml-parent \
							 -DgroupId=org.fxapps.ml \
							 -Dversion=0.0.1-SNAPSHOT \
							 -Dpackaging=pom \
							 -Dfile=/tmp/kie-ml-parent-0.0.1-SNAPSHOT.pom && \
	mvn install:install-file -DartifactId=kie-ml-test-models \
							 -DgroupId=org.fxapps.ml \
							 -Dversion=0.0.1-SNAPSHOT \
							 -Dpackaging=jar \
							 -Dfile=/tmp/kie-ml-test-models-0.0.1-SNAPSHOT.jar && \
	cp /tmp/settings.xml /opt/jboss/\?

EXPOSE 8080

CMD ["java", "-jar", "/opt/kie-ml/kie-ml.jar"]