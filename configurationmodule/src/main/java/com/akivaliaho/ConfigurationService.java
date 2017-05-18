package com.akivaliaho;

import com.akivaliaho.config.annotations.Interest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by vagrant on 5/18/17.
 */
@Component
public class ConfigurationService {
    private Map<ServiceEvent, List<String>> eventInterestMap;

    @Interest(value = ConfigurationPollEventResult.class, emit = ConfigurationPollEvent.class)
    public void sendInterestPoll(Map<ServiceEvent, List<String>> freshEventInterestMap) {
        this.eventInterestMap = freshEventInterestMap;
    }
}
