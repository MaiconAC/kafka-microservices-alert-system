# Imagem do jdk para usar no container
FROM openjdk:17-jdk-slim

COPY target/sensordatapublisher-0.0.1-SNAPSHOT.jar sensordatapublisher-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "sensordatapublisher-0.0.1-SNAPSHOT.jar"]
