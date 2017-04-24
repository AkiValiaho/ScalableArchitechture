package com.akivaliaho.service.events;

import com.akivaliaho.event.ServiceEvent;

public class CalculateSuperHardSumEvent extends ServiceEvent {
    public CalculateSuperHardSumEvent(Integer number1, Integer number2) {
        saveEvent(CalculateSuperHardSumEvent.class.getName(), number1, number2);
    }
}