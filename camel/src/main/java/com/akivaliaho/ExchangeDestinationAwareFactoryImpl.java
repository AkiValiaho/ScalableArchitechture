package com.akivaliaho;

import com.akivaliaho.event.EventInterestRegistrer;

/**
 * Created by vagrant on 6/24/17.
 */
public class ExchangeDestinationAwareFactoryImpl implements ExchangeDestinationAwareFactory {

    private final DefaultExchangeTools defaultExchangeTools;
    private final EventInterestRegistrer eventInterestRegistrer;

    public ExchangeDestinationAwareFactoryImpl(DefaultExchangeTools defaultExchangeTools, EventInterestRegistrer eventInterestRegistrer) {
        this.defaultExchangeTools = defaultExchangeTools;
        this.eventInterestRegistrer = eventInterestRegistrer;
    }

    @Override
    public ExchangeDestinationAware createExchangeSendingOperation(ExchangeOperation exchangeOperation) {
        //TODO Refactor this if-block
        if (exchangeOperation.equals(ExchangeOperation.INTERESTED_PARTIES)) {
            return new InterestedPartiesOperation(defaultExchangeTools);
        }
        if (exchangeOperation.equals(ExchangeOperation.POLL_RESULT_TO_CONFIG_MODULE)) {
            return new PollResulToConfigModuleOperation(defaultExchangeTools, eventInterestRegistrer);
        }
        if (exchangeOperation.equals(ExchangeOperation.POLL_RESULT_TO_CONFIG_MODULE_KNOWN_ROUTING_KEY)) {
            return new PollResulToConfigModuleKnownKeyOperation(defaultExchangeTools, eventInterestRegistrer);
        }
    }
}
