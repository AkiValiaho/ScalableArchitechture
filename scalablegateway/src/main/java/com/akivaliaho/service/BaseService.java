package com.akivaliaho.service;

import com.akivaliaho.event.AsyncQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.async.DeferredResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by akivv on 4.5.2017.
 */
public abstract class BaseService {
    @Autowired
    AsyncQueue asyncQueue;

    public <V> DeferredResult<V> callServiceMethod(String methodName, Object... params) {
        try {
            DeferredResult<V> deferredResult = new DeferredResult<>();
            asyncQueue.addWaitingResult(deferredResult);
            Class<?>[] classes = parseParamClasses(params);
            Method method = this.getClass().getMethod(methodName, classes);
            Object invoke = method.invoke(this, params);
            return deferredResult;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class<?>[] parseParamClasses(Object[] params) {
        List<? extends Class<?>> collect = Arrays.stream(params)
                .map(param -> {
                    Class<?> aClass = param.getClass();
                    return aClass;
                })
                .collect(Collectors.toList());
        Class<?>[] classArray = new Class[collect.size()];
        return collect.toArray(classArray);
    }

}
