package com.akivaliaho.event;

import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by akivv on 23.4.2017.
 */
@Component
public class LocalEventDelegator {
    private Map<String, Method> interestMap;


    public void delegateEvent(ServiceEvent foo) {
        Method method = interestMap.get(foo.getEventName());
        //TODO Delegate event to proper event handler
    }

    public void pluginInterests(Map<String, Method> interestMap) {
        this.interestMap = interestMap;
    }
}
