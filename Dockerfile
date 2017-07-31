
FROM docker.io/jboss/base-jdk:8

COPY kie-ml-dist/target/kie-ml-dist-0.0.1-SNAPSHOT-swarm.jar /tmp
COPY kie-ml-test-models/target/kie-ml-test-models-0.0.1-SNAPSHOT.jar /tmp
COPY pom.xml /tmp

USER root

RUN mkdir -p /opt/jboss/src/main/config && \
	mkdir -p /opt/jboss/src/main/webapp && \
	cp /tmp/kie-ml-dist-0.0.1-SNAPSHOT-swarm.jar /opt/jboss/kie-ml.jar && \
	mv /tmp/pom.xml /tmp/kie-ml-parent-0.0.1-SNAPSHOT.pom && \
	chown 1001:100 -R /opt && \
	chmod g+rw -R /opt && \
	yum install -y maven && \
	yum clean all

COPY kie-ml-dist/src/main/config/ /opt/jboss/src/main/config/
COPY kie-ml-dist/src/main/webapp/ /opt/jboss/src/main/webapp/

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
							 -Dfile=/tmp/kie-ml-test-models-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "-Dorg.kie.server.id=swarm-kie-server", "-Dorg.kie.server.location=http://localhost:8080/rest", "/opt/jboss/kie-ml.jar"]