package com.akivaliaho.services.application;

import com.akivaliaho.ESBDynamicRouter;
import org.apache.camel.builder.RouteBuilder;

import java.io.IOException;

/**
 * A Camel Java DSL Router
 */
public class FromMQRouteBuilder extends RouteBuilder {


    private final ExchangeService exchangeService;

    public FromMQRouteBuilder(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {
        //Set the new context
        from("rabbitmq://localhost:5672/toBrokerExchange?routingKey=master&queue=toBrokerQueue&username=hello&password=world&exchangeType=topic&autoDelete=false")
                .log("Got message from service: ${in.header.serviceName}")
                .process(exchangeService);
        //TODO Check if this works properly
        from("direct:fromESB")
                .log("Attempting to send to exchange with routing key: ${in.headers.routingKey}")
                .dynamicRouter(method(ESBDynamicRouter.class, "routeExchange"))
                .onException(IOException.class)
                .maximumRedeliveries(2);
    }


}
