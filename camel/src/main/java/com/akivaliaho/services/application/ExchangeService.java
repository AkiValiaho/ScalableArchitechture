package com.akivaliaho.services.application;

import com.akivaliaho.ByteTools;
import com.akivaliaho.domain.InterestHolder;
import com.akivaliaho.domain.ServiceEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Service that implements {@Processor} to get the exchanges
 * and delegate operation on them
 */
@Slf4j
public class ExchangeService implements Processor {

    private final ByteTools byteTools;
    private final AmqpConnection amqpConnection;
    private final InterestHolder interestHolder;

    public ExchangeService(AmqpConnection amqpConnection, ByteTools byteTools, InterestHolder interestHolder) {
        this.byteTools = byteTools;
        this.amqpConnection = amqpConnection;
        this.interestHolder = interestHolder;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        //Default operation is to send exchange with the new ServiceEvent converted to a DtO embedded through amqp
        new ServiceEvent(exchange, byteTools, amqpConnection, interestHolder);
    }
}
