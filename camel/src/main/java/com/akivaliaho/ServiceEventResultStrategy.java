package com.akivaliaho;

/**
 * Created by akivv on 6.7.2017.
 */
public class ServiceEventResultStrategy implements BuilderStrategy {
    private boolean isResultType;
    private DomainEvent domainevent;

    public ServiceEventResultStrategy() {
    }

    @Override
    public DomainEvent buildDomainEvent() {
        return new ServiceEventResult(domainevent);
    }
}
