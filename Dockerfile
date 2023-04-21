FROM maven:3.6.3-jdk-14

ADD . /usr/src/cf/backend
WORKDIR /usr/src/cf/backend
EXPOSE 4567
ENTRYPOINT ["mvn", "clean", "verify", "exec:java"]