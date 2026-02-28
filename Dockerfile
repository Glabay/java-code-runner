FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B -q -e -DskipTests dependency:go-offline

COPY src ./src

RUN mvn -B -DskipTests clean package

# --- Runtime image ---
FROM eclipse-temurin:25-jre-alpine
LABEL app.name="midnight-code-runner"
LABEL app.version="1.0"
WORKDIR /app

# Create non-root user and group
RUN addgroup -S appgroup \
    && adduser \
    -S midnightApp \
    -G appgroup

COPY --from=build /app/target/*.jar app.jar
# Switch to non-root user
USER midnightApp

ENTRYPOINT ["java",

 "-Xms64m",
 "-Xmx256m",
 "-XX:+ExitOnOutOfMemoryError",
 "-XX:+UseCompactObjectHeaders",

 "-jar",
 "app.jar"
 ]
