package com.akivaliaho;

/**
 * Created by akivv on 7.6.2017.
 */
public class RequestInterestedPartiesEvent extends ServiceEvent {
    public RequestInterestedPartiesEvent() {
        saveEvent(RequestInterestedPartiesEvent.class.getName(), null);
    }
}
