package com.akivaliaho.service.events;

import com.akivaliaho.event.ServiceEvent;

/**
 * Created by akivv on 23.4.2017.
 */
public class CalculateHardSumEvent extends ServiceEvent {
    public CalculateHardSumEvent(Integer number1, Integer number2) {
        saveEvent(CalculateHardSumEvent.class.getName(), number1, number2);
    }
}
