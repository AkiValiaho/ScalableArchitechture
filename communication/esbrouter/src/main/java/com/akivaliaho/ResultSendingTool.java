package com.akivaliaho;

import com.akivaliaho.amqp.EventUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by akivv on 9.6.2017.
 */
@Component
public class ResultSendingTool {
    private EventUtil eventUtil;

    public void send(MethodInvoker.InvocationResult invocationResult, ServiceEvent originalDelegatedEvent, Object[] parameters) {
        //TODO Write tests for this class
        sendInvocationOrParsedReturn(originalDelegatedEvent, parameters, invocationResult.getReturnType(), invocationResult.getInvocationResult());
    }

    private void sendInvocationOrParsedReturn(ServiceEvent originalDelegatedEvent, Object[] parameters, Optional<ServiceEvent> returnType, ServiceEvent invoke) {
        if (sendIfInvokeReturned(originalDelegatedEvent, parameters, invoke)) {

        } else {
            sendIfReturnTypeFound(returnType, originalDelegatedEvent, parameters);
        }
    }

    private void sendIfReturnTypeFound(Optional<ServiceEvent> returnType, ServiceEvent originalEvent, Object[] parameters) {
        if (returnType.isPresent()) {
            sendIfInvokeReturned(originalEvent, parameters, returnType.get());
        }
    }

    private boolean sendIfInvokeReturned(ServiceEvent originalEvent, Object[] parameters, ServiceEvent invocationResult) {
        if (invocationResult instanceof ServiceEventResult) {
            ServiceEventResult invoke1 = (ServiceEventResult) invocationResult;
            invoke1.setOriginalParameters(parameters);
            invoke1.setOriginalEventName(originalEvent.getEventName());
            eventUtil.publishEventResult(invoke1);
            return true;
        } else if (invocationResult instanceof ServiceEvent) {
            eventUtil.publishEvent(invocationResult);
            return true;
        }
        return false;
    }

    public void setEventUtil(EventUtil eventUtil) {
        this.eventUtil = eventUtil;
    }
}
