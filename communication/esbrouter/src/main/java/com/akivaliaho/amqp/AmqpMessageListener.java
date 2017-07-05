package com.akivaliaho.amqp;

import com.akivaliaho.ServiceEvent;
import com.akivaliaho.amqp.eventstrategy.AmqpEventStrategyHandler;
import com.akivaliaho.amqp.eventstrategy.DelegationFailure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@Component
@Slf4j
public class AmqpMessageListener {

    private final AmqpEventStrategyHandler amqpEventStrategyHandler;

    @Autowired
    public AmqpMessageListener(AmqpEventStrategyHandler amqpEventStrategyHandler) {
        this.amqpEventStrategyHandler = amqpEventStrategyHandler;
    }

    Object getAmqpMessageListener() {
        return new Object() {
            public void handleMessage(byte[] bytes) throws InstantiationException {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    ServiceEvent serviceEvent = (ServiceEvent) objectInputStream.readObject();
                    handleMessage(serviceEvent);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            public void handleMessage(ServiceEvent foo) throws InstantiationException {
                log.error("Got service-event: {}", foo.getEventName());
                if (foo != null) {
                    try {
                        amqpEventStrategyHandler.executeIncomingAmqpCommand(foo);
                    } catch (DelegationFailure e) {
                        //We can't do anything about this, because rabbitmq will just try to resend the message
                        throw new RuntimeException(e);
                    }
                }
            }

            public void handleMessage(Object object) {
                //Something is wrong because this is not an event
                log.error("Message received is malformed: {}", object);
            }
        };
    }
}