FROM openjdk:20-jdk-slim
COPY target/messageservice-0.0.1-SNAPSHOT.jar message-service.jar
COPY store/keystore.jks store/keystore.jks
COPY store/truststore.jks store/truststore.jks
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 7004
ENTRYPOINT ["java","-jar","/message-service.jar", "-Djavax.net.ssl.trustStore=/store/truststore.jks", "-Djavax.net.ssl.trustStorePassword=winted"]
