package com.akivaliaho;

import org.apache.camel.Exchange;

import java.io.IOException;

/**
 * Created by akivv on 25.6.2017.
 */
public class ExchangeConvertingImpl implements ExchangeConverting {
    private final ExchangeParser exchangeParser;

    public ExchangeConvertingImpl(ExchangeParser exchangeParser) {
        this.exchangeParser = exchangeParser;
    }

    @Override
    public ServiceEvent convertExchangeToServiceEvent(Exchange exchange) throws IOException, ClassNotFoundException {
        return exchangeParser.parseServiceEvent(exchange);
    }
}
