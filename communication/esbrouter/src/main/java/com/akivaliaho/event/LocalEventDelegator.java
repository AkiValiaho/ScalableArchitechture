package com.akivaliaho.event;

import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.PostConstruct;

/**
 * Created by akivv on 23.4.2017.
 */
@Component
public class LocalEventDelegator {
    @PostConstruct
    public void init() {
        //TODO Search through the classpath for an event handler to invoke
    }

    public void delegateEvent(ServiceEvent foo) {
        throw new NotImplementedException();
        //TODO Do Test
    }
}
