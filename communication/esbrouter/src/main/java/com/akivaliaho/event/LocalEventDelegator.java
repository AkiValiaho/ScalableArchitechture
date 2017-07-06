package com.akivaliaho.event;

import com.akivaliaho.MethodInvoker;
import com.akivaliaho.ResultSendingTool;
import com.akivaliaho.amqp.EventUtil;
import com.akivaliaho.DomainEvent;
import com.akivaliaho.amqp.eventstrategy.EventNotInMapException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by akivv on 23.4.2017.
 */
@Component
@Slf4j
public class LocalEventDelegator {
    private final MethodInvoker methodInvoker;
    private final ResultSendingTool resultSendingTool;
    ApplicationContext applicationContext;
    private Map<String, Method> interestMap;


    public void setEventUtil(EventUtil eventUtil) {
        this.eventUtil = eventUtil;
        this.resultSendingTool.setEventUtil(eventUtil);
    }

    private EventUtil eventUtil;

    @Autowired
    public LocalEventDelegator(ApplicationContext ctx, MethodInvoker methodInvoker, ResultSendingTool resultSendingTool) {
        this.applicationContext = ctx;
        this.methodInvoker = methodInvoker;
        this.resultSendingTool = resultSendingTool;
    }

    public void delegateEvent(DomainEvent foo) throws InstantiationException, EventNotInMapException {
        if (interestMap == null || interestMap.get(foo.getEventName()) == null) {
            throw new EventNotInMapException(foo.getEventName());
        }
        Method method = null;
        method = interestMap.get(foo.getEventName());
        Object[] parameters = foo.getParameters();
        MethodInvoker.InvocationResult invocationResult = methodInvoker.invokeMethod(method, parameters);
        resultSendingTool.send(invocationResult, foo, parameters);
    }

    public void pluginInterests(Map<String, Method> interestMap) {
        this.interestMap = interestMap;
    }

}
