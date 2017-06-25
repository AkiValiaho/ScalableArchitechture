package com.akivaliaho;

import com.akivaliaho.event.EventInterestRegistrer;
import org.apache.camel.Exchange;

import java.util.List;

/**
 * Created by vagrant on 6/25/17.
 */
public class InterestHandler {

    private final EventInterestRegistrer eventInterestRegistrer;
    private final ExchangeTools exchangeTools;

    public InterestHandler(EventInterestRegistrer eventInterestRegistrer, ExchangeTools exchangeTools) {
        this.eventInterestRegistrer =eventInterestRegistrer;
        this.exchangeTools = exchangeTools;
    }

    public void handleDeclarationOfInterests(Exchange exchange, ServiceEvent serviceEvent, ServiceEventResult serviceEventResult) {
                List<String> interestedParties = eventInterestRegistrer.getInterestedParties(serviceEvent);
        if (!isDeclarationOfInterest(exchange, serviceEvent)) {
            exchangeTools.sendToInterestedParties(interestedParties, serviceEvent, serviceEventResult, exchange);
        }
    }


    private boolean isDeclarationOfInterest(Exchange exchange, ServiceEvent serviceEvent) {
        if (serviceEvent.getEventName().equals("declarationOfInterests")) {
            registerInterests(exchange, serviceEvent);
            return true;
        }
        if (serviceEvent.getEventName().equals("com.akivaliaho.RequestInterestedPartiesEventResult")) {
            registerOnInitInterestResult(exchange, serviceEvent);
            return true;
        }
        return false;
    }

    private void registerOnInitInterestResult(Exchange exchange, ServiceEvent serviceEvent) {
    }

    private void registerInterests(Exchange exchange, ServiceEvent serviceEvent) {
    }
}
