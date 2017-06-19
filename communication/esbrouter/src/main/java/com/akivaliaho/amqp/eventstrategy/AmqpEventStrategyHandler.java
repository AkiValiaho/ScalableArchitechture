package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.ConfigurationPollEventResult;
import com.akivaliaho.RequestInterestedPartiesEvent;
import com.akivaliaho.ServiceEvent;
import com.akivaliaho.ServiceEventResult;
import com.akivaliaho.event.AsyncQueue;
import com.akivaliaho.event.LocalEventDelegator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vagrant on 6/19/17.
 */
@Component
public class AmqpEventStrategyHandler {
    private Map<String, Strategy> strategyMap;
    private final LocalEventDelegator localEventDelegator;
    private final AsyncQueue asyncQueue;
    @Autowired
    public AmqpEventStrategyHandler(LocalEventDelegator localEventDelegator, AsyncQueue asyncQueue) {
        initStrategies(localEventDelegator);
        this.localEventDelegator = localEventDelegator;
        this.asyncQueue = asyncQueue;
    }

    private void initStrategies(LocalEventDelegator localEventDelegator) {
        this.strategyMap = new HashMap<>();
        this.strategyMap.put(ConfigurationPollEventResult.class.getCanonicalName(), new ConfigurationPollEventResultStrategy(localEventDelegator));
        this.strategyMap.put(RequestInterestedPartiesEvent.class.getCanonicalName(), new RequestInterestedPartiesEventStrategy(localEventDelegator));
    }

    public void executeCommand(ServiceEvent foo) throws InstantiationException {
    Strategy strategy = strategyMap.get(foo.getClass().getCanonicalName());
        if (strategy != null) {
            strategy.execute(foo);
        } else {
            if (foo.getEventName().toLowerCase().contains("result")) {
                asyncQueue.solveResult(((ServiceEventResult) foo));
            } else {
                localEventDelegator.delegateEvent(foo);
            }
        }
    }
}
