package net.myscloud.pandora.common.util;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by user on 2015/7/6.
 */
public final class AnnotationUtil{
    /**
     *判断Class是否有某一注解
     * @param clazz
     * @param annotationType
     * @return
     */
    public static boolean hasAnnotation(Class clazz,Class annotationType){
        return findAnnotation(clazz,annotationType)!=null;
    }

    /**
     * 判断Method是否有某一注解
     * @param method
     * @param annotationType
     * @return
     */
    public static boolean hasAnnotation(Method method,Class annotationType){
        return method.getAnnotation(annotationType)!=null;
    }

    /**
     * 判断Field是否有某一注解
     * @param field
     * @param annotationType
     * @return
     */
    public static boolean hasAnnotation(Field field,Class annotationType){
        return field.getAnnotation(annotationType)!=null;
    }

    /**
     * 查找某一注解
     * @param clazz
     * @param annotationType
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        A annotation = clazz.getAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        for (Class<?> ifc : clazz.getInterfaces()) {
            annotation = findAnnotation(ifc, annotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        if (!Annotation.class.isAssignableFrom(clazz)) {
            for (Annotation ann : clazz.getAnnotations()) {
                annotation = findAnnotation(ann.annotationType(), annotationType);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == null || superClass == Object.class) {
            return null;
        }
        return findAnnotation(superClass, annotationType);
    }
}
