package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.DomainEvent;
import com.akivaliaho.event.AsyncQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by vagrant on 6/21/17.
 */
@Component
public class DefaultServiceEventStrategy implements Strategy<Object>, AMQPExecuting {

    private final AsyncQueue asyncQueue;
    private final SafeEventDelegator safeEventDelegator;

    @Autowired
    public DefaultServiceEventStrategy(AsyncQueue asyncQueue, SafeEventDelegator safeEventDelegator) {
        this.asyncQueue =asyncQueue;
        this.safeEventDelegator = safeEventDelegator;
    }

    @Override
    public void execute(Object foo) throws InstantiationException, DelegationFailure {
        if (foo instanceof DomainEvent) {
            if (((DomainEvent) foo).getEventName().toLowerCase().contains("result")) {
                asyncQueue.solveResult(foo);
            } else {
                //It's not a result but an event, delegate it to a method that's interested
                safeEventDelegator.safeDelegation(((DomainEvent) foo), 10);
            }
        }
    }
}
