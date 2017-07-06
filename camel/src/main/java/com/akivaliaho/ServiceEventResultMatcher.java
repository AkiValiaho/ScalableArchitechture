package com.akivaliaho;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akivv on 6.7.2017.
 */
public class ServiceEventResultMatcher implements DomainEventCaseMatcher{
    private final List<Class<?>> types;
    private final FieldMatcherTool fieldMatcherTool;

    public ServiceEventResultMatcher() {
        this.types = new ArrayList<>();
        types.add(DomainEvent.class);
        types.add(Boolean.class);
        types.add(String.class);
        types.add(Object[].class);
        this.fieldMatcherTool = new FieldMatcherTool();
    }

    @Override
    public boolean attemptMatch(Class<?> matchedClass) {
        return fieldMatcherTool.attemptMatch(matchedClass, types);

    }
}
