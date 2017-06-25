package com.akivaliaho.amqp;

import com.akivaliaho.ServiceEvent;
import com.akivaliaho.amqp.eventstrategy.AmqpEventStrategyHandler;
import com.akivaliaho.config.ConfigEnum;
import com.akivaliaho.event.AsyncQueue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * Created by akivv on 18.6.2017.
 */
@Component
@Slf4j
public class AmqpContainerInitialiser {
    private final AsyncQueue asyncQueue;
    private final AmqpEventStrategyHandler amqpEventStrategyHandler;
    @Getter
    private String serviceNameRoutingKey;

    @Autowired
    public AmqpContainerInitialiser(AmqpEventStrategyHandler amqpEventStrategyHandler, AsyncQueue asyncQueue) {
        this.amqpEventStrategyHandler = amqpEventStrategyHandler;
        this.asyncQueue = asyncQueue;
    }

    public void initAmqpContainer(Map<String, String> messagingConfiguration, RabbitAdmin admin, Queue serviceQueue, Queue toESBQueue, ConnectionFactory connectionFactory) {
        serviceNameRoutingKey = messagingConfiguration.get(ConfigEnum
                .SERVICEBASENAME);
        messageContainerInit(connectionFactory, serviceQueue);
    }

    private void messageContainerInit(ConnectionFactory connectionFactory, Queue serviceQueue) {
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
