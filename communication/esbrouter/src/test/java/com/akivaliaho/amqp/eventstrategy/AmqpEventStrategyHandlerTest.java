package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.ServiceEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.Times;

import static org.mockito.Mockito.*;

/**
 * Created by vagrant on 6/21/17.
 */
public class AmqpEventStrategyHandlerTest {
    private AmqpEventStrategyHandler amqpEventStrategyHandler;
    private AmqpEventStrategyHolder amqpEventStrategyHolder;
    //TODO Write test

    @Before
    public void init() {
        amqpEventStrategyHolder = mock(AmqpEventStrategyHolder.class);
        this.amqpEventStrategyHandler = new AmqpEventStrategyHandler(amqpEventStrategyHolder);
    }

    @Test
    public void executeIncomingAmqpCommand() throws Exception {
        Strategy strategyMock = mock(DefaultServiceEventStrategy.class);
        when(amqpEventStrategyHolder.getStrategy("com.akivaliaho.ServiceEvent"))
                .thenReturn(strategyMock);
        ServiceEvent serviceEvent = new ServiceEvent();
        amqpEventStrategyHandler.executeIncomingAmqpCommand(serviceEvent);
        verify(amqpEventStrategyHolder, new Times(1)).getStrategy("com.akivaliaho.ServiceEvent");
        verify(strategyMock, new Times(1)).execute(serviceEvent);
    }

    @Test
    public void executeIncomingAMqpCommandDefaultStrategy() throws Exception {
        Strategy strategyMock = mock(DefaultServiceEventStrategy.class);
        when(amqpEventStrategyHolder.getStrategy(any())).thenReturn(null);
        //Validate default strategy is executed
        Strategy defaultStrategy = mock(DefaultServiceEventStrategy.class);
        when(amqpEventStrategyHolder.getStrategy()).thenReturn(defaultStrategy);
        ServiceEvent foo = new ServiceEvent();
        amqpEventStrategyHandler.executeIncomingAmqpCommand(foo);
        verify(defaultStrategy, new Times(1)).execute(foo);
    }

}