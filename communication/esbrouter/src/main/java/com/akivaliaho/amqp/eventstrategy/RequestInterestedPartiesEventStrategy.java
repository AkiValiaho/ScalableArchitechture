package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.ServiceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by vagrant on 6/19/17.
 */
@Component
public class RequestInterestedPartiesEventStrategy implements Strategy, AMQPExecuting {

    private final SafeEventDelegator safeEventDelegator;

    @Autowired
    public RequestInterestedPartiesEventStrategy(SafeEventDelegator safeEventDelegator) {
        this.safeEventDelegator = safeEventDelegator;
    }

    @Override
    public void execute(ServiceEvent serviceEvent) throws InstantiationException, DelegationFailure {
        safeEventDelegator.safeDelegation(serviceEvent, 10);
    }

    @Override
    public void execute(Object amqpEvent) throws InstantiationException, DelegationFailure {
        if (amqpEvent instanceof ServiceEvent) {
            execute(((ServiceEvent) amqpEvent));
        }
    }
}
