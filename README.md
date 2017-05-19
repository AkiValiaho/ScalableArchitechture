# ScalableArchitechture
Opinionated Event-Driven architechture using RabbitMQ and Mule ESB



Uses RabbitMQ and Apache Camel for Event-Driven Communication between gateway and microservices.

Swagger-ui configured at http://localhost:8080/swagger-ui.html
with username: test and password: test

Service-methods annotated with @Interest-annotation will be picked up
and synced with an event the method emits and an optional event that the method listens to.
