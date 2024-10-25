FROM openjdk:24-jdk-slim

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apt-get update && apt-get install -y maven curl
RUN mvn clean package -DskipTests

COPY target/geolocalizacion-0.0.1-SNAPSHOT.jar geolocalizacion.jar

CMD ["java", "-jar", "geolocalizacion.jar"]