package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.DomainEvent;
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


    private void attemptToDelegateEvent(DomainEvent domainEvent, int maxTries) throws InstantiationException, DelegationFailure {
        safeEventDelegator.safeDelegation(domainEvent, maxTries);
    }

    @Override
    public void execute(Object amqpEvent) throws InstantiationException, DelegationFailure {
        if (amqpEvent instanceof DomainEvent) {
            attemptToDelegateEvent(((DomainEvent) amqpEvent), 10);
        }
    }
}
