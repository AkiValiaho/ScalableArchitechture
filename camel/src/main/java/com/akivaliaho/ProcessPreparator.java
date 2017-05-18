package com.akivaliaho;

import com.akivaliaho.event.EventInterestHolder;
import org.apache.camel.Exchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class ProcessPreparator {
    private final EventInterestHolder eventInterestHolder;
    private final ExchangeTools exchangeTools;
    private Exchange exchange;
    private ServiceEvent serviceEvent;
    private ServiceEventResult serviceEventResult;
    private List<String> interestedParties;

    public ProcessPreparator(EventInterestHolder eventInterestHolder, ExchangeTools exchangeTools) {
        this.eventInterestHolder = eventInterestHolder;
        this.exchangeTools = exchangeTools;
    }

    public ServiceEvent getServiceEvent() {
        return serviceEvent;
    }

    public ServiceEventResult getServiceEventResult() {
        return serviceEventResult;
    }

    public List<String> getInterestedParties() {
        return interestedParties;
    }

    public ProcessPreparator invoke() throws IOException, ClassNotFoundException {
        serviceEvent = convertExchangeToServiceEvent(exchange);
        serviceEventResult = parseServiceEventResultIfNeeded(serviceEvent);
        interestedParties = eventInterestHolder.getInterestedParties(serviceEvent);
        return this;
    }

    private ServiceEvent convertExchangeToServiceEvent(Exchange exchange) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = exchangeTools.feedOutputStream(exchange);
        return (ServiceEvent) objectInputStream.readObject();

    }

    private ServiceEventResult parseServiceEventResultIfNeeded(ServiceEvent serviceEvent) {
        ServiceEventResult serviceEventResult = null;
        if (serviceEvent.getEventName().toLowerCase().contains("result")) {
            serviceEventResult = ServiceEventResultBuilder.getBuilder()
                    .serviceEvent(serviceEvent)
                    .originalEventName(serviceEvent.getOriginalEventName())
                    .originalParameters(serviceEvent.getOriginalParameters()).build();
        }
        return serviceEventResult;
    }

    public ProcessPreparator feedExchange(Exchange exchange) {
        this.exchange = exchange;
        return this;
    }
}