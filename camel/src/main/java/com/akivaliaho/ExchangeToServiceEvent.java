package com.akivaliaho;

import com.akivaliaho.event.ServiceEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * Created by akivv on 24.4.2017.
 */
@Slf4j
public class ExchangeToServiceEvent implements Processor {
    private final ProducerTemplate producerTemplate;

    public ExchangeToServiceEvent(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        byte[] body = (byte[]) exchange.getIn().getBody();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        ServiceEvent serviceEvent = (ServiceEvent) objectInputStream.readObject();
        //TODO Pickup the listeners of this particular event before sending it back to the broker
        producerTemplate.sendBody("");
        exchange.getIn().setBody(serviceEvent);
    }
}
