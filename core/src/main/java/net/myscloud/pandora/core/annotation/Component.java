package net.myscloud.pandora.core.annotation;

import java.lang.annotation.*;

/**
 * Created by user on 2015/6/30.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
