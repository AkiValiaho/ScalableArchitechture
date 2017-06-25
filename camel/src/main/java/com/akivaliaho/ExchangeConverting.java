package com.akivaliaho;

import org.apache.camel.Exchange;

import java.io.IOException;

/**
 * Created by akivv on 25.6.2017.
 */
public interface ExchangeConverting {
    ServiceEvent convertExchangeToServiceEvent(Exchange exchange) throws IOException, ClassNotFoundException;
}
