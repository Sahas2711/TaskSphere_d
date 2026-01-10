FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/Gateway-*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV JWT_SECRET="your-production-secret-key-must-be-at-least-32-characters-long"
ENV JWT_ISSUER="tasksphere-api"
ENV REDIS_HOST="localhost"
ENV REDIS_PORT="6379"

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]