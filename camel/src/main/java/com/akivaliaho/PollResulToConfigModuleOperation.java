package com.akivaliaho;

import com.akivaliaho.event.EventInterestRegistrer;
import org.apache.camel.Exchange;

/**
 * Created by vagrant on 6/24/17.
 */
public class PollResulToConfigModuleOperation implements ExchangeDestinationAware<ServiceEvent, Void, Void> {
    private final DefaultExchangeTools defaultExchangeTools;
    private final EventInterestRegistrer eventInterestRegistrer;

    public PollResulToConfigModuleOperation(DefaultExchangeTools defaultExchangeTools, EventInterestRegistrer eventInterestRegistrer) {
        this.defaultExchangeTools = defaultExchangeTools;
        this.eventInterestRegistrer = eventInterestRegistrer;
    }

    @Override
    public void sendExchange(Exchange exchange, ServiceEvent s, Void y, Void T) {
        this.defaultExchangeTools.sendPollResult(eventInterestRegistrer.getEventInterestMap(), exchange, ((String) s.getParameters()[1]));
    }
}
