package com.akivaliaho;

import org.apache.camel.Exchange;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.apache.camel.impl.DefaultMessage;

/**
 * Created by vagrant on 6/24/17.
 */
public class DefaultExchangeProperties implements ExchangePropertyPopulator {
    @Override
    public void configureExchange(Object... params) {
        setDefaultExchangeProperties(((Exchange) params[0]), ((String) params[1]), ((byte[]) params[2]));
    }

    private void setDefaultExchangeProperties(Exchange exchange, String routingKey, byte[] bytes) {
        //If in is empty this is a default message, fill the in
        if (exchange.getIn() == null) {
            exchange.setIn(new DefaultMessage());
        }
        exchange.getIn().setBody(bytes);
        exchange.getIn().setHeader("routingKey", routingKey);
        exchange.getIn().setHeader(RabbitMQConstants.EXCHANGE_NAME, exchange.getIn().getHeader("routingKey"));
        exchange.getIn().setHeader(RabbitMQConstants.ROUTING_KEY, "");
    }
}
