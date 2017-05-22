package com.akivaliaho.event;

import com.akivaliaho.ServiceEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by akivv on 22.5.2017.
 */
public class EventInterestRegistrerTest {
    private EventInterestRegistrer eventInterestRegistrer;
    private EventAndRoutingKeyHolder eventAndRoutingKeyHolderMock;

    @Before
    public void init() {
        this.eventAndRoutingKeyHolderMock = mock(EventAndRoutingKeyHolder.class);
        this.eventInterestRegistrer = new EventInterestRegistrer(eventAndRoutingKeyHolderMock);
    }

    @Test(expected = NullPointerException.class)
    public void registerInterestsNullCall() throws Exception {
        eventInterestRegistrer.registerInterests(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerInterestsNoEventName() throws Exception {
        ServiceEvent event = new ServiceEvent("");
        eventInterestRegistrer.registerInterests(event);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFalseParameters() throws Exception {
        ServiceEvent event = new ServiceEvent();
        List<Integer> integerList = new ArrayList();
        integerList.add(1);
        event.saveEvent("Hello", integerList);
        eventInterestRegistrer.registerInterests(event);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkRegisterThrowsWithZeroParameterEvent() throws Exception {
        ServiceEvent event = new ServiceEvent("Hello");
        eventInterestRegistrer.registerInterests(event);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkRegisterWithoutRoutingKeyParameter() throws Exception {
        ServiceEvent serviceEvent = new ServiceEvent("Hello");
        List<ServiceEvent> interestList = new ArrayList<>();
        interestList.add(new ServiceEvent("Helloa"));
        serviceEvent.saveEvent("Hello", interestList);
        eventInterestRegistrer.registerInterests(serviceEvent);
    }

    @Test
    public void normalRunEventFound() throws Exception {
        ServiceEvent serviceEvent = new ServiceEvent("Hello");
        List<ServiceEvent> objects = new ArrayList<>();
        objects.add(new ServiceEvent("Something wild"));
        serviceEvent.saveEvent("Hello", objects, "helloRoutingKey");
        ArgumentCaptor<ServiceEvent> serviceEventArgumentCaptor = ArgumentCaptor.forClass(ServiceEvent.class);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.doNothing().when(eventAndRoutingKeyHolderMock).handleEvent(serviceEventArgumentCaptor.capture(), stringArgumentCaptor.capture());
        eventInterestRegistrer.registerInterests(serviceEvent);
        assertEquals(stringArgumentCaptor.getValue(), "helloRoutingKey");
        assertEquals(serviceEventArgumentCaptor.getValue().getEventName(), "Something wild");

    }


}
