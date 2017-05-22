package com.akivaliaho;

import com.akivaliaho.event.EventAndRoutingKeyHolder;
import com.akivaliaho.event.EventInterestRegistrer;
import org.apache.camel.builder.RouteBuilder;

import java.io.IOException;

/**
 * A Camel Java DSL Router
 */
public class FromMQRouteBuilder extends RouteBuilder {

    private ExchangeToServiceEvent exchangeToServiceEvent;

    public FromMQRouteBuilder() {
        EventAndRoutingKeyHolder eventAndRoutingKeyHolder = new EventAndRoutingKeyHolder();
        EventInterestRegistrer eventInterestRegistrer = new EventInterestRegistrer(eventAndRoutingKeyHolder);
        ExchangeTools exchangeTools = new ExchangeTools();
        ProcessPreparator processPreparator = new ProcessPreparator(eventInterestRegistrer, exchangeTools);
        this.exchangeToServiceEvent = new ExchangeToServiceEvent(eventInterestRegistrer, exchangeTools, processPreparator);
    }

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {
        from("rabbitmq://localhost:5672/toBrokerExchange?routingKey=master&queue=toBrokerQueue&username=hello&password=world&exchangeType=topic&autoDelete=false")
                .log("Got message from service: ${in.header.serviceName}")
                .process(exchangeToServiceEvent);
        //TODO Check if this works properly
        from("direct:fromESB")
                .log("Attempting to send to exchange with routing key: ${in.headers.routingKey}")
                .dynamicRouter(method(ESBDynamicRouter.class, "routeExchange"))
                .onException(IOException.class)
                .maximumRedeliveries(2);
    }


}
