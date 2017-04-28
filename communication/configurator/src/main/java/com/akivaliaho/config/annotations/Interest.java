package com.akivaliaho.config.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by vagrant on 4/28/17.
 */
//Element type for complex meta-annotations
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface Interest {
	String value() default "";
}
