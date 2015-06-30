package net.myscloud.pandora.mvc.bind.annotation;

import net.myscloud.pandora.common.annotation.Component;

import java.lang.annotation.*;

/**
 * @ClassName: Controller
 * @Description: 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface Controller {
	String value() default "";
}
