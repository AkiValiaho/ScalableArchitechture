package com.akivaliaho.domain;

import org.apache.camel.Exchange;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.apache.camel.impl.DefaultMessage;

import java.io.IOException;

/**
 * DomainExchange is a domain level exchange that
 * embeds the given object to an {@Exchange}.
 * Uses default properties
 * <p>
 * Created by akivv on 23.7.2017.
 */
public class DomainExchange {
    private Exchange exchange;

    public DomainExchange(Exchange exchange, Object object) throws IOException {
        this.exchange = exchange;
        configureExchange(exchange, object);
    }

    private void configureExchange(Exchange exchange, Object object) throws IOException {
        if (exchange.getIn() == null) {
            exchange.setIn(new DefaultMessage());
        }
        if (object instanceof ServiceEvent) {
            configureExchange(exchange, ((ServiceEvent) object).toBytes(), ((ServiceEvent) object).getEvent());
        } else {
            //Not a ServiceEvent, use empty as event name
            configureExchange(exchange, ((ServiceEvent) object).toBytes());
        }
    }

    private void configureExchange(Exchange exchange, byte[] bytes, String event) {
        exchange.getIn().setBody(bytes);
        exchange.getIn().setHeader("routingKey", event);
        exchange.getIn().setHeader(RabbitMQConstants.EXCHANGE_NAME, exchange.getIn().getHeader("routingKey"));
        exchange.getIn().setHeader(RabbitMQConstants.ROUTING_KEY, "");
    }

    /**
     * Sets the bytes provided to the given exchange
     * and sets default routing keys as headers
     * <p>
     * Uses empty as event name
     *
     * @param exchange
     * @param bytes
     */
    private void configureExchange(Exchange exchange, byte[] bytes) {
        configureExchange(exchange, bytes, "");
    }


    public Exchange getExchange() {
        return exchange;
    }
}
