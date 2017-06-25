package com.akivaliaho;

import com.akivaliaho.event.EventInterestRegistrer;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ModelCamelContext;

/**
 * Created by akivv on 24.4.2017.
 */
@Slf4j
public class ExchangeToServiceEvent implements Processor {
    private final EventInterestRegistrer eventInterestRegistrer;
    private final ServiceEventHandler serviceEventHandler;
    private CamelContext context;
    private String configHolderRoutingKey = Addresses.CONFIGURATIONSERVICE_DEFAULT.getValue();
    private ModelCamelContext camelContext;

    public ExchangeToServiceEvent(EventInterestRegistrer eventInterestRegistrer, ServiceEventHandler serviceEventHandler,  CamelContext context
                                  ) {
        this.eventInterestRegistrer = eventInterestRegistrer;
        this.context = context;
        this.serviceEventHandler = serviceEventHandler;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        serviceEventHandler.handleServiceEvent(exchange);

    }

    public void setCamelContext(CamelContext camelContext) {
        this.serviceEventHandler.setCamelContext(camelContext);
    }

    public void sendInterestRequest() {
        this.serviceEventHandler.sendInterestRequest();
    }
}
