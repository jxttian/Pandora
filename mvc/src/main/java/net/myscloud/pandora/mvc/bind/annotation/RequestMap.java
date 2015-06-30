package net.myscloud.pandora.mvc.bind.annotation;

import net.myscloud.pandora.common.enums.Method;
import net.myscloud.pandora.mvc.bind.enums.ResponseType;

import java.lang.annotation.*;


/**
 * @ClassName: RequestMap
 * @Description: 
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMap {
	String value() default "";
	Method method() default  Method.GET;
	ResponseType responseType() default ResponseType.STRING;
}
