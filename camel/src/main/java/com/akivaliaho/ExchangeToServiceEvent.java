package com.akivaliaho;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.IOException;

/**
 * Created by akivv on 24.4.2017.
 */
@Slf4j
public class ExchangeToServiceEvent implements Processor {
    private final ServiceEventToCommand serviceEventToCommand;

    public ExchangeToServiceEvent(ServiceEventToCommand serviceEventToCommand
    ) {
        this.serviceEventToCommand = serviceEventToCommand;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        serviceEventToCommand.handleServiceEvent(exchange);

    }

    public void setCamelContext(CamelContext camelContext) {
        this.serviceEventToCommand.setCamelContext(camelContext);
    }

    public void sendInterestRequest() throws IOException {
        this.serviceEventToCommand.sendInterestRequest();
    }
}
