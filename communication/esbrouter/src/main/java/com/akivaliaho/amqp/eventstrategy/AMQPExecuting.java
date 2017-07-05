package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.ServiceEvent;

/**
 * Created by vagrant on 6/21/17.
 */
public interface AMQPExecuting {
    public void execute(ServiceEvent serviceEvent) throws InstantiationException, DelegationFailure;
}
