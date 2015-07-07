package net.myscloud.pandora.core.annotation;

import java.lang.annotation.*;

/**
 * Created by user on 2015/7/7.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Injective {
    String value() default "";
}
