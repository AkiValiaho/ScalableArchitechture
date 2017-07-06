package com.akivaliaho;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by akivv on 6.7.2017.
 */
public class FieldMatcherTool {
    private List<Class<?>> types;

    public boolean attemptMatch(Class<?> builderFieldArray, List<Class<?>> typesOfTheClass) {
        this.types = typesOfTheClass;
        //Service events call should have the right properties
        //This is quite generic matching code here
        List<Object> collect = Arrays.stream(builderFieldArray.getDeclaredFields())
                .filter(this::inTypes)
                .collect(Collectors.toList());
        return collect.size() == typesOfTheClass.size();
    }

    private boolean inTypes(Object o) {
        return types.contains(o.getClass());
    }
}
