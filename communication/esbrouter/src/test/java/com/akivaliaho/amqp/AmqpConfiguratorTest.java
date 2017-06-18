package com.akivaliaho.amqp;

import com.akivaliaho.event.AsyncQueue;
import com.akivaliaho.event.LocalEventDelegator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by vagrant on 6/18/17.
 */
public class AmqpConfiguratorTest {
    private AmqpConfigurator amqpConfigurator;
    private AmqpEntitiesConstructor amqpEntitiesConstructor;
    private AsyncQueue asyncQueue;
    private LocalEventDelegator localEventDelegator;

    @Before
    public void init() {
        amqpEntitiesConstructor = mock(AmqpEntitiesConstructor.class);
        asyncQueue = mock(AsyncQueue.class);
        localEventDelegator = mock(LocalEventDelegator.class);
        this.amqpConfigurator = new AmqpConfigurator(localEventDelegator, asyncQueue, amqpEntitiesConstructor);
    }

    @Test
    public void configureAmqp() throws Exception {
        ESBRouter mock = mock(ESBRouter.class);
        when(amqpEntitiesConstructor.getServiceName()).thenReturn("hello world");
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        when(amqpEntitiesConstructor.getTemplate()).thenReturn(rabbitTemplate);
        amqpConfigurator.configureAmqp(mock);
        verify(amqpEntitiesConstructor, times(1)).constructAmqpEntities();
        verify(localEventDelegator, times(1)).setEventUtil(any());
        assertEquals(amqpEntitiesConstructor.getTemplate(), rabbitTemplate);
    }

}