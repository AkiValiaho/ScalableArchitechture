package com.akivaliaho;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;

import java.io.IOException;

/**
 * Created by akivv on 25.6.2017.
 */
public class CommandDelegatingImpl implements CommandDelegating {
    private final ExchangeConverting exchangeConvertingImpl;
    private final ServiceEventSending serviceEventSending;

    public CommandDelegatingImpl(ExchangeConverting exchangeConverting, ServiceEventSending serviceEventSending) {
        this.exchangeConvertingImpl = exchangeConverting;
        this.serviceEventSending = serviceEventSending;
    }

    @Override
    public void sendExchange(Exchange exchange) throws IOException, ClassNotFoundException {
        ServiceEvent serviceEvent = this.exchangeConvertingImpl.convertExchangeToServiceEvent(exchange);
        serviceEventSending.handleInterestFlowAndSend(serviceEvent, exchange);
    }


    @Override
    public void sendPollResultToConfig(Exchange exchange) {
        serviceEventSending.sendPollResultToConfig(exchange);
    }

    @Override
    public void sendInterestedPartiesRequest(CamelContext context) throws IOException {
        serviceEventSending.requestInterestedParties(context);
    }
}
