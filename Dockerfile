FROM debian:buster-slim

RUN apt-get update
RUN apt-get install -y openjdk-11-jre

ADD output/robot-worlds-server-0.3.0.jar /srv/robot-worlds-server-0.3.0.jar

WORKDIR /srv
EXPOSE 5050
CMD ["java", "-jar", "robot-worlds-server-0.3.0.jar"]
