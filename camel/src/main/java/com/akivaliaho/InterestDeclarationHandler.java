package com.akivaliaho;

import com.akivaliaho.event.EventInterestRegistrer;
import org.apache.camel.Exchange;

/**
 * Created by vagrant on 6/25/17.
 */
public class InterestDeclarationHandler {

    private final EventInterestRegistrer eventInterestRegistrer;

    public InterestDeclarationHandler(EventInterestRegistrer eventInterestRegistrer) {
        this.eventInterestRegistrer = eventInterestRegistrer;
    }

    private boolean registerInterests(Exchange exchange, ServiceEvent serviceEvent) {
        if (serviceEvent.getEventName().equals("com.akivaliaho.RequestInterestedPartiesEventResult")) {
            registerOnInitInterestResult(exchange, serviceEvent);
            return true;
        }
        return false;
    }

    private void registerOnInitInterestResult(Exchange exchange, ServiceEvent serviceEvent) {
        eventInterestRegistrer.registerPollInterestResults(serviceEvent);
    }


    public boolean registerDeclarationOfInterest(Exchange exchange, ServiceEvent serviceEvent) {
        return registerInterests(exchange, serviceEvent);
    }
}
