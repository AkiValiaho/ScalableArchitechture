package com.akivaliaho;

import org.apache.camel.Exchange;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;

import java.io.*;
import java.util.HashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

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
            byte[] bytes = getBytes(serviceEvent, finalServiceEventResult);
            sendExchangeWithProducerTemplate(exchange, event, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getBytes(ServiceEvent serviceEvent, ServiceEventResult finalServiceEventResult) throws IOException {
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

    private void sendExchangeWithProducerTemplate(Exchange exchange, String event, byte[] bytes) {
        exchange.getIn().setBody(bytes);
        exchange.getIn().setHeader("routingKey", event);
        exchange.getIn().setHeader(RabbitMQConstants.EXCHANGE_NAME, exchange.getIn().getHeader("routingKey"));
        exchange.getIn().setHeader(RabbitMQConstants.ROUTING_KEY, "");
        exchange.getContext().createProducerTemplate().send("direct:fromESB", exchange);
    }

    public void sendPollResult(HashMap<ServiceEvent, List<String>> eventInterestMap, Exchange exchange, String event) {
        ServiceEventResult serviceEvent = new ConfigurationPollEventResult(eventInterestMap);
        sendExchangeThroughTemplate(exchange, null, serviceEvent, event);

    }

    public void sendToInterestedParties(PreProcessData preProcessData, Exchange exchange) {
        validatePreProcessData(preProcessData);
        List<String> interestedParties = preProcessData.getInterestedParties();
        ServiceEvent serviceEvent = preProcessData.getServiceEvent();
        ServiceEventResult serviceEventResult = preProcessData.getServiceEventResult();
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

    private void validatePreProcessData(PreProcessData preProcessData) {
        checkNotNull(preProcessData.getInterestedParties());
        if (preProcessData.getServiceEvent() == null) {
            //then serviceEventResult should not be null
            checkNotNull(preProcessData.getServiceEventResult());
        }
    }
}
