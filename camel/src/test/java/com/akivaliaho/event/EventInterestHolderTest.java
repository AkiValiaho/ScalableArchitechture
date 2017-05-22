package com.akivaliaho.event;

import com.akivaliaho.ServiceEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by akivv on 22.5.2017.
 */
public class EventInterestHolderTest {
    private EventInterestHolder eventInterestHolder;

    @Before
    public void init() {
        this.eventInterestHolder = new EventInterestHolder();
    }

    @Test(expected = NullPointerException.class)
    public void registerInterestsNullCall() throws Exception {
        eventInterestHolder.registerInterests(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerInterestsNoEventName() throws Exception {
        ServiceEvent event = new ServiceEvent("");
        eventInterestHolder.registerInterests(event);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFalseParameters() throws Exception {
        ServiceEvent event = new ServiceEvent();
        List<Integer> integerList = new ArrayList();
        integerList.add(1);
        event.saveEvent("Hello", integerList);
        eventInterestHolder.registerInterests(event);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkRegisterThrowsWithZeroParameterEvent() throws Exception {
        ServiceEvent event = new ServiceEvent("Hello");
        eventInterestHolder.registerInterests(event);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkRegisterWithoutRoutingKeyParameter() throws Exception {
        ServiceEvent serviceEvent = new ServiceEvent("Hello");
        List<ServiceEvent> interestList = new ArrayList<>();
        interestList.add(new ServiceEvent("Helloa"));
        serviceEvent.saveEvent("Hello", interestList);
        eventInterestHolder.registerInterests(serviceEvent);
    }

    @Test
    public void normalRunEventFound() throws Exception {
        ServiceEvent event = new ServiceEvent("hello");
        List<ServiceEvent> helloEvents = new ArrayList<>();
        helloEvents.add(new ServiceEvent("Yes"));
        event.saveEvent("hello", helloEvents, "serviceRouter");
        HashMap<ServiceEvent, List<String>> eventInterestMap = mock(HashMap.class);
        eventInterestHolder.setEventInterestMap(eventInterestMap);
        when(eventInterestMap.containsKey(any())).thenReturn(true);
        List<String> eventInfo = new ArrayList<>();
        when(eventInterestMap.get(any())).thenReturn(eventInfo);
        eventInterestHolder.registerInterests(event);
        //The routing key is actually added to eventinfo
        assertTrue(eventInfo.size() > 0);
        assertTrue(eventInfo.get(0).equals("serviceRouter"));
    }

    //TODO Test Normal run event not found in the map

}
