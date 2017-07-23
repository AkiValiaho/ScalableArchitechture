package com.akivaliaho.services.domain;

import com.akivaliaho.domain.ServiceEvent;
import com.akivaliaho.services.application.AmqpConnection;
import org.apache.camel.Exchange;

import java.io.IOException;

/**
 * Domain Service that converts {@link ServiceEvent} to a
 * {@link com.akivaliaho.ServiceEventDto} before sending it through with {@link AmqpConnection}
 * Created by akivv on 23.7.2017.
 */
public class ServiceEventSendingTemplate {
    private final AmqpConnection amqpConnection;

    public ServiceEventSendingTemplate(AmqpConnection amqpConnection) {
        this.amqpConnection = amqpConnection;
    }
    public void sendServiceEvent(Exchange exchange, ServiceEvent serviceEvent) throws IOException {
        amqpConnection.sendObject(exchange, serviceEvent.createDto());
    }
}
