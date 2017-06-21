package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.ServiceEvent;
import com.akivaliaho.event.LocalEventDelegator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by vagrant on 6/19/17.
 */
@Component
public class RequestInterestedPartiesEventStrategy implements Strategy, AMQPExecuting {
    private final LocalEventDelegator localEventDelegator;

    @Autowired
    public RequestInterestedPartiesEventStrategy(LocalEventDelegator localEventDelegator) {
        this.localEventDelegator = localEventDelegator;
    }

    @Override
    public void execute(ServiceEvent serviceEvent) throws InstantiationException {
        localEventDelegator.delegateEvent(serviceEvent);
    }

    @Override
    public void execute(Object amqpEvent) throws InstantiationException {
        if (amqpEvent instanceof  ServiceEvent) {
            execute(((ServiceEvent) amqpEvent));
        }
    }
}
