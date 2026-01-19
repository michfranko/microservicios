FROM eclipse-temurin:21
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y maven && mvn clean package -DskipTests
EXPOSE 8083
CMD ["java", "-jar", "target/quarkus-app/quarkus-run.jar"]
