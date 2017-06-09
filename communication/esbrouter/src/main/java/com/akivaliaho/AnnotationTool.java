package com.akivaliaho;

import com.akivaliaho.config.annotations.FieldInterest;
import com.akivaliaho.config.annotations.Interest;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by akivv on 9.6.2017.
 */
@Component
public class AnnotationTool {
    //TODO Write tests for this class
    public Interest getInterestAnnotation(Method invokedMethod) {
        Interest annotation = invokedMethod.getAnnotation(Interest.class);
        if (annotation == null || annotation.emits() == null) {
            throw new IllegalArgumentException("@Interest annotation or given emits-receives not present in method: " + invokedMethod.getName());
        }
        return annotation;
    }

    public Constructor<?> findServiceEventConstructor(Interest annotation) {
        Class<?> emits = annotation.emits();
        Constructor<?>[] declaredConstructors = emits.getDeclaredConstructors();
        if (declaredConstructors.length != 1) {
            throw new IllegalArgumentException("Events should only have one constructor defined! Illegal event: " + emits.getName());
        }
        return declaredConstructors[0];
    }

    private Field findAndCheckFieldInterest(Class<?> emits, Class<?> declaringClass) {
        Field[] declaredFields = declaringClass.getDeclaredFields();
        List<Field> collect = Arrays.stream(declaredFields)
                .filter(field -> {
                    FieldInterest annotation = field.getAnnotation(FieldInterest.class);
                    //TODO Test  What if there are no fields with FieldInterest annotation yet we have some methods with a return value?
                    return annotation == null ? false : (annotation.event().equals(emits));
                }).collect(Collectors.toList());
        if (collect.size() != 1) {
            throw new IllegalArgumentException("Methods with an EventResult value should have a singular FieldInterest");
        }
        return collect.get(0);
    }

    public Field findFieldInterest(Class<?> fieldEmit, Class<?> declaringClass) {
        //Find a field interest if the class has one
        Field fieldInterest = findAndCheckFieldInterest(fieldEmit, declaringClass);
        fieldInterest.setAccessible(true);
        return fieldInterest;
    }
}
