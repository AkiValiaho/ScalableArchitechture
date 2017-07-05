package com.akivaliaho.amqp.eventstrategy;

/**
 * Created by akivv on 5.7.2017.
 */
public interface DomainEvent {
    String getEventName();

    Object[] getParameters();
}
