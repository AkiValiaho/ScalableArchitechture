package com.akivaliaho;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;

import java.io.IOException;

/**
 * Created by akivv on 25.6.2017.
 */
public interface CommandDelegating {
    void sendExchange(Exchange exchange) throws IOException, ClassNotFoundException;

    void sendPollResultToConfig(Exchange exchange);

    void sendInterestedPartiesRequest(CamelContext context) throws IOException;
}
