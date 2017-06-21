package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.ServiceEvent;
import com.akivaliaho.event.AsyncQueue;
import com.akivaliaho.event.LocalEventDelegator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by vagrant on 6/21/17.
 */
@Component
public class DefaultServiceEventStrategy implements Strategy<Object>, AMQPExecuting {

    private final AsyncQueue asyncQueue;
    private final LocalEventDelegator localEventDelegator;

    @Autowired
    public DefaultServiceEventStrategy(AsyncQueue asyncQueue, LocalEventDelegator localEventDelegator) {
        this.asyncQueue =asyncQueue;
        this.localEventDelegator = localEventDelegator;
    }

    @Override
    public void execute(Object amqpEvent) throws InstantiationException {
        if (amqpEvent instanceof ServiceEvent) {
            execute(((ServiceEvent) amqpEvent));
        }
    }

    @Override
    public void execute(ServiceEvent foo) throws InstantiationException {
        if (foo.getEventName().toLowerCase().contains("result")) {
            asyncQueue.solveResult(foo);
        } else {
            localEventDelegator.delegateEvent(foo);
        }
    }
}
