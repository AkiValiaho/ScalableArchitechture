package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.ConfigurationPollEventResult;
import com.akivaliaho.RequestInterestedPartiesEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created by vagrant on 6/21/17.
 */
public class AmqpEventStrategyHolderTest {
    private AmqpEventStrategyHolder amqpEventStrategyHolder;
    private Strategy defaultServiceEventStrategy;
    private Strategy configurationPollStrategy;
    private Strategy requestInterestedPartiesStrategy;


    @Before
    public void before() {
        defaultServiceEventStrategy = mock(DefaultServiceEventStrategy.class);
        configurationPollStrategy = mock(ConfigurationPollEventResultStrategy.class);
        requestInterestedPartiesStrategy = mock(RequestInterestedPartiesEventStrategy.class);
        this.amqpEventStrategyHolder = new AmqpEventStrategyHolder();
    }

    @Test
    public void constructorTest() {
        AmqpEventStrategyHolder amqpEventStrategyHolder = new AmqpEventStrategyHolder(mock(Strategy.class), mock(Strategy.class), mock(Strategy.class));
        //Should call init strategies
        assertTrue(amqpEventStrategyHolder.getStrategyMap().size() == 2);
    }

    @Test
    public void initStrategies() throws Exception {
        amqpEventStrategyHolder.initStrategies();
        assertTrue(amqpEventStrategyHolder.getStrategyMap().size() == 2);
    }

    @Test
    public void getStrategy() throws Exception {
        //DefaultServiceEventStrategy
        this.amqpEventStrategyHolder = new AmqpEventStrategyHolder(defaultServiceEventStrategy, configurationPollStrategy, requestInterestedPartiesStrategy);
        Strategy<Object> strategy = this.amqpEventStrategyHolder.getStrategy();
        assertEquals(strategy, defaultServiceEventStrategy);
    }

    @Test
    public void getStrategyAny() throws Exception {
        this.amqpEventStrategyHolder = new AmqpEventStrategyHolder(defaultServiceEventStrategy, configurationPollStrategy, requestInterestedPartiesStrategy);
        Strategy configurationPollStrategyReturned = this.amqpEventStrategyHolder.getStrategy(ConfigurationPollEventResult.class.getCanonicalName());
        assertEquals(configurationPollStrategy, configurationPollStrategyReturned);
        Strategy requestInterestedPartiesReturned = this.amqpEventStrategyHolder.getStrategy(RequestInterestedPartiesEvent.class.getCanonicalName());
        assertEquals(requestInterestedPartiesStrategy, requestInterestedPartiesReturned);
    }

}