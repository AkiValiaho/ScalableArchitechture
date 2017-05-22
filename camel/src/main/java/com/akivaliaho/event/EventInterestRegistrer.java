package com.akivaliaho.event;

import com.akivaliaho.ServiceEvent;
import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by vagrant on 4/28/17.
 */
public class EventInterestRegistrer {
    private final EventAndRoutingKeyHolder eventAndRoutingKeyHolder;


    public EventInterestRegistrer(EventAndRoutingKeyHolder eventAndRoutingKeyHolder) {
        this.eventAndRoutingKeyHolder = eventAndRoutingKeyHolder;
    }

    public List<String> getInterestedParties(ServiceEvent serviceEvent) {
        return eventAndRoutingKeyHolder.get(serviceEvent);
    }

    public void registerInterests(ServiceEvent serviceEvent) {
        checkRegisterInterestsPreconditions(serviceEvent);
        List<ServiceEvent> serviceEventList = (List<ServiceEvent>) serviceEvent.getParameters()[0];
        //Number 1 is the routing key parameter
        String serviceRoutingKey = (String) serviceEvent.getEventParams().getParams()[1];
        serviceEventList.stream()
                .forEach(event -> {
                    eventAndRoutingKeyHolder.handleEvent(event, serviceRoutingKey);
                });
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

    public HashMap<ServiceEvent, List<String>> getEventInterestMap() {
        return eventAndRoutingKeyHolder.getEventInterestMap();
    }
}
