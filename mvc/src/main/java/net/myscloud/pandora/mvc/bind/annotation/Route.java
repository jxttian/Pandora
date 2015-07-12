package net.myscloud.pandora.mvc.bind.annotation;

import net.myscloud.pandora.mvc.bind.enums.RequestMethod;
import net.myscloud.pandora.mvc.bind.enums.ResponseType;

import java.lang.annotation.*;


/**
 * @ClassName: Route
 * @Description: 
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Route {
	String value() default "";
	RequestMethod method() default  RequestMethod.GET;
	ResponseType responseType() default ResponseType.STRING;
}
