#use auth image jdk to run application
FROM eclipse-temurin:17-jdk-alpine

# set working file inside container
WORKDIR /app

# copy file jar that build from target into container
COPY target/learn-spring-framework-0.0.1-SNAPSHOT.jar app.jar
#run mvn clean package to have this file

# open 8080 port
EXPOSE 8080

# code for run application
ENTRYPOINT ["java", "-jar", "app.jar"]