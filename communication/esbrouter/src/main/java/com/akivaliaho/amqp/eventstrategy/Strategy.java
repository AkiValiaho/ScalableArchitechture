package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.ServiceEvent;

/**
 * Created by vagrant on 6/19/17.
 */
public interface Strategy {
    public void execute(ServiceEvent serviceEvent) throws InstantiationException;
}
