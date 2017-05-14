package com.akivaliaho.service;

import com.akivaliaho.config.annotations.Interest;
import com.akivaliaho.event.ServiceEvent;
import com.akivaliaho.service.events.CalculateHardSumEvent;
import com.akivaliaho.service.events.CalculateSuperHardSumEvent;
import org.springframework.stereotype.Service;

/**
 * Created by vagrant on 4/5/17.
 */
@Service
public class CalculationService extends BaseService {
    @Interest(emit = CalculateHardSumEvent.class)
    public CalculateHardSumEvent doHardCalculation(Integer number1, Integer number2) {
        return new CalculateHardSumEvent(number1, number2);
    }

    @Interest(value = "com.akivaliaho.CalculateSuperHardSumResultEvent", emit = CalculateSuperHardSumEvent.class)
    public ServiceEvent doSuperHardcalculation(Integer number1, Integer number32) {
        return
                new CalculateSuperHardSumEvent(number1, number32);
    }

}
