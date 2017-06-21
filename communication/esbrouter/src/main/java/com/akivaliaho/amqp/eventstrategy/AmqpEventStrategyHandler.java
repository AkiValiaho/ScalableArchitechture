package com.akivaliaho.amqp.eventstrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by vagrant on 6/19/17.
 */
@Component
public class AmqpEventStrategyHandler {
    private final AmqpEventStrategyHolder amqpEventStrategyholder;
    @Autowired
    public AmqpEventStrategyHandler(AmqpEventStrategyHolder amqpEventStrategyHolder) {
        this.amqpEventStrategyholder = amqpEventStrategyHolder;
    }

    public void executeIncomingAmqpCommand(Object foo) throws InstantiationException {
        Strategy strategy = amqpEventStrategyholder.getStrategy(foo.getClass().getCanonicalName());
        if (strategy != null) {
            strategy.execute(foo);
        } else {
            amqpEventStrategyholder.getStrategy().execute(foo);
        }
    }
}
