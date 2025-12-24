# FROM openjdk:26-ea-trixie

# WORKDIR /app

# COPY target/sredemo-0.0.1-SNAPSHOT.war app.war

# ENTRYPOINT ["java","-jar","/app/app.war"]


FROM eclipse-temurin:8u472-b08-jre-alpine-3.23

# Create a non-privileged user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app

# Copy the artifact and change ownership immediately
COPY --chown=appuser:appuser target/sredemo-0.0.1-SNAPSHOT.war app.war

# Switch to the non-root user
USER appuser

# Use 'exec' form for better signal handling (SIGTERM)
ENTRYPOINT ["java", "-jar", "app.war"]
