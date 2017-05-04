package com.akivaliaho;

import com.akivaliaho.event.ServiceEventResult;

/**
 * Created by akivv on 1.5.2017.
 */
public class CalculateSuperHardSumResultEvent extends ServiceEventResult {

    public CalculateSuperHardSumResultEvent(int i) {
        saveEvent(this.getClass().getName(), i);
    }
}
