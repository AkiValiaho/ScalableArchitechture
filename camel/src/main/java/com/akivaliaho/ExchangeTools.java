package com.akivaliaho;

import org.apache.camel.Exchange;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;

import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by akivv on 18.5.2017.
 */
public class ExchangeTools {
    ObjectInputStream feedOutputStream(Exchange exchange) throws IOException {
        byte[] body = (byte[]) exchange.getIn().getBody();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ObjectInputStream(byteArrayInputStream);
    }

    void sendExchangeThroughTemplate(Exchange exchange, ServiceEvent serviceEvent, ServiceEventResult finalServiceEventResult, String event) {
        //Send all the events through the producer template
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            if (finalServiceEventResult != null) {
                objectOutputStream.writeObject(finalServiceEventResult);
            } else {
                objectOutputStream.writeObject(serviceEvent);
            }
            objectOutputStream.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            exchange.getIn().setBody(bytes);
            exchange.getIn().setHeader("routingKey", event);
            exchange.getIn().setHeader(RabbitMQConstants.EXCHANGE_NAME, exchange.getIn().getHeader("routingKey"));
            exchange.getIn().setHeader(RabbitMQConstants.ROUTING_KEY, "");
            exchange.getContext().createProducerTemplate().send("direct:fromESB", exchange);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPollResult(HashMap<ServiceEvent, List<String>> eventInterestMap, Exchange exchange, String event) {
        ServiceEventResult serviceEvent = new ConfigurationPollEventResult(eventInterestMap);
        sendExchangeThroughTemplate(exchange, null, serviceEvent, event);

    }

    public void sendToInterestedParties(List<String> interestedParties, ServiceEventResult serviceEventResult, Exchange exchange, ServiceEvent serviceEvent) {
        if (interestedParties != null) {
            ServiceEventResult finalServiceEventResult = serviceEventResult;
            interestedParties
                    .forEach(event -> {
                        sendExchangeThroughTemplate(exchange, serviceEvent, finalServiceEventResult, event);
                    });
        }
    }
}
