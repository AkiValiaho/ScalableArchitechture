package com.akivaliaho.domain;

import com.akivaliaho.ByteTools;
import com.akivaliaho.ServiceEventDto;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Created by akivv on 21.7.2017.
 */
public class ServiceEvent implements Serializable {
    private final ByteTools byteTools;
    @Getter @Setter
    private String originalEventName;
    @Getter @Setter
    private Object[] originalParameters;
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private Object[] parameters;
    @Getter
    @Setter
    private String eventName;


    public ServiceEvent(Exchange exchange, ByteTools byteTools) throws IOException, ClassNotFoundException{
        //Convert the exchange to a service event
        convertExchangeToServiceEvent(exchange);
        this.byteTools = byteTools;
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

    public ServiceEventDto createDto() {
        return new ServiceEventDto(originalEventName, originalParameters, id, parameters, eventName);
    }
}