package com.akivaliaho.amqp;

import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import static org.mockito.Mockito.mock;

/**
 * Created by akivv on 1.7.2017.
 */
public class AmqpContainerHolderTest {
    private AmqpContainerHolder amqpContainerHolder;
    private ConnectionFactory connectionFactoryMock;
    private Queue serviceQueueMock;

    @Before
    public void setUp() throws Exception {
        //TODO Write test
        AmqpMessageListener amqpMessageListener = mock(AmqpMessageListener.class);
        this.amqpContainerHolder = new AmqpContainerHolder(amqpMessageListener);
        connectionFactoryMock = mock(ConnectionFactory.class);
        serviceQueueMock = mock(Queue.class);
    }

    @Test(expected = NullPointerException.class)
    public void registerContainerConnectionFactoryNull() throws Exception {
        this.amqpContainerHolder.registerContainer(null, serviceQueueMock);
    }

    @Test(expected = NullPointerException.class)
    public void registerContainerServiceQueueNull() {
        this.amqpContainerHolder.registerContainer(connectionFactoryMock, null);

    }

}