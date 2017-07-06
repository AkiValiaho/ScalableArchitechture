package com.akivaliaho;

import org.apache.camel.Exchange;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by akivv on 24.6.2017.
 */
public class ExchangeParser {
    private final ExchangeTools exchangeTools;

    public ExchangeParser(ExchangeTools exchangeTools) {
        this.exchangeTools = exchangeTools;
    }

    ;

    public ServiceEvent parseServiceEvent(Exchange exchange) throws IOException, ClassNotFoundException {
        return convertExchangeToServiceEvent(exchange);
    }

    private ServiceEvent convertExchangeToServiceEvent(Exchange exchange) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = exchangeTools.feedOutputStream(exchange);
        return (ServiceEvent) objectInputStream.readObject();

    }

    public DomainEvent parseServiceEventResult(ServiceEvent serviceEvent) {
        return parseServiceEventResultIfNeeded(serviceEvent);
    }

    private DomainEvent parseServiceEventResultIfNeeded(ServiceEvent serviceEvent) {
        DomainEvent serviceEventResult = null;
        if (serviceEvent.getEventName().toLowerCase().contains("result")) {
            serviceEventResult = ServiceEventResultBuilder.getBuilder()
                    .serviceEvent(serviceEvent)
                    .originalEventName(serviceEvent.getOriginalEventName())
                    .originalParameters(serviceEvent.getOriginalParameters()).build();
        }
        return serviceEventResult;
    }
}
