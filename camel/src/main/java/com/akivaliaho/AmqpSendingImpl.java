package com.akivaliaho;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by akivv on 25.6.2017.
 */
public class AmqpSendingImpl implements AmqpSending, ConfigModuleRoutingKeyAware {

    private final String configModuleRoutingKey = Addresses.CONFIGURATIONSERVICE_DEFAULT.getValue();
    private final ExchangeTools exchangeTools;
    private final ExchangeParser exchangeParser;

    public AmqpSendingImpl(ExchangeTools exchangeTools, ExchangeParser exchangeParser) {
        this.exchangeTools = exchangeTools;
        this.exchangeParser = exchangeParser;
    }

    @Override
    public void sendToInterestedParties(List<String> interestedParties, ServiceEvent serviceEvent, Exchange exchange) {
        DomainEvent serviceEventResult = exchangeParser.parseServiceEventResult(serviceEvent);
        exchangeTools.sendToInterestedParties(interestedParties, serviceEvent, serviceEventResult, exchange);
    }

    @Override
    public void requestInterestedParties(CamelContext context) throws IOException {
        exchangeTools.requestInterestedParties(configModuleRoutingKey, context);
    }

    @Override
    public void sendPollResultToConfig(Exchange exchange, Map<ServiceEvent, List<String>> eventInterestMap) {
        exchangeTools.sendPollResult(eventInterestMap, exchange, configModuleRoutingKey);
    }

    @Override
    public String getConfigModuleRoutingKey() {
        return configModuleRoutingKey;
    }
}
