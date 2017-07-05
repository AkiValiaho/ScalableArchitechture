package com.akivaliaho.amqp.eventstrategy;

/**
 * Created by vagrant on 6/19/17.
 */
public interface Strategy <E> {
    public void execute(E amqpEvent) throws InstantiationException, DelegationFailure;
}
