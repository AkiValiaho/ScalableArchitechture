package com.akivaliaho.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by vagrant on 4/28/17.
 */
//Element type for complex meta-annotations
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Interest {
    Class<?> value() default Object.class;

    Class<?> emit() default Object.class;
}
