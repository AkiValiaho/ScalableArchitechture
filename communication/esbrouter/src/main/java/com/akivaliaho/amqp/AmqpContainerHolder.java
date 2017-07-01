package com.akivaliaho.amqp;

import com.akivaliaho.ServiceEvent;
import com.akivaliaho.amqp.eventstrategy.AmqpEventStrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by akivv on 1.7.2017.
 */
@Component
@Slf4j
public class AmqpContainerHolder {

    private final AmqpEventStrategyHandler amqpEventStrategyHandler;

    public AmqpContainerHolder(AmqpEventStrategyHandler amqpEventStrategyHandler) {
        this.amqpEventStrategyHandler = amqpEventStrategyHandler;
    }

    public void registerContainer(ConnectionFactory connectionFactory, Queue serviceQueue) {
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer(connectionFactory);
        Object listener = new Object() {
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
                    amqpEventStrategyHandler.executeIncomingAmqpCommand(foo);
                }
            }

            public void handleMessage(Object object) {
                //Something is wrong because this is not an event
                log.error("Message received is malformed: {}", object);
            }
        };
        MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
        container.setMessageListener(adapter);
        container.setQueueNames(serviceQueue.getName());
        container.start();
    }
}
