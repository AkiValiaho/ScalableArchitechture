package com.akivaliaho;

import org.apache.camel.spi.ManagementStrategy;

/**
 * Created by akivv on 8.6.2017.
 */
public class EventNotifierRegisterer {
    private final RoutesReadyEventNotifier routesReadyEventNotifier;

    public EventNotifierRegisterer(RoutesReadyEventNotifier routesReadyEventNotifier) {
        this.routesReadyEventNotifier = routesReadyEventNotifier;
    }

    public void registerEventNotifierToContext(ManagementStrategy managementStrategy) {
        managementStrategy.addEventNotifier(routesReadyEventNotifier);
    }
}
