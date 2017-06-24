package com.akivaliaho;

import org.apache.camel.Exchange;

import java.util.List;

/**
 * Created by vagrant on 6/24/17.
 */
public class InterestedPartiesOperation implements ExchangeDestinationAware<List<String>, ServiceEvent, ServiceEventResult> {
    private final DefaultExchangeTools defaultExchangeTools;

    public InterestedPartiesOperation(DefaultExchangeTools defaultExchangeTools) {
        this.defaultExchangeTools = defaultExchangeTools;
    }

    @Override
    public void sendExchange(Exchange exchange, ServiceEvent interestedParties, ServiceEvent serviceEvent, Void serviceEventResult) {
        defaultExchangeTools.sendToInterestedParties(interestedParties, serviceEvent, serviceEventResult, exchange);
    }
}
