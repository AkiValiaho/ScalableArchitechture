package com.akivaliaho;

import com.akivaliaho.event.EventInterestHolder;
import org.apache.camel.builder.RouteBuilder;

import java.io.IOException;

/**
 * A Camel Java DSL Router
 */
public class FromMQRouteBuilder extends RouteBuilder {

    private ExchangeToServiceEvent exchangeToServiceEvent;

    public FromMQRouteBuilder() {
        EventInterestHolder eventInterestHolder = new EventInterestHolder();
        this.exchangeToServiceEvent = new ExchangeToServiceEvent(eventInterestHolder);
    }

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {
        from("rabbitmq://localhost:5672/toBrokerExchange?routingKey=master&queue=toBrokerQueue&username=hello&password=world&exchangeType=topic&autoDelete=false")
                .process(exchangeToServiceEvent);
        //TODO Check if this works properly
        from("direct:fromESB")
                .log("Attempting to send to exchange with routing key: ${in.headers.routingKey}")
                .dynamicRouter(method(ESBDynamicRouter.class, "routeExchange"))
                .onException(IOException.class)
                .maximumRedeliveries(2);
    }


}
