package com.akivaliaho;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;

import java.io.IOException;

/**
 * Created by akivv on 25.6.2017.
 */
public interface ServiceEventSending {
    void handleInterestFlowAndSend(ServiceEvent serviceEvent, Exchange exchange);

    void requestInterestedParties(CamelContext context) throws IOException;

    void sendPollResultToConfig(Exchange exchange);
}
