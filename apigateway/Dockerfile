FROM openjdk:20-jdk-slim
COPY target/apigateway-0.0.1-SNAPSHOT.jar gateway-service.jar
COPY store/keystore.jks store/keystore.jks
COPY store/truststore.jks store/truststore.jks
EXPOSE 8443
ENTRYPOINT ["java","-jar","/gateway-service.jar", "-Djavax.net.ssl.trustStore=/store/truststore.jks", "-Djavax.net.ssl.trustStorePassword=winted"]
