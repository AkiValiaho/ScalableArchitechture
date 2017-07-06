package com.akivaliaho;

import java.util.Optional;

/**
 * Created by akivv on 18.5.2017.
 */
public class ServiceEventResultBuilder {
    private static ServiceEventResultBuilder builder;
    private String originalEventName;
    private Object[] originalParameters;
    private DomainEvent serviceEvent;

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

    public DomainEvent build() {
        Optional<BuilderStrategy> builderStrategy = buildDomainEvent();
        if (builderStrategy.isPresent()) {
            DomainEvent serviceEventResult = builderStrategy.get().buildDomainEvent();
            serviceEventResult.setOriginalEventName(originalEventName);
            serviceEventResult.setOriginalParameters(originalParameters);
            return serviceEventResult;
        }
        return null;
    }

    private Optional<BuilderStrategy> buildDomainEvent() {
        return new DomainEventBuilder()
                .setDomainEvent(serviceEvent)
                .isResult()
                .setOriginalEventName(originalEventName)
                .setOriginalParameters(originalParameters)
                .build();
    }

    public ServiceEventResultBuilder serviceEvent(ServiceEvent serviceEvent) {
        this.serviceEvent = serviceEvent;
        return this;
    }
}
