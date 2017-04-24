FROM openjdk:jre

WORKDIR /app
ADD ./target/*.jar /app/gateway.jar

CMD  ["java","-Dspring.profiles.active=prod", "-jar", "/app/gateway.jar"]
