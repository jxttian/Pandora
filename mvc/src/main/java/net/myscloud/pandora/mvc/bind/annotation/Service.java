package net.myscloud.pandora.mvc.bind.annotation;

import net.myscloud.pandora.core.boot.annotation.Component;

import java.lang.annotation.*;

/**
 * Created by user on 2015/6/30.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface Service {
    String value() default "";
}
