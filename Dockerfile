# ---------- Etapa de build ----------
FROM maven:3.9.11-eclipse-temurin-24 AS build
WORKDIR /app

COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests package

# ---------- Etapa runtime ----------
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
ENV TZ=UTC

# JVM flags seguros en contenedor
ENV JAVA_OPTS="-XX:+HeapDumpOnOutOfMemoryError -XX:MaxRAMPercentage=75.0"

COPY --from=build /app/target/reactive-tasks-0.0.1.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["/bin/sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
