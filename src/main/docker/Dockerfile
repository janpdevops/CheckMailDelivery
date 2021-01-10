FROM openjdk:15-jdk-slim
COPY  target/checkMailDelivery-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/lib
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "/usr/local/lib/checkMailDelivery-1.0-SNAPSHOT-jar-with-dependencies.jar"]
CMD  []

#ENTRYPOINT ["ls"]
#CMD []