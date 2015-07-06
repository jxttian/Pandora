package net.myscloud.pandora.common.util;

import org.apache.commons.lang3.AnnotationUtils;

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
        return cls.getAnnotation(annotation)!=null;
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
}
