package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.DomainEvent;
import com.akivaliaho.event.LocalEventDelegator;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by akivv on 4.7.2017.
 */
public class SafeEventDelegatorTest {
    private SafeEventDelegator safeDelegator;
    private LocalEventDelegator localEventDelegator;

    @Before
    public void setUp() throws Exception {
        localEventDelegator = mock(LocalEventDelegator.class);
        this.safeDelegator = new SafeEventDelegator(localEventDelegator);
    }

    @Test(expected = NullPointerException.class)
    public void safeDelegationEventNull() throws Exception {
        safeDelegator.safeDelegation(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void safeDelegationTriesIllegal() throws Exception {
        safeDelegator.safeDelegation(mock(DomainEvent.class), 0);
    }

    @Test
    public void safeDelegationDelegatorThrowsOnce() throws Exception, EventNotInMapException {
        DomainEvent mockDomainEvent = mock(DomainEvent.class);
        doThrow(EventNotInMapException.class)
                .doNothing()
                .when(localEventDelegator)
                .delegateEvent(any());
        safeDelegator.safeDelegation(mockDomainEvent, 2);
    }

    @Test(expected = DelegationFailure.class)
    public void safeDelegationDelegationNotSuccesful() throws DelegationFailure, EventNotInMapException, InstantiationException {
        DomainEvent mockDomainEvent = mock(DomainEvent.class);
        doThrow(EventNotInMapException.class)
                .when(localEventDelegator).delegateEvent(any());
        safeDelegator.safeDelegation(mockDomainEvent, 1);
    }

    @Test
    public void safeDelegationSuccesful() throws Exception, EventNotInMapException {
        DomainEvent mockDomainEvent = mock(DomainEvent.class);
        doNothing()
                .when(localEventDelegator).delegateEvent(any());
        safeDelegator.safeDelegation(mockDomainEvent, 1);
    }

}