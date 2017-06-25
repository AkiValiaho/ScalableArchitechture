package com.akivaliaho;

import com.akivaliaho.event.EventInterestRegistrer;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vagrant on 6/25/17.
 */

public class ServiceEventHandler {

    private final ExchangeParser exchangeParser;
    private final ExchangeTools exchangeTools;
    private final EventInterestRegistrer eventInterestRegistrer;
    private String configHolderRoutingKey;
    private CamelContext context;

    public ServiceEventHandler(ExchangeParser exchangeParser, ExchangeTools exchangeTools, EventInterestRegistrer eventInterestRegistrer) {
        this.exchangeParser = exchangeParser;
        this.exchangeTools= exchangeTools;
        this.eventInterestRegistrer = eventInterestRegistrer;
    }

    private void registerOnInitInterestResult(Exchange exchange, ServiceEvent serviceEvent) {
        eventInterestRegistrer.registerPollInterestResults(serviceEvent);
    }


    private void sendPollResultToConfigModule(Exchange exchange, ServiceEvent serviceEvent) {
        //Send an configHolderRoutingKey about every interest back to the configuration holder
        configHolderRoutingKey = (String) serviceEvent.getParameters()[1];
        exchangeTools.sendPollResult(eventInterestRegistrer.getEventInterestMap(), exchange, configHolderRoutingKey);
    }

    public void sendInterestRequest() {
        try {
            exchangeTools.requestInterestedParties(configHolderRoutingKey, context);
        } catch (IOException e) {
            //TODO Handle exception
            e.printStackTrace();
        }
    }

    public void handleServiceEvent(Exchange exchange) throws IOException, ClassNotFoundException {
        //Convert exchange to serviceevent
        ServiceEvent serviceEvent = exchangeParser.parseServiceEvent(exchange);
        ServiceEventResult serviceEventResult = exchangeParser.parseServiceEventResult(serviceEvent);
        List<String> interestedParties = eventInterestRegistrer.getInterestedParties(serviceEvent);
        if (!handleDeclarationOfInterest(exchange, serviceEvent)) {
            exchangeTools.sendToInterestedParties(interestedParties, serviceEvent, serviceEventResult, exchange);
        }
    }

    private void registerInterests(Exchange exchange, ServiceEvent serviceEvent) {
        ServiceEvent o = (ServiceEvent) ((ArrayList) serviceEvent.getParameters()[0]).get(0);
        eventInterestRegistrer.registerInterests(serviceEvent);
        if (o.getEventName().equals("com.akivaliaho.ConfigurationPollEventResult")) {
            sendPollResultToConfigModule(exchange, serviceEvent);
        } else if (configHolderRoutingKey != null && !configHolderRoutingKey.isEmpty()) {
            exchangeTools.sendPollResult(eventInterestRegistrer.getEventInterestMap(), exchange, configHolderRoutingKey);
        }
    }

    private boolean handleDeclarationOfInterest(Exchange exchange, ServiceEvent serviceEvent) {
        if (serviceEvent.getEventName().equals("declarationOfInterests")) {
            registerInterests(exchange, serviceEvent);
            return true;
        }
        if (serviceEvent.getEventName().equals("com.akivaliaho.RequestInterestedPartiesEventResult")) {
            registerOnInitInterestResult(exchange, serviceEvent);
            return true;
        }
        return false;
    }

    public void setCamelContext(CamelContext camelContext) {
        this.context = camelContext;
    }
}
