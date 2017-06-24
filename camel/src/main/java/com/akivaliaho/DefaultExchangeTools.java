package com.akivaliaho;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;

import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by akivv on 18.5.2017.
 */
<<<<<<< Updated upstream:camel/src/main/java/com/akivaliaho/ExchangeTools.java
public class ExchangeTools {
=======
public class DefaultExchangeTools {

    private final ByteTools byteTools;
    private final ExchangePropertyPopulator exchangePropertyPopulator;

    public DefaultExchangeTools(ExchangePropertyPopulator exchangePropertyPopulator, ByteTools byteTools) {
        this.byteTools = byteTools;
        this.exchangePropertyPopulator = exchangePropertyPopulator;
    }

>>>>>>> Stashed changes:camel/src/main/java/com/akivaliaho/DefaultExchangeTools.java
    ObjectInputStream feedOutputStream(Exchange exchange) throws IOException {
        byte[] body = (byte[]) exchange.getIn().getBody();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ObjectInputStream(byteArrayInputStream);
    }

    void sendExchangeThroughTemplate(Exchange exchange, ServiceEvent serviceEvent, ServiceEventResult finalServiceEventResult, String routingKey) {
        //Send all the events through the producer template
        try {
            byte[] bytes = getBytes(serviceEvent, finalServiceEventResult);
            sendExchangeWithProducerTemplate(exchange, routingKey, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getBytes(ServiceEvent serviceEvent, ServiceEventResult finalServiceEventResult) throws IOException {
        //TODO Refactor this to a separate ByteTools utility!
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        if (finalServiceEventResult != null) {
            objectOutputStream.writeObject(finalServiceEventResult);
        } else {
            objectOutputStream.writeObject(serviceEvent);
        }
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

<<<<<<< Updated upstream:camel/src/main/java/com/akivaliaho/ExchangeTools.java
    private void sendExchangeWithProducerTemplate(Exchange exchange, String event, byte[] bytes) {
        setRoutingKeyProperties(exchange, event, bytes);
=======
    private void sendExchangeWithProducerTemplate(Exchange exchange, String routingKey, byte[] bytes) {
        //Set Default routing properties for the exchange message
        exchangePropertyPopulator.configureExchange(exchange, routingKey, bytes);
>>>>>>> Stashed changes:camel/src/main/java/com/akivaliaho/DefaultExchangeTools.java
        exchange.getContext().createProducerTemplate().send("direct:fromESB", exchange);
    }

    private void setRoutingKeyProperties(Exchange exchange, String event, byte[] bytes) {
        //If in is empty this is a default message, fill the in
        if (exchange.getIn() == null) {
            exchange.setIn(new DefaultMessage());
        }
        exchange.getIn().setBody(bytes);
        exchange.getIn().setHeader("routingKey", event);
        exchange.getIn().setHeader(RabbitMQConstants.EXCHANGE_NAME, exchange.getIn().getHeader("routingKey"));
        exchange.getIn().setHeader(RabbitMQConstants.ROUTING_KEY, "");
    }

    public void sendPollResult(HashMap<ServiceEvent, List<String>> eventInterestMap, Exchange exchange, String routingKey) {
        ServiceEventResult serviceEvent = new ConfigurationPollEventResult(eventInterestMap);
        sendExchangeThroughTemplate(exchange, null, serviceEvent, routingKey);

    }

    public void sendToInterestedParties(List<String> interestedParties, ServiceEvent serviceEvent, ServiceEventResult serviceEventResult, Exchange exchange) {
        //TODO These senders should be part of another class
        sendToParties(exchange, interestedParties, serviceEvent, serviceEventResult);
    }

    private void sendToParties(Exchange exchange, List<String> interestedParties, ServiceEvent serviceEvent, ServiceEventResult serviceEventResult) {
        if (interestedParties != null) {
            ServiceEventResult finalServiceEventResult = serviceEventResult;
            interestedParties
                    .forEach(event -> {
                        sendExchangeThroughTemplate(exchange, serviceEvent, finalServiceEventResult, event);
                    });
        }
    }


    public void requestInterestedParties(String configHolderRoutingKey, CamelContext context) throws IOException {
        //Send body
        sendExchangeThroughTemplate(new DefaultExchange(context), new RequestInterestedPartiesEvent(), null, configHolderRoutingKey);
    }
}
