package com.akivaliaho;

import org.apache.camel.Exchange;

import java.io.IOException;

/**
 * Created by akivv on 24.6.2017.
 */
public interface ExchangeParser {
    ServiceEvent parseServiceEvent(Exchange exchange) throws IOException, ClassNotFoundException;

    ServiceEventResult parseServiceEventResult(ServiceEvent serviceEvent);
}
