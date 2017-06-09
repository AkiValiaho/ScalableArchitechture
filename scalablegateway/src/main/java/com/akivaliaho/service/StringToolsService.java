package com.akivaliaho.service;

import com.akivaliaho.AppendStringsEvent;
import com.akivaliaho.AppendStringsEventResult;
import com.akivaliaho.config.annotations.Interest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by akivv on 9.5.2017.
 */
@Service
public class StringToolsService {
    @Interest(receives = AppendStringsEventResult.class, emits = AppendStringsEvent.class)
    public DeferredResult<String> appendStrings(String one, String two) {
        return null;
    }
}
