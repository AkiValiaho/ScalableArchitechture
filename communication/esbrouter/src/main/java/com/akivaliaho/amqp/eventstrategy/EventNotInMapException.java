package com.akivaliaho.amqp.eventstrategy;

/**
 * Created by akivv on 5.7.2017.
 */
public class EventNotInMapException extends Exception {
    private final String eventName;

    public EventNotInMapException(String eventName) {
        this.eventName = eventName;
    }
}
