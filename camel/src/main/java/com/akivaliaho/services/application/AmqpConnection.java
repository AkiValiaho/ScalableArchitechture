package com.akivaliaho.services.application;

import com.akivaliaho.domain.DomainExchange;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;

import java.io.IOException;

/**
 * This class sends objects through amqp template to the direct:fromESB endpoint
 * Created by akivv on 22.7.2017.
 */
public class AmqpConnection {

    /**
     * Sends an object through the producer template
     * created from given exchange, uses {@DomainExchange} to configure the
     * Exchange with default properties and sets the given object to that exchange
     */
    public void sendObject(Exchange exchange, Object object) throws IOException {
        DomainExchange domainExchange = new DomainExchange(exchange, object);
        sendDomainExchange(domainExchange);
    }

    private void sendDomainExchange(DomainExchange domainExchange) {
        sendExchange(domainExchange.getExchange());
    }

    /**
     * Sends an exchange with the producer template.
     * Assumes that all the proper headers are set in {@Exchange}
     *
     * @param exchange
     */
    public void sendExchange(Exchange exchange) {
        ProducerTemplate producerTemplate = exchange.getContext().createProducerTemplate();
        producerTemplate.send("direct:fromESB", exchange);
    }
}
