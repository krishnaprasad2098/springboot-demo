FROM eclipse-temurin:21-jre-alpine-3.23
WORKDIR /app
COPY target/sredemo-0.0.1-SNAPSHOT.war app.war
ENTRYPOINT ["java","-jar","/app/app.war"]
