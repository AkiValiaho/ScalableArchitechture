package com.akivaliaho;

import com.akivaliaho.event.EventInterestRegistrer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by akivv on 21.5.2017.
 */
public class ProcessPreparatorTest {
    private ProcessPreparator processPreparator;
    private EventInterestRegistrer eventInterestRegistrerMock;
    private DefaultExchangeTools defaultExchangeToolsMock;

    @Before
    public void initTest() {
        eventInterestRegistrerMock = mock(EventInterestRegistrer.class);
        defaultExchangeToolsMock = mock(DefaultExchangeTools.class);
        this.processPreparator = new ProcessPreparator(eventInterestRegistrerMock, defaultExchangeToolsMock);
    }

    @org.junit.Test
    public void getServiceEvent() throws Exception {
    }

    @org.junit.Test
    public void getServiceEventResult() throws Exception {
    }

    @org.junit.Test
    public void getInterestedParties() throws Exception {
    }

    @org.junit.Test
    @Ignore
    public void invoke() throws Exception {

        ServiceEvent randomEvent = new ServiceEvent("TestEvent");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(randomEvent);


        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        when(defaultExchangeToolsMock.feedOutputStream(any())).thenReturn(objectInputStream);
        List<String> interestList = new ArrayList<>();
        interestList.add(randomEvent.getEventName());
        when(eventInterestRegistrerMock.getInterestedParties(any())).thenReturn(interestList);
        ProcessPreparator invoke = processPreparator.invoke();
        List<String> interestedParties = invoke.getInterestedParties();
        ServiceEvent serviceEvent = invoke.getServiceEvent();
        assertEquals(serviceEvent.getEventName(), "TestEvent");
        Assert.assertArrayEquals(interestList.toArray(), interestedParties.toArray());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void feedExchangeNull() throws Exception {
        processPreparator.feedExchange(null);
    }

    @org.junit.Test
    public void getPreprocessData() throws Exception {
    }

}
