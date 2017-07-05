package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.ServiceEvent;
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
    public void execute(Object amqpEvent) throws InstantiationException, DelegationFailure {
        if (amqpEvent instanceof ServiceEvent) {
            execute(((ServiceEvent) amqpEvent));
        }
    }

    @Override
    public void execute(DomainEvent foo) throws InstantiationException, DelegationFailure {
        if (foo.getEventName().toLowerCase().contains("result")) {
            asyncQueue.solveResult(foo);
        } else {
            //It's not a result but an event, delegate it to a method that's interested
            safeEventDelegator.safeDelegation(foo, 10);
        }
    }
}
