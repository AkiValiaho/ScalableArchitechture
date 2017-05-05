package com.akivaliaho.service;

import com.akivaliaho.event.AsyncQueue;
import com.akivaliaho.service.events.CalculateHardSumEvent;
import mockit.Deencapsulation;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.request.async.DeferredResult;

import static org.junit.Assert.assertEquals;

/**
 * Created by akivv on 5.5.2017.
 */
public class BaseServiceTest {
    private TestService testService;

    @Before
    public void init() {
        this.testService = new TestService();
    }

    @Test
    public void callServiceMethodNullParameters() throws Exception {
        MockUp<AsyncQueue> asyncQueueMockUp = new MockUp<AsyncQueue>() {
            @Mock
            public void addWaitingResult(DeferredResult<?> vAsyncResult, Object[] params, String eventName) {
                //Do nothing
                assertEquals(eventName, CalculateHardSumEvent.class.getName());
            }
        };
        Deencapsulation.setField(testService, "asyncQueue", asyncQueueMockUp.getMockInstance());
        testService.callServiceMethod("something", null);
    }

}