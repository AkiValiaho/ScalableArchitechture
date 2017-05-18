package com.akivaliaho;

import com.akivaliaho.event.EventInterestHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akivv on 24.4.2017.
 */
@Slf4j
public class ExchangeToServiceEvent implements Processor {
    private final EventInterestHolder eventInterestHolder;
    private final ExchangeTools exchangeTools;
    private String configHolderRoutingKey;

    public ExchangeToServiceEvent(EventInterestHolder eventInterestHolder, ExchangeTools exchangeTools) {
        this.eventInterestHolder = eventInterestHolder;
        this.exchangeTools = exchangeTools;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        ObjectInputStream objectInputStream = exchangeTools.feedOutputStream(exchange);
        ServiceEvent serviceEvent = (ServiceEvent) objectInputStream.readObject();
        if (serviceEvent.getEventName().equals("declarationOfInterests")) {
            ServiceEvent o = (ServiceEvent) ((ArrayList) serviceEvent.getParameters()[0]).get(0);
            eventInterestHolder.registerInterests(serviceEvent);
            if (o.getEventName().equals("com.akivaliaho.ConfigurationPollEventResult")) {
                //Send an configHolderRoutingKey about every interest back to the configuration holder
                configHolderRoutingKey = (String) serviceEvent.getParameters()[1];
                exchangeTools.sendPollResult(eventInterestHolder.getEventInterestMap(), exchange, configHolderRoutingKey);
                return;
            }
            if (configHolderRoutingKey != null && !configHolderRoutingKey.isEmpty()) {
                exchangeTools.sendPollResult(eventInterestHolder.getEventInterestMap(), exchange, configHolderRoutingKey);
            }
        }
        ServiceEventResult serviceEventResult = null;
        if (serviceEvent.getEventName().toLowerCase().contains("result")) {
            serviceEventResult = new ServiceEventResult(serviceEvent);
            serviceEventResult.setOriginalEventName(serviceEvent.getOriginalEventName());
            serviceEventResult.setOriginalParameters(serviceEvent.getOriginalParameters());
        }
        List<String> interestedParties = eventInterestHolder.getInterestedParties(serviceEvent);
        if (interestedParties != null) {
            ServiceEventResult finalServiceEventResult = serviceEventResult;
            interestedParties
                    .forEach(event -> {
                        exchangeTools.sendExchangeThroughTemplate(exchange, serviceEvent, finalServiceEventResult, event);
                    });
        }
    }


}
