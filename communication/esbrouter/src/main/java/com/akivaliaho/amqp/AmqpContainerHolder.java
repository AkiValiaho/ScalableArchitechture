package com.akivaliaho.amqp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by akivv on 1.7.2017.
 */
@Component
@Slf4j
public class AmqpContainerHolder {

    private final AmqpMessageListener amqpMessageListener;
    private SimpleMessageListenerContainer container;

    public AmqpContainerHolder(AmqpMessageListener amqpMessageListener) {
        this.amqpMessageListener = amqpMessageListener;
    }

    public void registerContainer(ConnectionFactory connectionFactory, Queue serviceQueue) {
        checkNotNull(connectionFactory);
        checkNotNull(serviceQueue);
        container = new SimpleMessageListenerContainer(connectionFactory);
        Object listener = amqpMessageListener.getAmqpMessageListener();
        MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
        container.setMessageListener(adapter);
        container.setQueueNames(serviceQueue.getName());
        container.start();
    }

}
