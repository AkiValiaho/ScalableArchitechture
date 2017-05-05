package com.akivaliaho.service;

import com.akivaliaho.config.annotations.Interest;
import com.akivaliaho.service.events.CalculateHardSumEvent;

/**
 * Created by akivv on 5.5.2017.
 */
public class TestService extends BaseService {
    @Interest(emit = CalculateHardSumEvent.class)
    public void something() {

    }
}
