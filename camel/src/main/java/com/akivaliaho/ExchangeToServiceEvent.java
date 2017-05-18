package com.akivaliaho;

import com.akivaliaho.event.EventInterestHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akivv on 24.4.2017.
 */
@Slf4j
public class ExchangeToServiceEvent implements Processor {
    private final EventInterestHolder eventInterestHolder;
    private final ExchangeTools exchangeTools;
    private final ProcessPreparator processPreparator;
    private String configHolderRoutingKey;

    public ExchangeToServiceEvent(EventInterestHolder eventInterestHolder, ExchangeTools exchangeTools, ProcessPreparator processPreparator) {
        this.eventInterestHolder = eventInterestHolder;
        this.exchangeTools = exchangeTools;
        this.processPreparator = processPreparator;
        //TODO On-init connection to ConfigurationHolder
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        processPreparator.feedExchange(exchange).invoke();
        List<String> interestedParties = processPreparator.getInterestedParties();
        ServiceEventResult serviceEventResult = processPreparator.getServiceEventResult();
        ServiceEvent serviceEvent = processPreparator.getServiceEvent();
        handleDeclarationOfInterest(exchange, serviceEvent);
        exchangeTools.sendToInterestedParties(interestedParties, serviceEventResult, exchange, serviceEvent);

    }

    private void handleDeclarationOfInterest(Exchange exchange, ServiceEvent serviceEvent) {
        if (serviceEvent.getEventName().equals("declarationOfInterests")) {
            registerInterests(exchange, serviceEvent);
        }
    }

    private void registerInterests(Exchange exchange, ServiceEvent serviceEvent) {
        ServiceEvent o = (ServiceEvent) ((ArrayList) serviceEvent.getParameters()[0]).get(0);
        eventInterestHolder.registerInterests(serviceEvent);
        if (o.getEventName().equals("com.akivaliaho.ConfigurationPollEventResult")) {
            sendPollResultToConfigModule(exchange, serviceEvent);
        } else if (configHolderRoutingKey != null && !configHolderRoutingKey.isEmpty()) {
            exchangeTools.sendPollResult(eventInterestHolder.getEventInterestMap(), exchange, configHolderRoutingKey);
        }
    }

    private void sendPollResultToConfigModule(Exchange exchange, ServiceEvent serviceEvent) {
        //Send an configHolderRoutingKey about every interest back to the configuration holder
        configHolderRoutingKey = (String) serviceEvent.getParameters()[1];
        exchangeTools.sendPollResult(eventInterestHolder.getEventInterestMap(), exchange, configHolderRoutingKey);
    }


}
