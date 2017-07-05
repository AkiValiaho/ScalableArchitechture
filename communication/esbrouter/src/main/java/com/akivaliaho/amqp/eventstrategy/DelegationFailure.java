package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.DomainEvent;

/**
 * Created by akivv on 5.7.2017.
 */
public class DelegationFailure extends Exception {
    private final DomainEvent domainEvent;

    public DelegationFailure(DomainEvent domainEvent) {
        this.domainEvent = domainEvent;
    }

    @Override
    public String getMessage() {
        return "DomainEvent " + domainEvent.getEventName() + " was not found from the interestMap after some tries";
    }
}
