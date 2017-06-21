package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.ConfigurationPollEventResult;
import com.akivaliaho.RequestInterestedPartiesEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vagrant on 6/21/17.
 */
@Component
public class AmqpEventStrategyHolder {

    private final Strategy defaultServiceEventStrategy;
    private final Strategy configStrategy;
    private final Strategy requestStrategy;
    private Map<String, Strategy> strategyMap;

    @Autowired
    public AmqpEventStrategyHolder(@Qualifier("defaultServiceEventStrategy") Strategy defaultServiceEventStrategy,
                                   @Qualifier("configurationPollEventResultStrategy") Strategy configurationPollEventResultStrategy,
                                   @Qualifier("requestInterestedPartiesEventStrategy")Strategy requestInterestedPartiesEventStrategy) {
        this.defaultServiceEventStrategy = defaultServiceEventStrategy;
        this.configStrategy = configurationPollEventResultStrategy;
    this.requestStrategy = requestInterestedPartiesEventStrategy;
        initStrategies();
    }

    public void initStrategies() {
        strategyMap = new HashMap<>();
        strategyMap.put(ConfigurationPollEventResult.class.getCanonicalName(), configStrategy);
        strategyMap.put(RequestInterestedPartiesEvent.class.getCanonicalName(),requestStrategy);
    }

    public Strategy getStrategy(String canonicalName) {
        return strategyMap.get(canonicalName);
    }

    public Strategy<Object> getStrategy() {
        return defaultServiceEventStrategy;
    }
}
