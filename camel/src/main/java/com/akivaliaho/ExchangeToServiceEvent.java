package com.akivaliaho;

import com.akivaliaho.event.EventInterestHolder;
import com.akivaliaho.event.ServiceEvent;
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
        byte[] body = (byte[]) exchange.getIn().getBody();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        ServiceEvent serviceEvent = (ServiceEvent) objectInputStream.readObject();
        if (serviceEvent.getEventName().equals("declarationOfInterests")) {
            eventInterestHolder.registerInterests(serviceEvent);
        }
        //TODO Pickup the listeners of this particular event before sending it back to the broker
        List<String> interestedParties = eventInterestHolder.getInterestedParties(serviceEvent);
        if (interestedParties != null) {
            interestedParties

                    .forEach(event -> {
                        //Send all the events through the producer template
                        try {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                            objectOutputStream.writeObject(serviceEvent);
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
}
