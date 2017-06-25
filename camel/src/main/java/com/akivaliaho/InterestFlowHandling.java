package com.akivaliaho;

import org.apache.camel.Exchange;

/**
 * Created by akivv on 25.6.2017.
 */
public class InterestFlowHandling {
    private final InterestDeclarationHandler interestDeclarationHandler;

    public InterestFlowHandling(InterestDeclarationHandler interestDeclarationHandler) {
        this.interestDeclarationHandler = interestDeclarationHandler;
    }

    public boolean handleInterestFlow(ServiceEvent serviceEvent, Exchange exchange) {
        return interestDeclarationHandler.registerDeclarationOfInterest(exchange, serviceEvent);
    }
}
