FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy jar
COPY target/*.jar app.jar

# Render injects PORT
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
