package com.akivaliaho.service;

import com.akivaliaho.amqp.EventUtil;
import com.akivaliaho.config.annotations.Interest;
import com.akivaliaho.event.AsyncQueue;
import com.akivaliaho.event.ServiceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.async.DeferredResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by akivv on 4.5.2017.
 */
public abstract class BaseService {
    @Autowired
    AsyncQueue asyncQueue;
    @Autowired
    EventUtil eventUtil;

    public <V> DeferredResult<V> callServiceMethod(String methodName, Object... params) {
        try {
            DeferredResult<V> deferredResult = new DeferredResult<>(TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS));
            Class<?>[] classes = null;
            if (params != null) {
                classes = parseParamClasses(params);
            }
            Method method = this.getClass().getMethod(methodName, classes);
            Interest annotation = method.getAnnotation(Interest.class);
            if (annotation == null || annotation.emit() == null) {
                throw new IllegalArgumentException("@Interest annotation or given emit-value not present in method: " + method.getName());
            }
            asyncQueue.addWaitingResult(deferredResult, params, annotation.emit().getName());
            ServiceEvent invoke = (ServiceEvent) method.invoke(this, params);
            eventUtil.publishEvent(invoke);
            return deferredResult;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class<?>[] parseParamClasses(Object[] params) {
        List<? extends Class<?>> collect = Arrays.stream(params)
                .map(param -> {
                    if (param == null) {
                        return null;
                    }
                    Class<?> aClass = param.getClass();
                    return aClass;
                })
                .collect(Collectors.toList());
        Class<?>[] classArray = new Class[collect.size()];
        return collect.toArray(classArray);
    }

}
