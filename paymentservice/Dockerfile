FROM openjdk:20-jdk-slim
COPY target/paymentservice-0.0.1-SNAPSHOT.jar payment-service.jar
COPY store/keystore.jks store/keystore.jks
COPY store/truststore.jks store/truststore.jks
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080
ENTRYPOINT ["java","-jar","/payment-service.jar", "-Djavax.net.ssl.trustStore=/store/truststore.jks", "-Djavax.net.ssl.trustStorePassword=winted"]
