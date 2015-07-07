package net.myscloud.pandora.common.util;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by user on 2015/7/6.
 */
public final class AnnotationUtil extends AnnotationUtils {
    /**
     *判断Class是否有某一注解
     * @param cls
     * @param annotation
     * @return
     */
    public static boolean hasAnnotation(Class cls,Class annotation){
        return findAnnotation(cls,annotation)!=null;
    }

    /**
     * 判断Method是否有某一注解
     * @param method
     * @param annotation
     * @return
     */
    public static boolean hasAnnotation(Method method,Class annotation){
        return method.getAnnotation(annotation)!=null;
    }

    /**
     * 判断Field是否有某一注解
     * @param field
     * @param annotation
     * @return
     */
    public static boolean hasAnnotation(Field field,Class annotation){
        return field.getAnnotation(annotation)!=null;
    }

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
