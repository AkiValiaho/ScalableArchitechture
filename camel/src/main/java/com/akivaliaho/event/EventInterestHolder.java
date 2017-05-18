package com.akivaliaho.event;

import com.akivaliaho.ServiceEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vagrant on 4/28/17.
 */
public class EventInterestHolder {
    @Getter
    private final HashMap<ServiceEvent, List<String>> eventInterestMap;

    public EventInterestHolder() {
        this.eventInterestMap = new HashMap<>();
    }

    public List<String> getInterestedParties(ServiceEvent serviceEvent) {
        return eventInterestMap.get(serviceEvent);
    }

    public void registerInterests(ServiceEvent serviceEvent) {
        //TODO Test register interest
        List<ServiceEvent> serviceEventList = (List<ServiceEvent>) serviceEvent.getParameters()[0];
        String serviceRoutingKey = (String) serviceEvent.getEventParams().getParams()[1];
        serviceEventList.stream()
                .forEach(event -> {
                    if (eventInterestMap.containsKey(event)) {
                        List<String> strings = eventInterestMap.get(event);
                        if (!strings.contains(serviceRoutingKey)) {
                            strings.add(serviceRoutingKey);
                        }
                    } else {
                        ArrayList<String> strings = new ArrayList<>();
                        strings.add(serviceRoutingKey);
                        eventInterestMap.put(event, strings);
                    }
                });
    }
}
