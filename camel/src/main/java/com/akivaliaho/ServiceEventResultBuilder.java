package com.akivaliaho;

/**
 * Created by akivv on 18.5.2017.
 */
public class ServiceEventResultBuilder {
    private static ServiceEventResultBuilder builder;
    private String originalEventName;
    private Object[] originalParameters;
    private ServiceEvent serviceEvent;

    public static ServiceEventResultBuilder getBuilder() {
        return new ServiceEventResultBuilder();
    }

    public ServiceEventResultBuilder originalEventName(String originalEventName) {
        this.originalEventName = originalEventName;
        return this;
    }

    public ServiceEventResultBuilder originalParameters(Object[] originalParameters) {
        this.originalParameters = originalParameters;
        return this;
    }

    public ServiceEventResult build() {
        ServiceEventResult serviceEventResult = new ServiceEventResult(serviceEvent);
        serviceEventResult.setOriginalEventName(originalEventName);
        serviceEventResult.setOriginalParameters(originalParameters);
        return serviceEventResult;
    }

    public ServiceEventResultBuilder serviceEvent(ServiceEvent serviceEvent) {
        this.serviceEvent = serviceEvent;
        return this;
    }
}
