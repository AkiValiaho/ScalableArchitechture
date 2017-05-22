package com.akivaliaho.event;

import com.akivaliaho.ServiceEvent;
import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by vagrant on 4/28/17.
 */
public class EventInterestHolder {
    @Getter
    @Setter
    private HashMap<ServiceEvent, List<String>> eventInterestMap;

    public EventInterestHolder() {
        this.eventInterestMap = new HashMap<>();
    }

    public List<String> getInterestedParties(ServiceEvent serviceEvent) {
        return eventInterestMap.get(serviceEvent);
    }

    public void registerInterests(ServiceEvent serviceEvent) {
        checkRegisterInterestsPreconditions(serviceEvent);
        List<ServiceEvent> serviceEventList = (List<ServiceEvent>) serviceEvent.getParameters()[0];
        String serviceRoutingKey = (String) serviceEvent.getEventParams().getParams()[1];
        serviceEventList.stream()
                .forEach(event -> {
                    if (eventInterestMap.containsKey(event)) {
                        addRoutingkeyToExistingEvent(serviceRoutingKey, event);
                    } else {
                        createNewEventAndRoutingKey(serviceRoutingKey, event);
                    }
                });
    }

    private void createNewEventAndRoutingKey(String serviceRoutingKey, ServiceEvent event) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(serviceRoutingKey);
        eventInterestMap.put(event, strings);
    }

    private void addRoutingkeyToExistingEvent(String serviceRoutingKey, ServiceEvent event) {
        List<String> strings = eventInterestMap.get(event);
        if (!strings.contains(serviceRoutingKey)) {
            strings.add(serviceRoutingKey);
        }
    }

    private void checkRegisterInterestsPreconditions(ServiceEvent serviceEvent) {
        checkNotNull(serviceEvent);
        checkArgument(!Strings.isNullOrEmpty(serviceEvent.getEventName()));
        TypeToken serviceCheck = new TypeToken<List<ServiceEvent>>() {
        };
        Object[] parameters = serviceEvent.getParameters();
        //Check that there actually are two parameters, the parameter list and the service routing key for communication purposes
        if (parameters != null && parameters.length > 1) {
            checkArgument(parameters[0].getClass().isAssignableFrom(ArrayList.class));
            ArrayList parameter = (ArrayList) parameters[0];
            checkArgument(parameter.get(0).getClass().isAssignableFrom(ServiceEvent.class));
        } else {
            throw new IllegalArgumentException("No arguments given for the interest event");
        }
    }
}
