package com.akivaliaho.event;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by vagrant on 5/2/17.
 */
@Component
@Slf4j
public class AsyncQueue {
    List<DeferredResult<Object>> asyncList = new ArrayList<>();
    Map<Params, List<DeferredResult<?>>> hitList = new HashMap<>();

    public void addWaitingResult(DeferredResult<?> vAsyncResult, Object[] params) {
        if (hitList.containsKey(params)) {
            List<DeferredResult<?>> deferredResults = hitList.get(params);
            deferredResults.add(vAsyncResult);
        } else {
            ArrayList<DeferredResult<?>> deferredResults = new ArrayList<>();
            hitList.put(new Params(params), deferredResults);
        }

    }

    public void solveResult(ServiceEventResult foo) {
        Object o = foo.getParameters()[0];
        asyncList.stream()
                .forEach(result -> result.setResult(o));
        log.debug("Got ServiceEvent: {}", foo);
        asyncList.clear();
    }

    @EqualsAndHashCode
    private class Params {
        private final String paramsAsString;

        public Params(Object[] params) {
            String collect = Arrays.stream(params)
                    .map(param -> param.toString())
                    .collect(Collectors.joining(""));
            this.paramsAsString = collect;
        }
    }
}
