package com.akivaliaho.event;

import com.akivaliaho.amqp.EventUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by akivv on 23.4.2017.
 */
@Component
@Slf4j
public class LocalEventDelegator {
    ApplicationContext applicationContext;
    private Map<String, Method> interestMap;


    @Setter
    private EventUtil eventUtil;

    @Autowired
    public LocalEventDelegator(ApplicationContext ctx) {
        this.applicationContext = ctx;
    }


    public void delegateEvent(ServiceEvent foo) {
        Method method = interestMap.get(foo.getEventName());
        //TODO Delegate event to proper event handler
        Object[] parameters = foo.getParameters();
        try {
            Object bean = applicationContext.getBean(method.getDeclaringClass());
            ServiceEvent invoke = (ServiceEvent) method.invoke(bean, parameters);
            log.debug("Got event as a result of computation: {}", invoke);
            if (invoke instanceof ServiceEventResult) {
                ServiceEventResult invoke1 = (ServiceEventResult) invoke;
                invoke1.setOriginalParameters(parameters);
                invoke1.setOriginalEventName(foo.getEventName());
                eventUtil.publishEventResult(invoke1);
            } else {
                eventUtil.publishEvent(invoke);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void pluginInterests(Map<String, Method> interestMap) {
        this.interestMap = interestMap;
    }

}
