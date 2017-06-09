package com.akivaliaho.service;

import com.akivaliaho.config.annotations.Interest;
import com.akivaliaho.CalculateHardSumEvent;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by akivv on 5.5.2017.
 */
public class TestService {
    @Interest(emits = CalculateHardSumEvent.class)
    public DeferredResult<Integer> something() {
        return null;
    }
}
