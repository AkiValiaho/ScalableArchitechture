package com.akivaliaho.event;

import com.akivaliaho.ServiceEvent;
import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by vagrant on 4/28/17.
 */
@Slf4j
public class EventInterestRegistrer {
    private final EventAndRoutingKeyHolder eventAndRoutingKeyHolder;


    public EventInterestRegistrer(EventAndRoutingKeyHolder eventAndRoutingKeyHolder) {
        this.eventAndRoutingKeyHolder = eventAndRoutingKeyHolder;
    }

    public List<String> getInterestedParties(ServiceEvent serviceEvent) {
        //TODO Refactor this brittle piece of logic somewhere more general (eg. InterestFlowControl-module)
        if (!serviceEvent.getEventName().equals("declarationOfInterests") && !serviceEvent.getEventName().equals("com.akivaliaho.RequestInterestedPartiesEventResult")) {
            log.info("Trying to get interested parties with service event: {}", serviceEvent.getEventName());
            List<String> strings = eventAndRoutingKeyHolder.get(serviceEvent);
            if (strings == null || strings.isEmpty()) {
                //Wait for a few seconds
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return strings;
        }
        return null;
    }

    public void registerPollInterestResults(ServiceEvent serviceEvent) {
        //TODO Fairly simple, but the whole Interest-contraception needs some additional thought
        HashMap<ServiceEvent, List<String>> serviceEventListHashMap = (HashMap<ServiceEvent, List<String>>) serviceEvent.getParameters()[0];
        //If the list is null configuration module has not yet received the interests from camel (eg. first start)
        //Lets not override anything
        if (serviceEventListHashMap != null) {
            HashMap<ServiceEvent, List<String>> eventInterestMap = eventAndRoutingKeyHolder.getEventInterestMap();
            eventInterestMap.putAll(serviceEventListHashMap);
        }
    }

    public void registerInterests(ServiceEvent serviceEvent) {
        checkRegisterInterestsPreconditions(serviceEvent);
        registerAfterCheckPassed(serviceEvent);
    }

    private void registerAfterCheckPassed(ServiceEvent serviceEvent) {
        List<ServiceEvent> serviceEventList = (List<ServiceEvent>) serviceEvent.getParameters()[0];
        //Number 1 is the routing key parameter
        String serviceRoutingKey = (String) serviceEvent.getEventParams().getParams()[1];
        log.info("Registering events: {} to routing key: {}", concatEvents(serviceEventList), serviceRoutingKey);
        serviceEventList.stream()
                .forEach(event -> {
                    eventAndRoutingKeyHolder.handleEvent(event, serviceRoutingKey);
                });
    }

    private String concatEvents(List<ServiceEvent> serviceEventList) {
        StringBuilder builder = new StringBuilder();
        serviceEventList.stream()
                .forEach(serviceEvent -> builder.append(serviceEvent.getEventName() + " "));
        return builder.toString();

    }

    private void checkRegisterInterestsPreconditions(ServiceEvent serviceEvent) {
        checkNotNull(serviceEvent);
        checkArgument(!Strings.isNullOrEmpty(serviceEvent.getEventName()));
        TypeToken serviceCheck = new TypeToken<List<ServiceEvent>>() {
        };
        Object[] parameters = serviceEvent.getParameters();
        //TODO Way too damn brittle stuff here, better refactor this out
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
