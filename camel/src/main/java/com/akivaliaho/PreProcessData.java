package com.akivaliaho;

import lombok.Getter;

import java.util.List;

/**
 * Created by akivv on 18.5.2017.
 */
public class PreProcessData {
    @Getter
    private final List<String> interestedParties;
    @Getter
    private final ServiceEventResult serviceEventResult;
    @Getter
    private final ServiceEvent serviceEvent;

    public PreProcessData(List<String> interestedParties, ServiceEventResult serviceEventResult, ServiceEvent serviceEvent) {
        this.interestedParties = interestedParties;
        this.serviceEventResult = serviceEventResult;
        this.serviceEvent = serviceEvent;
    }
}
