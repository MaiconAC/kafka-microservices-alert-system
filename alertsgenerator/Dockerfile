# Imagem do jdk para usar no container
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/alertsgenerator-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

