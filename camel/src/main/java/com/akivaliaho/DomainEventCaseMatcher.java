package com.akivaliaho;

/**
 * Created by akivv on 6.7.2017.
 */
public interface DomainEventCaseMatcher {
    boolean attemptMatch(Class<?> builderFieldArray);
}
