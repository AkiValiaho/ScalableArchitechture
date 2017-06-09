package com.akivaliaho;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Created by akivv on 9.6.2017.
 */
@Component
@Slf4j
public class MethodInvoker {

    private final ApplicationContext applicationContext;
    private final InstanceCreator instanceCreator;

    @Autowired
    public MethodInvoker(ApplicationContext applicationContext, InstanceCreator instanceCreator) {
        //TODO Write tests for this class
        this.applicationContext = applicationContext;
        this.instanceCreator = instanceCreator;
    }

    public InvocationResult invokeMethod(Method method, Object[] parameters) throws InstantiationException {
        try {
            Object bean = applicationContext.getBean(method.getDeclaringClass());
            //Parse method return type and create a result from it automatically
            Optional<ServiceEvent> returnType = instanceCreator.createReturnType(method, bean);
            ServiceEvent invoke = (ServiceEvent) method.invoke(bean, parameters);
            log.debug("Got event as a result of computation: {}", invoke);
            return new InvocationResult(returnType, invoke);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class InvocationResult {
        @Getter
        @Setter
        private final Optional<ServiceEvent> returnType;
        @Getter
        @Setter
        private final ServiceEvent invocationResult;

        public InvocationResult(Optional<ServiceEvent> returnType, ServiceEvent invoke) {
            this.returnType = returnType;
            this.invocationResult = invoke;
        }
    }
}
