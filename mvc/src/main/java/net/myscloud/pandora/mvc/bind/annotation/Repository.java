package net.myscloud.pandora.mvc.bind.annotation;

import net.myscloud.pandora.core.annotation.Component;

import java.lang.annotation.*;

/**
 * Created by user on 2015/6/30.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Repository {
    String value() default "";
}
