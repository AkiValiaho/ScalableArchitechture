package com.akivaliaho;

import com.akivaliaho.event.EventInterestHolder;
import org.junit.Before;
import org.junit.Ignore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by akivv on 21.5.2017.
 */
public class ProcessPreparatorTest {
    private ProcessPreparator processPreparator;
    private EventInterestHolder eventInterestHolderMock;
    private ExchangeTools exchangeToolsMock;

    @Before
    public void initTest() {
        eventInterestHolderMock = mock(EventInterestHolder.class);
        exchangeToolsMock = mock(ExchangeTools.class);
        this.processPreparator = new ProcessPreparator(eventInterestHolderMock, exchangeToolsMock);
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
        //TODO Finish this functional test

        ServiceEvent randomEvent = new ServiceEvent("TestEvent");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(randomEvent);


        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        when(exchangeToolsMock.feedOutputStream(any())).thenReturn(objectInputStream);
        processPreparator.invoke();
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void feedExchangeNull() throws Exception {
        processPreparator.feedExchange(null);
    }

    @org.junit.Test
    public void getPreprocessData() throws Exception {
    }

}
