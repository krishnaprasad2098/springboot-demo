# FROM openjdk:26-ea-trixie

# WORKDIR /app

# COPY target/sredemo-0.0.1-SNAPSHOT.war app.war

# ENTRYPOINT ["java","-jar","/app/app.war"]

FROM tomcat:9-jdk8

COPY target/sredemo-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/sredemo.war

ENTRYPOINT ["catalina.sh", "run"]
