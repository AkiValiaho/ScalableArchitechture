package com.akivaliaho.service.events;

import com.akivaliaho.event.ServiceEvent;

/**
 * Created by akivv on 9.5.2017.
 */
public class AppendStringsEvent extends ServiceEvent {
    public AppendStringsEvent(String one, String two) {
        saveEvent(AppendStringsEvent.class.getName(), one, two);
    }
}
