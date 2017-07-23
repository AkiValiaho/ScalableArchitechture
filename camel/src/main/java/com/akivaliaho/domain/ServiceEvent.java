package com.akivaliaho.domain;

import com.akivaliaho.ByteTools;
import com.akivaliaho.ServiceEventDto;
import com.akivaliaho.services.application.AmqpConnection;
import lombok.NoArgsConstructor;
import org.apache.camel.Exchange;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by akivv on 21.7.2017.
 */
@NoArgsConstructor
public class ServiceEvent implements Serializable {
    private ByteTools byteTools;
    private AmqpConnection amqpConnection;
    private Exchange exchange;
    private String originalEventName;
    private Object[] originalParameters;
    private Long id;
    private Object[] parameters;
    private String eventName;
    private InterestHolder serviceEventInterestHolder;


    public ServiceEvent(Exchange exchange, ByteTools byteTools, AmqpConnection amqpConnection, InterestHolder interestHolder) throws IOException, ClassNotFoundException {
        checkNotNull(exchange);
        checkNotNull(byteTools);
        checkNotNull(amqpConnection);
        checkNotNull(interestHolder);
        this.byteTools = byteTools;
        this.amqpConnection = amqpConnection;
        this.exchange = exchange;
        this.serviceEventInterestHolder = interestHolder;
        //Convert the exchange to a service event
        convertExchangeToServiceEvent(exchange);
        //Handle this event (send to amqp, save interests... etc.)
        handleEvent();
    }

    private ServiceEventDto convertExchangeToServiceEvent(Exchange exchangeToConvert) throws IOException, ClassNotFoundException {
        ObjectInputStream convert = convert(exchangeToConvert);
        Object o = convert.readObject();
        if (o instanceof ServiceEventDto) {
            ServiceEventDto o1 = (ServiceEventDto) o;
            this.originalEventName = o1.getOriginalEventName();
            this.originalParameters = o1.getOriginalParameters();
            this.id = o1.getId();
            this.parameters = o1.getParameters();
            this.eventName = o1.getEventName();
            return (ServiceEventDto) o;
        }
        //No known way to convert Exchange body to ServiceEvent
        throw new IOException("No known way to convert Exchange to ServiceEvent");
    }

    private ObjectInputStream convert(Exchange exchange) throws IOException {
        return handleExchangeConversion(exchange);
    }

    private ObjectInputStream handleExchangeConversion(Exchange exchange) throws IOException {
        byte[] body = (byte[]) exchange.getIn().getBody();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ObjectInputStream(byteArrayInputStream);

    }

    public byte[] toBytes() throws IOException {
        return byteTools.objectToBytes(this);
    }

    public String getEvent() {
        return eventName;
    }

    private ServiceEventDto createDto() {
        return new ServiceEventDto(originalEventName, originalParameters, id, parameters, eventName);
    }

    private void handleEvent() throws IOException {
        if (eventName.equals("declarationOfInterests")) {
            //Register these interest declarations to the stateful holder
            serviceEventInterestHolder.registerInterests(parameters);
        } else {
            //Default case is when the ServiceEvent needs to be send through an AMQP connection
            sendServiceEvent();
        }
    }

    private void sendServiceEvent() throws IOException {
        amqpConnection.sendObject(exchange, createDto());
    }
}