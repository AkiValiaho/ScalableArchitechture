package com.akivaliaho.domain;

import com.akivaliaho.ServiceEventDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Value-object that holds knowledge of all serviceEvents that are of interest to some
 * service.
 * Created by akivv on 23.7.2017.
 */
public final class InterestHolder {

    private Map<String, List<ServiceInterest>> interestMap;

    public InterestHolder() {

    }

    public InterestHolder(Object[] parameters) {
        registerInterests(parameters);

    }

    public void registerInterests(Object[] parameters) {
        checkParameters(parameters);

        ArrayList<ServiceEventDto> trueTypeList = ((ArrayList<ServiceEventDto>) parameters[0]);
        String interestedService = (String) parameters[1];

        //Set all these events to the interest map
        interestMap = new HashMap<>();
        interestMap.put(interestedService, convertDto(trueTypeList));
    }

    private List<ServiceInterest> convertDto(ArrayList<ServiceEventDto> trueTypeList) {
        return trueTypeList.stream()
                .map(ServiceInterest::new)
                .collect(Collectors.toList());
    }

    private void checkParameters(Object[] parameters) {
        //First parameter should be the list of events the given service is interested in
        checkArgument(parameters[0] instanceof ArrayList);
        //Second parameter should be the name of actual service that is interested on these events
        checkArgument(parameters[1] instanceof String);
        ArrayList<Object> parameter = (ArrayList<Object>) parameters[0];
        //Should only contain ServiceEvents
        checkArgument(onlyContainsServiceEvents(parameter));
    }

    private boolean onlyContainsServiceEvents(ArrayList<Object> parameter) {
        Boolean onlyContainsServiceEvents = true;
        for (Object o : parameter) {
            if (!(o instanceof ServiceEventDto)) {
                onlyContainsServiceEvents = false;
            }
        }
        return onlyContainsServiceEvents;
    }
}
