/**
 * @Title: Path.java
 * @Package net.myscloud.pandora.controller.annotation
 * @Description: 
 * Copyright: Copyright (c) 2015 
 * Company:杭州点望科技有限公司
 */
package net.myscloud.pandora.common.annotation;

import net.myscloud.pandora.common.enums.Method;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @ClassName: Path
 * @Description: 
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Path {
	String value() default "";
	Method method() default  Method.GET;
}
