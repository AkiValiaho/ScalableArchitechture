package com.akivaliaho;

public class CalculateSuperHardSumEvent extends ServiceEvent {
    public CalculateSuperHardSumEvent(Integer number1, Integer number2) {
        saveEvent(CalculateSuperHardSumEvent.class.getName(), number1, number2);
    }
}