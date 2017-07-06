package com.akivaliaho;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by akivv on 6.7.2017.
 */
public class DomainEventBuilderStrategyFactory {
    Map<DomainEventCaseMatcher, BuilderStrategy> domainEventCaseMatcherMap = new HashMap<>();
    private Class<?> resolvableClass;

    public DomainEventBuilderStrategyFactory() {
    this.domainEventCaseMatcherMap.put(new ServiceEventResultMatcher(), new ServiceEventResultStrategy());
    }

    public Optional<BuilderStrategy> resolveBuilderStrategy(Class<?> resolvableClass) {
        checkNotNull(resolvableClass);
        //try to match all the cases
        BuilderStrategy builderStrategy;
        Optional<Map.Entry<DomainEventCaseMatcher, BuilderStrategy>> first = domainEventCaseMatcherMap.entrySet().stream()
                .filter(matchCase -> matchCase.getKey().attemptMatch(resolvableClass))
                .findFirst();
        if (first.isPresent()) {
            return Optional.of(first.get().getValue());
        }
        return Optional.empty();

    }
}
