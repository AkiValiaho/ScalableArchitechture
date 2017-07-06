package com.akivaliaho;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by akivv on 6.7.2017.
 */
public class DomainEventBuilder {
    private DomainEvent domainEvent;
    private boolean isResult;
    private String originalEventName;
    private Object[] originalParameters;

    public DomainEventBuilder setDomainEvent(DomainEvent serviceEvent) {
        this.domainEvent = serviceEvent;
        return this;
    }

    public DomainEventBuilder isResult() {
        this.isResult = true;
        return this;
    }

    public Optional<BuilderStrategy> build() {
        List<Object> builderFieldArray = createBuilderFieldArray();
        DomainEventBuilderStrategyFactory domainEventBuilderStrategyFactory = new DomainEventBuilderStrategyFactory();
        Optional<BuilderStrategy> builderStrategy = domainEventBuilderStrategyFactory.resolveBuilderStrategy(this.getClass());
        if (builderStrategy.isPresent()) {
            return Optional.of(builderStrategy.get());
        }
        return Optional.empty();
    }

    private List<Object> createBuilderFieldArray() {
        List<Object> objectList = new ArrayList<>();
        objectList.add(domainEvent);
        objectList.add(isResult);
        objectList.add(originalEventName);
        objectList.add(originalParameters);
        return objectList;
    }

    public DomainEventBuilder setOriginalEventName(String originalEventName) {
        //This should be a result type of event
        checkArgument(isResult);
        this.originalEventName = originalEventName;
        return this;
    }

    public DomainEventBuilder setOriginalParameters(Object[] originalParameters) {
        this.originalParameters = originalParameters;
        return this;
    }
}
