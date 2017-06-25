package com.akivaliaho;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by akivv on 25.6.2017.
 */
public interface AmqpSending {
    void sendToInterestedParties(List<String> interestedParties, ServiceEvent serviceEvent, Exchange exchange);

    void requestInterestedParties(CamelContext context) throws IOException;

    void sendPollResultToConfig(Exchange exchange, Map<ServiceEvent, List<String>> eventInterestMap);
}
