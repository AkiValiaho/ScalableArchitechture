package com.akivaliaho.service;

import com.akivaliaho.*;
import com.akivaliaho.config.annotations.FieldInterest;
import com.akivaliaho.config.annotations.Interest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by vagrant on 5/18/17.
 */
@Component
public class ConfigurationService {
    @FieldInterest(event = RequestInterestedPartiesEventResult.class)
    private Map<ServiceEvent, List<String>> eventInterestMap;

    @Interest(receives = ConfigurationPollEventResult.class)
    public void receiveInterestPoll(Map<ServiceEvent, List<String>> freshEventInterestMap) {
        this.eventInterestMap = freshEventInterestMap;
    }

    @Interest(receives = RequestInterestedPartiesEvent.class, emits = RequestInterestedPartiesEventResult.class)
    public void sendInterests() {
    }
}
