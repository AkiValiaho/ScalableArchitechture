package com.akivaliaho;

import com.akivaliaho.config.annotations.Interest;

/**
 * Created by akivv on 1.5.2017.
 */
public class SuperCalculator {
    @Interest(value = "com.akivaliaho.service.events.CalculateSuperHardSumEvent")
    public CalculateSuperHardSumResultEvent calculateSuperHard(Integer one, Integer two) {
        int i = one + two;
        return new CalculateSuperHardSumResultEvent(i);
    }

}
