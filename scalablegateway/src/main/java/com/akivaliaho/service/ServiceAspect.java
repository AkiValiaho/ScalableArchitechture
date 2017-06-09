package com.akivaliaho.service;

import com.akivaliaho.ServiceEvent;
import com.akivaliaho.amqp.EventUtil;
import com.akivaliaho.config.annotations.Interest;
import com.akivaliaho.event.AsyncQueue;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by akivv on 14.5.2017.
 */
@Aspect
@Component
@Slf4j
public class ServiceAspect {
    AsyncQueue asyncQueue;
    EventUtil eventUtil;
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    public ServiceAspect(AsyncQueue asyncQueue, EventUtil eventUtil) {
        this.asyncQueue = asyncQueue;
        this.eventUtil = eventUtil;
        threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(4);
        threadPoolTaskExecutor.setMaxPoolSize(4);
        threadPoolTaskExecutor.setThreadNamePrefix("GatewayServiceThread-");
        threadPoolTaskExecutor.initialize();
    }

    @Pointcut("execution(* com.akivaliaho.service.*Service.*(..))")
    public void servicePointcut() {

    }

    @Around("servicePointcut()")
    public <V> DeferredResult<V> aroundServiceCall(ProceedingJoinPoint proceedingJoinPoint) {
        //TODO Too long, needs splitting up
        MethodInvocationProceedingJoinPoint methodInvcJoinPoint = (MethodInvocationProceedingJoinPoint) proceedingJoinPoint;
        String invokedMethodName = methodInvcJoinPoint.getSignature().getName();
        //Parse arg types
        //Parse the method
        try {
            Method invokedMethod = parseMethod(invokedMethodName, proceedingJoinPoint.getSignature().getDeclaringType(), parseArgTypes(proceedingJoinPoint.getArgs()));
            Interest annotation = getAnnotation(invokedMethod);
            DeferredResult<V> deferredresult = new DeferredResult<V>(TimeUnit.MILLISECONDS.convert(300, TimeUnit.SECONDS));
            asyncQueue.addWaitingResult(deferredresult, proceedingJoinPoint.getArgs(), annotation.emits().getName());
            threadPoolTaskExecutor.execute(() -> {
                ServiceEvent invoke = null;
                try {
                    invoke = (ServiceEvent) proceedingJoinPoint.proceed();
                    Interest annotation1 = getAnnotation(invokedMethod);
                    Class<?> emit = annotation1.emits();
                    Constructor<?>[] declaredConstructors = emit.getDeclaredConstructors();
                    Constructor<?> declaredConstructor = declaredConstructors[0];
                    ServiceEvent o = (ServiceEvent) declaredConstructor.newInstance(proceedingJoinPoint.getArgs());
                    eventUtil.publishEvent(o);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("error while trying to execute a method invocation {} asynchronously: exception details:", invokedMethod.getName(), e);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
            return deferredresult;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        System.out.println(proceedingJoinPoint);
        return null;
    }

    private Interest getAnnotation(Method invokedMethod) {
        Interest annotation = invokedMethod.getAnnotation(Interest.class);
        if (annotation == null || annotation.emits() == null) {
            throw new IllegalArgumentException("@Interest annotation or given emits-receives not present in method: " + invokedMethod.getName());
        }
        return annotation;
    }

    private Class<?>[] parseArgTypes(Object[] args) {
        List<? extends Class<?>> collect = Arrays.stream(args)
                .map(arg -> arg.getClass())
                .collect(Collectors.toList());
        Class<?>[] arrayOfClasses = new Class[collect.size()];
        return collect.toArray(arrayOfClasses);
    }

    private Method parseMethod(String invokedMethodName, Class declaringType, Class<?>... params) throws NoSuchMethodException {
        return declaringType.getMethod(invokedMethodName, params);
    }

    private String parseEventName(ProceedingJoinPoint proceedingJoinPoint) {
        return null;
    }
}
