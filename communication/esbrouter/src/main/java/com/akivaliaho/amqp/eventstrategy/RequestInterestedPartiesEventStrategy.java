package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.ServiceEvent;
import com.akivaliaho.event.LocalEventDelegator;

/**
 * Created by vagrant on 6/19/17.
 */
public class RequestInterestedPartiesEventStrategy implements Strategy {
    private final LocalEventDelegator localEventDelegator;

    public RequestInterestedPartiesEventStrategy(LocalEventDelegator localEventDelegator) {
        this.localEventDelegator = localEventDelegator;
    }

    @Override
    public void execute(ServiceEvent serviceEvent) throws InstantiationException {
        localEventDelegator.delegateEvent(serviceEvent);
    }
}