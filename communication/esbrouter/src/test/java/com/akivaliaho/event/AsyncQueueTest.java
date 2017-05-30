package com.akivaliaho.event;

import com.akivaliaho.AppendStringsEventResult;
import com.akivaliaho.ServiceEventResult;
import mockit.Deencapsulation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by akivv on 5.5.2017.
 */
public class AsyncQueueTest {
    private AsyncQueue asyncQueue;

    @Before
    public void setUp() throws Exception {
        this.asyncQueue = new AsyncQueue();
    }

    @Test
    public void noParamsCase() throws Exception {
        Map<Params, Map<String, List<DeferredResult<?>>>> hitList = new HashMap<>();
        //No parameters yet present
        Integer[] params = initializeHitList(hitList);
        Params params1 = new Params(params);
        Map<String, List<DeferredResult<?>>> object = hitList.get(params1);
        assertNotNull(object);
        List<DeferredResult<?>> deferredResults = object.get("hackyTestEvent");
        assertTrue(deferredResults.size() > 0);

    }

    private Integer[] initializeHitList(Map<Params, Map<String, List<DeferredResult<?>>>> hitList) {
        Deencapsulation.setField(asyncQueue, "hitList", hitList);
        DeferredResult<Object> hello = new DeferredResult<>();
        Integer[] params = {1, 2};
        asyncQueue.addWaitingResult(hello, params, "hackyTestEvent");
        return params;
    }

    @Test
    public void paramsAlreadyPresentCase() throws Exception {
        Map<Params, Map<String, List<DeferredResult<?>>>> hitList = new HashMap<>();
        Integer[] integers = initializeHitList(hitList);
        //Add the same stuff again, assert that DeferredResult-list is actually increased
        DeferredResult<Object> hello = new DeferredResult<>();
        Integer[] params = {1, 2};
        asyncQueue.addWaitingResult(hello, params, "hackyTestEvent");
        assertNotNull(hitList.get(new Params(params)));
        Map<String, List<DeferredResult<?>>> stringListMap = hitList.get(new Params(params));
        List<DeferredResult<?>> deferredResults = stringListMap.get("hackyTestEvent");
        assertTrue(deferredResults.size() > 1);
    }

    @Test
    public void paramsPresentButDifferentEventName() throws Exception {
        Map<Params, Map<String, List<DeferredResult<?>>>> hitList = new HashMap<>();
        Integer[] integers = initializeHitList(hitList);
        //Add the same stuff again, assert that DeferredResult-list is actually increased
        DeferredResult<Object> hello = new DeferredResult<>();
        Integer[] params = {1, 2};
        asyncQueue.addWaitingResult(hello, params, "notSoHackyTestEvent");
        assertNotNull(hitList.get(new Params(params)));
        Map<String, List<DeferredResult<?>>> stringListMap = hitList.get(new Params(params));
        assertTrue(stringListMap.size() > 1);
        List<DeferredResult<?>> deferredResults = stringListMap.get("hackyTestEvent");
        assertTrue(deferredResults.size() == 1);
        List<DeferredResult<?>> deferredResults1 = stringListMap.get("notSoHackyTestEvent");
        assertTrue(deferredResults1.size() == 1);
    }

    @Test(expected = NullPointerException.class)
    public void solveTestResultEventNotFound() {
        ServiceEventResult serviceEventResult = new AppendStringsEventResult("asdf");
        this.asyncQueue.solveResult(serviceEventResult);

    }
}