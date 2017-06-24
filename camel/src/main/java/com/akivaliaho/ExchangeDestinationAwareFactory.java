package com.akivaliaho;

/**
 * Created by vagrant on 6/24/17.
 */
public interface ExchangeDestinationAwareFactory {
    ExchangeDestinationAware createExchangeSendingOperation(ExchangeOperation exchangeOperation);
}
