package com.akivaliaho.amqp;

import com.akivaliaho.amqp.eventstrategy.AmqpEventStrategyHandler;
import com.akivaliaho.config.ConfigEnum;
import com.akivaliaho.event.AsyncQueue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by akivv on 18.6.2017.
 */
@Component
@Slf4j
public class AmqpContainerInitialiser {
    private final AsyncQueue asyncQueue;
    private final AmqpEventStrategyHandler amqpEventStrategyHandler;
    private final AmqpContainerHolder amqpContainerHolder;
    @Getter
    private String serviceNameRoutingKey;

    @Autowired
    public AmqpContainerInitialiser(AmqpEventStrategyHandler amqpEventStrategyHandler, AsyncQueue asyncQueue, AmqpContainerHolder amqpContainerHolder) {
        this.amqpEventStrategyHandler = amqpEventStrategyHandler;
        this.asyncQueue = asyncQueue;
        this.amqpContainerHolder = amqpContainerHolder;
    }

    public void initAmqpContainer(Map<String, String> messagingConfiguration, RabbitAdmin admin, Queue serviceQueue, Queue toESBQueue, ConnectionFactory connectionFactory) {
        serviceNameRoutingKey = messagingConfiguration.get(ConfigEnum
                .SERVICEBASENAME);
        checkPreconditions(admin, serviceQueue, toESBQueue);
        messageContainerInit(connectionFactory, serviceQueue);
    }

    private void checkPreconditions(RabbitAdmin admin, Queue serviceQueue, Queue toESBQueue) {
        checkArgument(serviceNameRoutingKey != null && !isEmpty(serviceNameRoutingKey));
        checkNotNull(admin);
        checkNotNull(serviceQueue);
        checkNotNull(toESBQueue);
    }

    private void messageContainerInit(ConnectionFactory connectionFactory, Queue serviceQueue) {
        amqpContainerHolder.registerContainer(connectionFactory, serviceQueue);
    }


}
