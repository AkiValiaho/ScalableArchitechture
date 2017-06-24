package com.akivaliaho;

/**
 * Created by vagrant on 6/24/17.
 */
public class ExchangePropertyStrategyFactory {
    public ExchangePropertyPopulator createStrategy(ExchangePropertyStrategy exchangePropertyStrategy) {
        if (exchangePropertyStrategy.equals(ExchangePropertyStrategy.DEFAULT_EXCHANGE_PROPERTIES)) {
            //Build a default exchange strategy
            return new DefaultExchangeProperties();
        }
        return null;
    }
    public static enum ExchangePropertyStrategy {
        DEFAULT_EXCHANGE_PROPERTIES;
    }

}
