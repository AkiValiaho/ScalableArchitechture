package com.akivaliaho.service;

import com.akivaliaho.amqp.EventUtil;
import com.akivaliaho.config.annotations.Interest;
import com.akivaliaho.service.events.CalculateHardSumEvent;
import com.akivaliaho.service.events.CalculateSuperHardSumEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by vagrant on 4/5/17.
 */
@Service
public class CalculationService extends BaseService {
    @Autowired
    EventUtil eventUtil;

    @Async
    @Interest(emit = CalculateHardSumEvent.class)
    public void doHardCalculation(Integer number1, Integer number2) {
        eventUtil.publishEvent(
                new CalculateHardSumEvent(number1, number2)
        );
    }

    @Async
    @Interest(value = "com.akivaliaho.CalculateSuperHardSumResultEvent", emit = CalculateSuperHardSumEvent.class)
    public void doSuperHardcalculation(Integer number1, Integer number32) {
        eventUtil.publishEvent(new CalculateSuperHardSumEvent(number1, number32));
    }

}
