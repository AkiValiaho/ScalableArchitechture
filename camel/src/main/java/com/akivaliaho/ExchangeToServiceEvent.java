package com.akivaliaho;

import com.akivaliaho.event.EventInterestHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;

import java.io.*;
import java.util.List;

/**
 * Created by akivv on 24.4.2017.
 */
@Slf4j
public class ExchangeToServiceEvent implements Processor {
    private final EventInterestHolder eventInterestHolder;

    public ExchangeToServiceEvent(EventInterestHolder eventInterestHolder) {
        this.eventInterestHolder = eventInterestHolder;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        ObjectInputStream objectInputStream = feedOutputStream(exchange);
        ServiceEvent serviceEvent = (ServiceEvent) objectInputStream.readObject();
        if (serviceEvent.getEventName().equals("declarationOfInterests")) {
            eventInterestHolder.registerInterests(serviceEvent);
        }
        ServiceEventResult serviceEventResult = null;
        if (serviceEvent.getEventName().toLowerCase().contains("result")) {
            serviceEventResult = new ServiceEventResult(serviceEvent);
            serviceEventResult.setOriginalEventName(serviceEvent.getOriginalEventName());
            serviceEventResult.setOriginalParameters(serviceEvent.getOriginalParameters());
        }
        //TODO Pickup the listeners of this particular event before sending it back to the broker
        List<String> interestedParties = eventInterestHolder.getInterestedParties(serviceEvent);
        if (interestedParties != null) {
            ServiceEventResult finalServiceEventResult = serviceEventResult;
            interestedParties

                    .forEach(event -> {
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
                    });
        }
    }

    private ObjectInputStream feedOutputStream(Exchange exchange) throws IOException {
        byte[] body = (byte[]) exchange.getIn().getBody();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ObjectInputStream(byteArrayInputStream);
    }
}
