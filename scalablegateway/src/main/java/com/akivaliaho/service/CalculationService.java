package com.akivaliaho.service;

import com.akivaliaho.CalculateSuperHardSumResultEvent;
import com.akivaliaho.config.annotations.Interest;
import com.akivaliaho.CalculateHardSumEvent;
import com.akivaliaho.CalculateSuperHardSumEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by vagrant on 4/5/17.
 */
@Service
public class CalculationService {
    @Interest(emits = CalculateHardSumEvent.class)
    public DeferredResult<Integer> doHardCalculation(Integer number1, Integer number2) {
        return null;
    }

    @Interest(receives = CalculateSuperHardSumResultEvent.class, emits = CalculateSuperHardSumEvent.class)
    public DeferredResult<Integer> doSuperHardcalculation(Integer number1, Integer number32) {
        return null;
    }
}
