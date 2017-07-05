package com.akivaliaho.amqp.eventstrategy;

import com.akivaliaho.DomainEvent;
import com.akivaliaho.event.EventNotInMapException;
import com.akivaliaho.event.LocalEventDelegator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by akivv on 3.7.2017.
 */
@Component
@Slf4j
public class SafeEventDelegator {
    private final LocalEventDelegator localEventDelegator;

    @Autowired
    public SafeEventDelegator(LocalEventDelegator localEventDelegator) {
        this.localEventDelegator = localEventDelegator;
    }

    public void safeDelegation(DomainEvent domainEvent, int maxTries) throws InstantiationException, DelegationFailure {
        checkNotNull(domainEvent);
        checkArgument(maxTries > 0);
        boolean delegationSuccesful = tryToDelegate(domainEvent, maxTries);
        if (!delegationSuccesful) {
            throw new DelegationFailure(domainEvent);
        }
    }

    private boolean tryToDelegate(DomainEvent domainEvent, int maxTries) throws InstantiationException {
        int currentTries = 0;
        boolean delegationSuccesful = false;
        while (!delegationSuccesful && currentTries < maxTries) {
            try {
                localEventDelegator.delegateEvent(domainEvent);
                delegationSuccesful = true;
                currentTries = maxTries;
            } catch (EventNotInMapException e) {
                log.debug("Event " + domainEvent.getEventName() + " was not in map");
                currentTries++;
                //Lock hold for ten seconds
                safeSleep(10);
            }
        }
        return delegationSuccesful;
    }

    private void safeSleep(int seconds) {
        boolean noninterruptedSleepPerformed = false;
        while (!noninterruptedSleepPerformed) {
            try {
                Thread.sleep(new Long(seconds * 1000));
                noninterruptedSleepPerformed = true;
            } catch (InterruptedException e1) {
                noninterruptedSleepPerformed = false;
            }
        }
    }
}
