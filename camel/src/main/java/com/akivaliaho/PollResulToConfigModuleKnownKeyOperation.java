package com.akivaliaho;

import com.akivaliaho.event.EventInterestRegistrer;
import org.apache.camel.Exchange;

/**
 * Created by vagrant on 6/24/17.
 */
public class PollResulToConfigModuleKnownKeyOperation implements ExchangeDestinationAware<String, Void, Void> {
    private final DefaultExchangeTools defaultExchangeTool;
    private final EventInterestRegistrer eventInterestRegistrer;

    public PollResulToConfigModuleKnownKeyOperation(DefaultExchangeTools defaultExchangeTools, EventInterestRegistrer eventInterestRegistrer) {
        this.defaultExchangeTool = defaultExchangeTools;
        this.eventInterestRegistrer = eventInterestRegistrer;
    }


    @Override
    public void sendExchange(Exchange exchange, String routingKey, Void aVoid, Void aVoid2) {
        defaultExchangeTool.sendPollResult(eventInterestRegistrer.getEventInterestMap(), exchange, routingKey);
    }
}
