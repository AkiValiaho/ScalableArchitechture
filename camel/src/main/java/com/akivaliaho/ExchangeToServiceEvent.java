package com.akivaliaho;

import com.akivaliaho.event.EventInterestRegistrer;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ModelCamelContext;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by akivv on 24.4.2017.
 */
@Slf4j
public class ExchangeToServiceEvent implements Processor {
    private final EventInterestRegistrer eventInterestRegistrer;
    private final ExchangeTools exchangeTools;
    private final ProcessPreparator processPreparator;
    private CamelContext context;
    private String configHolderRoutingKey = Addresses.CONFIGURATIONSERVICE_DEFAULT.getValue();
    private ModelCamelContext camelContext;

    public ExchangeToServiceEvent(EventInterestRegistrer eventInterestRegistrer, ExchangeTools exchangeTools, ProcessPreparator processPreparator, CamelContext context) {
        this.eventInterestRegistrer = eventInterestRegistrer;
        this.exchangeTools = exchangeTools;
        this.processPreparator = processPreparator;
        //TODO On-init connection to ConfigurationHolder
        this.context = context;

    }

    public void sendInterestRequest() {
        try {
            exchangeTools.requestInterestedParties(configHolderRoutingKey, context);
        } catch (IOException e) {
            //TODO Handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        PreProcessData preprocessData = processPreparator
                .feedExchange(exchange)
                .invoke()
                .getPreprocessData();
        if (!handleDeclarationOfInterest(exchange, preprocessData.getServiceEvent())) {
            exchangeTools.sendToInterestedParties(preprocessData, exchange
            );
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

    private void registerOnInitInterestResult(Exchange exchange, ServiceEvent serviceEvent) {
        eventInterestRegistrer.registerPollInterestResults(serviceEvent);
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

    private void sendPollResultToConfigModule(Exchange exchange, ServiceEvent serviceEvent) {
        //Send an configHolderRoutingKey about every interest back to the configuration holder
        configHolderRoutingKey = (String) serviceEvent.getParameters()[1];
        //TODO Rethink this a bit, transitive access of a class, not good design
        exchangeTools.sendPollResult(eventInterestRegistrer.getEventInterestMap(), exchange, configHolderRoutingKey);
    }

    public void setCamelContext(CamelContext camelContext) {
        this.context = camelContext;
    }
}
