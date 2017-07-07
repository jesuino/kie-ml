
FROM docker.io/jboss/base-jdk:8

COPY kie-ml-dist/target/kie-ml-dist-0.0.1-SNAPSHOT-swarm.jar /tmp

USER root

RUN mkdir -p /opt/kie-ml && \
	mkdir -p /opt/jboss/src/main/config && \
	cp /tmp/kie-ml-dist-0.0.1-SNAPSHOT-swarm.jar /opt/kie-ml/kie-ml.jar && \
	chown 1001:100 -R /opt && \
	chmod g+rw -R /opt && \
	rm -rf /tmp/*

COPY kie-ml-dist/src/main/config/* /opt/jboss/src/main/config/

USER 1001

EXPOSE 8080

CMD ["java", "-jar", "/opt/kie-ml/kie-ml.jar"]
