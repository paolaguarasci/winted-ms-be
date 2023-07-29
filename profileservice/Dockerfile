FROM openjdk:20-jdk-slim
COPY target/profileservice-0.0.1-SNAPSHOT.jar profile-service.jar
COPY store/keystore.jks store/keystore.jks
COPY store/truststore.jks store/truststore.jks
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080
ENTRYPOINT ["java","-jar","/profile-service.jar", "-Djavax.net.ssl.trustStore=/store/truststore.jks", "-Djavax.net.ssl.trustStorePassword=winted"]
