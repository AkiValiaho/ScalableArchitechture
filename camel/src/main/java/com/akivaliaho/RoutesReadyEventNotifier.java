package com.akivaliaho;

import org.apache.camel.support.EventNotifierSupport;

import java.util.EventObject;

/**
 * Created by akivv on 7.6.2017.
 */
public class RoutesReadyEventNotifier extends EventNotifierSupport {

    private final ExchangeToServiceEvent exchangeToServiceEvent;

    public RoutesReadyEventNotifier(ExchangeToServiceEvent exchangeToServiceEvent) {
        this.exchangeToServiceEvent = exchangeToServiceEvent;
    }

    @Override
    public void notify(EventObject event) throws Exception {
        exchangeToServiceEvent.sendInterestRequest();
    }

    @Override
    public boolean isEnabled(EventObject event) {
        if (event.toString().contains("Started CamelContext")) {
            return true;
        }
        return false;
    }
}
