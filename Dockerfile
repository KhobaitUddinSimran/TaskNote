FROM gradle:7.6.3-jdk17 AS build

WORKDIR /app
COPY . /app/

RUN gradle fatJar --no-daemon

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/build/libs/*-all.jar /app/application.jar

# Expose the port your app runs on
EXPOSE 8080

# Environment variables if needed
ENV PORT=8080

# Run the application
CMD ["java", "-jar", "/app/application.jar"]