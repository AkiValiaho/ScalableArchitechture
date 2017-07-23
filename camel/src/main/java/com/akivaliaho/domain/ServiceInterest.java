package com.akivaliaho.domain;

import com.akivaliaho.ServiceEventDto;

/**
 * Created by akivv on 23.7.2017.
 */
public class ServiceInterest {

    private final String eventName;

    public ServiceInterest(ServiceEventDto dto) {
        this.eventName = dto.getEventName();
    }
}
