# ScalableArchitechture
Opinionated Event-Driven architechture using RabbitMQ and Mule ESB



Uses RabbitMQ and Apache Camel for Event-Driven Communication between gateway and microservices.

Swagger-ui configured at http://localhost:8080/swagger-ui.html
with username: test and password: test

Please start RabbitMQ instance from the provided docker image prior to development:
From the project root: docker-compose up rabbitmq

RabbitMQ admin panel available from url http://localhost:8056

Run integration tests with mvn clean install -DskipITs=false from the project root. 

Service-methods annotated with @Interest-annotation will be picked up
and synced with an event the method emits and an optional event that the method listens to.
