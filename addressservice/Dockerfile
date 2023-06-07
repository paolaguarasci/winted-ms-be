FROM openjdk:20-jdk-slim
COPY target/addressservice-0.0.1-SNAPSHOT.jar address-service.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/address-service.jar"]
