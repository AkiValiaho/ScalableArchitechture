package com.akivaliaho;

import com.akivaliaho.event.EventInterestRegistrer;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by akivv on 25.6.2017.
 */
public class ServiceEventSendingImpl implements ServiceEventSending {
    private final InterestFlowHandling interestFlowHandling;
    private final EventInterestRegistrer eventInterestRegistrer;
    private final AmqpSending amqpSending;

    public ServiceEventSendingImpl(EventInterestRegistrer eventInterestRegistrer, AmqpSending amqpSending, InterestFlowHandling interestFlowHandling) {
        this.amqpSending = amqpSending;
        this.interestFlowHandling = interestFlowHandling;
        this.eventInterestRegistrer = eventInterestRegistrer;
    }

    @Override
    public void handleInterestFlowAndSend(ServiceEvent serviceEvent, Exchange exchange) {
        //TODO What the heck is this?
        boolean wasInterestEvent = false;
        if (serviceEvent.getEventName().equals("declarationOfInterests")) {
            register(exchange, serviceEvent);
            wasInterestEvent = true;
        } else {
            wasInterestEvent = interestFlowHandling.handleInterestFlow(serviceEvent, exchange);
        }
        if (!wasInterestEvent) {
            amqpSending.sendToInterestedParties(eventInterestRegistrer.getInterestedParties(serviceEvent), serviceEvent, exchange);
        }
    }

    private void register(Exchange exchange, ServiceEvent serviceEvent) {
        ServiceEvent o = (ServiceEvent) ((ArrayList) serviceEvent.getParameters()[0]).get(0);
        eventInterestRegistrer.registerInterests(serviceEvent);
        if (o.getEventName().equals("com.akivaliaho.ConfigurationPollEventResult") || serviceEvent.getEventName().equals("declarationOfInterests")) {
            amqpSending.sendPollResultToConfig(exchange, eventInterestRegistrer.getEventInterestMap());
        }
    }

    @Override
    public void requestInterestedParties(CamelContext context) throws IOException {
        amqpSending.requestInterestedParties(context);
    }

    @Override
    public void sendPollResultToConfig(Exchange exchange) {
        Map<ServiceEvent, List<String>> eventInterestMap = eventInterestRegistrer.getEventInterestMap();
        amqpSending.sendPollResultToConfig(exchange, eventInterestMap);
    }

}
