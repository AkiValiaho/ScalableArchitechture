package com.akivaliaho.service;

import com.akivaliaho.config.annotations.Interest;
import com.akivaliaho.service.events.AppendStringsEvent;
import org.springframework.stereotype.Service;

/**
 * Created by akivv on 9.5.2017.
 */
@Service
public class StringToolsService extends BaseService {
    @Interest(value = "com.akivaliaho.AppendStringsEventResult", emit = AppendStringsEvent.class)
    public AppendStringsEvent appendStrings(String one, String two) {
        return new AppendStringsEvent(one, two);
    }
}
