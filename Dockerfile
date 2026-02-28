FROM eclipse-temurin:25-jdk-alpine
LABEL app.name="midnight-code-runner"
LABEL app.version="1.0"
WORKDIR /app

# Create non-root user and group
RUN addgroup -S appgroup \
    && adduser \
    -S midnightApp \
    -G appgroup

COPY target/*.jar app.jar
USER midnightApp

ENTRYPOINT ["java", "-Xms64m", "-Xmx256m", "-XX:+ExitOnOutOfMemoryError", "-XX:+UseCompactObjectHeaders", "-jar", "app.jar" ]
