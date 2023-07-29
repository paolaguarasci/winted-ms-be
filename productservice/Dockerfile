FROM openjdk:20-jdk-slim
COPY target/productservice-0.0.1-SNAPSHOT.jar product-service.jar
COPY store/keystore.jks store/keystore.jks
COPY store/truststore.jks store/truststore.jks
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/product-service.jar", "-Djavax.net.ssl.trustStore=/store/truststore.jks", "-Djavax.net.ssl.trustStorePassword=winted"]
