FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Mengambil JAR dari folder target hasil build Maven/Lombok
COPY target/*.jar app.jar

CMD ["sh", "-c", "echo Waiting for dependencies... && sleep 120 && java -jar app.jar"]