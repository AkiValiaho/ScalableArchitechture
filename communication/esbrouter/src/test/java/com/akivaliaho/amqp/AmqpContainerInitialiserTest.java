package com.akivaliaho.amqp;

import com.akivaliaho.amqp.eventstrategy.AmqpEventStrategyHandler;
import com.akivaliaho.config.ConfigEnum;
import com.akivaliaho.event.AsyncQueue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.Times;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by vagrant on 6/21/17.
 */
public class AmqpContainerInitialiserTest {
    private AmqpContainerInitialiser amqpContainerInitialiser;
    private AmqpContainerHolder amqpContainerHolder;

    @Before
    public void before() {
        AmqpEventStrategyHandler amqpEventStrategyHandler = mock(AmqpEventStrategyHandler.class);
        AsyncQueue mock = mock(AsyncQueue.class);
        amqpContainerHolder = mock(AmqpContainerHolder.class);
        this.amqpContainerInitialiser = new AmqpContainerInitialiser(amqpEventStrategyHandler, mock, amqpContainerHolder);
    }

    @Test
    public void initAmqpContainer() throws Exception {
        Map<String, String> testMessagingConfiguration = new HashMap<>();
        testMessagingConfiguration.put(ConfigEnum.SERVICEBASENAME, "baseTestService");
        RabbitAdmin rabbitAdmin = mock(RabbitAdmin.class);
        Queue serviceQueue = mock(Queue.class);
        Queue esbQueue = mock(Queue.class);
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        this.amqpContainerInitialiser.initAmqpContainer(testMessagingConfiguration, rabbitAdmin, serviceQueue, esbQueue, connectionFactory);
        verify(amqpContainerHolder, new Times(1)).registerContainer(connectionFactory, serviceQueue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void initAmqpContainerNoBaseServiceName() throws Exception {
        Map<String, String> testMessagingConfiguration = new HashMap<>();
        RabbitAdmin rabbitAdmin = mock(RabbitAdmin.class);
        Queue serviceQueue = mock(Queue.class);
        Queue esbQueue = mock(Queue.class);
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        this.amqpContainerInitialiser.initAmqpContainer(testMessagingConfiguration, rabbitAdmin, serviceQueue, esbQueue, connectionFactory);
    }

    @Test(expected = NullPointerException.class)
    public void initAmqpContainerRabbitAdminNull() {
        Map<String, String> testMessagingConfiguration = new HashMap<>();
        testMessagingConfiguration.put(ConfigEnum.SERVICEBASENAME, "baseTestService");
        RabbitAdmin rabbitAdmin = null;
        Queue serviceQueue = mock(Queue.class);
        Queue esbQueue = mock(Queue.class);
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        this.amqpContainerInitialiser.initAmqpContainer(testMessagingConfiguration, rabbitAdmin, serviceQueue, esbQueue, connectionFactory);
    }

    @Test(expected = NullPointerException.class)
    public void initAmqpContainerServiceQueueNull() {
        Map<String, String> testMessagingConfiguration = new HashMap<>();
        testMessagingConfiguration.put(ConfigEnum.SERVICEBASENAME, "baseTestService");
        RabbitAdmin rabbitAdmin = mock(RabbitAdmin.class);
        Queue serviceQueue = null;
        Queue esbQueue = mock(Queue.class);
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        this.amqpContainerInitialiser.initAmqpContainer(testMessagingConfiguration, rabbitAdmin, serviceQueue, esbQueue, connectionFactory);
    }

    @Test(expected = NullPointerException.class)
    public void initAmqpEsbQueueNull() {
        Map<String, String> testMessagingConfiguration = new HashMap<>();
        testMessagingConfiguration.put(ConfigEnum.SERVICEBASENAME, "baseTestService");
        RabbitAdmin rabbitAdmin = mock(RabbitAdmin.class);
        Queue serviceQueue = mock(Queue.class);
        Queue esbQueue = null;
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        this.amqpContainerInitialiser.initAmqpContainer(testMessagingConfiguration, rabbitAdmin, serviceQueue, esbQueue, connectionFactory);
    }

    @Test
    public void getServiceNameRoutingKey() throws Exception {
    }

}