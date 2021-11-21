FROM debian:buster-slim

RUN apt-get update
RUN apt-get install -y openjdk-11-jre

ADD output/api-server.jar /srv/api-server.jar
ADD uss_victory_db.sqlite /srv/uss_victory_db.sqlite

WORKDIR /srv
EXPOSE 5000
CMD ["java", "-jar", "api-server.jar"]