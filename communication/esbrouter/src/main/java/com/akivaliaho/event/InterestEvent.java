package com.akivaliaho.event;

import com.akivaliaho.ServiceEvent;
import com.akivaliaho.ServiceEventDto;

import java.util.List;

/**
 * Created by vagrant on 4/28/17.
 */
public class InterestEvent extends ServiceEvent {
    public InterestEvent(List<ServiceEventDto> eventInterests, String serviceName) {
        saveEvent("declarationOfInterests", eventInterests, serviceName);
    }
}
