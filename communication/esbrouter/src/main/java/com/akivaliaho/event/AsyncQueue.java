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
    Map<Params, Map<String, List<DeferredResult<?>>>> hitList = new HashMap<>();

    public void addWaitingResult(DeferredResult<?> vAsyncResult, Object[] params, String eventName) {
        Params params1 = new Params(params);
        if (hitList.containsKey(params1)) {
            Map<String, List<DeferredResult<?>>> stringListMap = hitList.get(params1);
            if (stringListMap.containsKey(eventName)) {
                List<DeferredResult<?>> deferredResults = stringListMap.get(eventName);
                deferredResults.add(vAsyncResult);
            } else {
                List<DeferredResult<?>> deferredResults = new ArrayList<>();
                deferredResults.add(vAsyncResult);
                stringListMap.put(eventName, deferredResults);
            }
        } else {
            HashMap<String, List<DeferredResult<?>>> stringListHashMap = new HashMap<>();
            List<DeferredResult<?>> listOfDeferredResults = new ArrayList<>();
            listOfDeferredResults.add(vAsyncResult);
            stringListHashMap.put(eventName, listOfDeferredResults);
            hitList.put(params1, stringListHashMap);
        }
    }

    public void solveResult(ServiceEventResult foo) {
        //TODO Write some tests to validate this brittle piece of logic
        log.debug("Got ServiceEvent: {}", foo);
        Object[] originalParameters = foo.getOriginalParameters();
        Params param = new Params(originalParameters);
        if (hitList.containsKey(param)) {
            Map<String, List<DeferredResult<?>>> stringListMap = hitList.get(param);
            List<DeferredResult<?>> deferredResults = stringListMap.get(foo.getOriginalEventName());
            DeferredResult<Object> deferredResult = ((DeferredResult<Object>) deferredResults.get(deferredResults.size() - 1));
            deferredResult.setResult(foo.getParameters()[0]);
            //Remove from the list
            deferredResults.remove(deferredResults.size() - 1);
        }
    }
}

@EqualsAndHashCode
class Params {
    private final String paramsAsString;

    public Params(Object[] params) {
        String collect = Arrays.stream(params)
                .map(param -> param.toString())
                .collect(Collectors.joining(""));
        this.paramsAsString = collect;
    }
}
