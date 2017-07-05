package com.akivaliaho.amqp.eventstrategy;

/**
 * Created by vagrant on 6/21/17.
 */
public interface AMQPExecuting {
    public void execute(DomainEvent serviceEvent) throws InstantiationException, DelegationFailure;
}
