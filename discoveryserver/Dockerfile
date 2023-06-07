FROM openjdk:20-jdk-slim
COPY target/discoveryserver-0.0.1-SNAPSHOT.jar discovery-service.jar
COPY store/keystore.jks store/keystore.jks
COPY store/truststore.jks store/truststore.jks
ENTRYPOINT ["java","-jar","/discovery-service.jar", "-Djavax.net.ssl.trustStore=/store/truststore.jks", "-Djavax.net.ssl.trustStorePassword=winted"]
