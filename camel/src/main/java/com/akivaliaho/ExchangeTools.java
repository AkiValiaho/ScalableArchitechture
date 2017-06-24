package com.akivaliaho;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by akivv on 18.5.2017.
 */
public class ExchangeTools {

    private final ByteTools byteTools;
    private final ExchangePropertyPopulator exchangePropertyPopulator;

    public ExchangeTools(ExchangePropertyPopulator exchangePropertyPopulator, ByteTools byteTools) {
        this.byteTools = byteTools;
        this.exchangePropertyPopulator = exchangePropertyPopulator;
    }

    ObjectInputStream feedOutputStream(Exchange exchange) throws IOException {
        //TODO Refactor this whole class, it's spaghetti
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
        if (finalServiceEventResult != null) {
            return byteTools.objectToBytes(finalServiceEventResult);
        } else {
            return byteTools.objectToBytes(serviceEvent);
        }
    }

    private void sendExchangeWithProducerTemplate(Exchange exchange, String event, byte[] bytes) {
        //Set Default routing properties for the exchange message
        exchangePropertyPopulator.configureExchange(exchange, event, bytes);
        exchange.getContext().createProducerTemplate().send("direct:fromESB", exchange);
    }


    public void sendPollResult(HashMap<ServiceEvent, List<String>> eventInterestMap, Exchange exchange, String event) {
        ServiceEventResult serviceEvent = new ConfigurationPollEventResult(eventInterestMap);
        sendExchangeThroughTemplate(exchange, null, serviceEvent, event);

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
