package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.ServiceEvent;
import com.akivaliaho.event.LocalEventDelegator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by vagrant on 6/19/17.
 */
@Component
public class ConfigurationPollEventResultStrategy implements Strategy, AMQPExecuting {
    private final SafeEventDelegator safeEventDelegator;

    @Autowired
    public ConfigurationPollEventResultStrategy(SafeEventDelegator safeEventDelegator) {
        this.safeEventDelegator = safeEventDelegator;
    }

    public void execute(ServiceEvent serviceEvent) throws InstantiationException, DelegationFailure {
        attemptToDelegateEvent(serviceEvent, 10);

    }

    private void attemptToDelegateEvent(ServiceEvent serviceEvent, int maxTries) throws InstantiationException, DelegationFailure {
        safeEventDelegator.safeDelegation(serviceEvent, maxTries);
    }

    @Override
    public void execute(Object amqpEvent) throws InstantiationException, DelegationFailure {
        if (amqpEvent instanceof ServiceEvent) {
            execute(((ServiceEvent) amqpEvent));
        }

    }
}
