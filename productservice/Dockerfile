#FROM maven:3-amazoncorretto-20 AS build
#COPY src /home/app/src
#COPY pom.xml /home/app
#ENV SPRING_PROFILES_ACTIVE=docker
#RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:20-jdk-slim
#COPY --from=build /home/app/target/productservice-0.0.1-SNAPSHOT.jar product-service.jar
COPY target/productservice-0.0.1-SNAPSHOT.jar product-service.jar
COPY store/keystore.jks store/keystore.jks
COPY store/truststore.jks store/truststore.jks
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/product-service.jar", "-Djavax.net.ssl.trustStore=/store/truststore.jks", "-Djavax.net.ssl.trustStorePassword=winted"]
