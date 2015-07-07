package net.myscloud.pandora.mvc.bind.annotation;

import net.myscloud.pandora.core.annotation.Component;

import java.lang.annotation.*;

/**
 * @ClassName: Controller
 * @Description: 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {
	String value() default "";
}
