package com.akivaliaho.services.application;

import com.akivaliaho.ByteTools;
import com.akivaliaho.domain.ServiceEvent;
import com.akivaliaho.services.domain.ServiceEventSendingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Service that implements {@Processor} to get the exchanges
 * and delegate operation on them
 */
@Slf4j
public class ExchangeService implements Processor {

    private final ServiceEventSendingTemplate serviceEventSendingTemplate;
    private final ByteTools byteTools;

    public ExchangeService(ServiceEventSendingTemplate serviceEventSendingTemplate, ByteTools byteTools) {
        this.serviceEventSendingTemplate = serviceEventSendingTemplate;
        this.byteTools = byteTools;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        ServiceEvent serviceEvent = new ServiceEvent(exchange, byteTools);
        //Default operation is to send exchange with the new ServiceEvent converted to a DtO embedded through amqp
        serviceEventSendingTemplate.sendServiceEvent(exchange, serviceEvent);
    }
}
