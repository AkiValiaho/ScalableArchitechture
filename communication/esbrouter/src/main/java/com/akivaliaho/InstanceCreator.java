package com.akivaliaho;

import com.akivaliaho.config.annotations.Interest;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by akivv on 9.6.2017.
 */
@Component
public class InstanceCreator {
    private final AnnotationTool annotationTool;

    public InstanceCreator(AnnotationTool annotationTool) {
        //TODO Write tests for this class
        this.annotationTool = annotationTool;
    }

    public Optional<ServiceEvent> createMethodReturnTypeIfPresent(Method method, Object bean) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Interest annotation = annotationTool.getInterestAnnotation(method);
        if (annotation.emits() != Object.class) {
            Constructor<?> declaredConstructor = annotationTool.findServiceEventConstructor(annotation);
            Field fieldInterest = annotationTool.findFieldInterest(annotation.emits(), method.getDeclaringClass());
            Object o = declaredConstructor.newInstance(fieldInterest.get(bean));
            //Check that object is instance of service event
            checkArgument(o instanceof ServiceEvent);
            //Let it go
            return Optional.of(((ServiceEvent) o));
        }
        return Optional.empty();
    }

    public Optional<ServiceEvent> createReturnType(Method method, Object bean) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return createMethodReturnTypeIfPresent(method, bean);
    }
}
