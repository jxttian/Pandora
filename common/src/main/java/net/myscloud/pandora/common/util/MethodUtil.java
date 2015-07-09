package net.myscloud.pandora.common.util;

import com.google.common.collect.Maps;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Created by user on 2015/7/9.
 */
public final class MethodUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    public static Map<String, Class> getMethodParameterName(Method method) {
        //因为参数顺序不可变，所以使用LinkedHashMap
        Map<String, Class> paramsMap = Maps.newLinkedHashMap();
        try {
            Class clazz = method.getDeclaringClass();
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ClassClassPath(clazz));
            CtClass cc = pool.get(clazz.getName());

            Class[] paramClasses = method.getParameterTypes();
            CtClass[] paramCtClasses = new CtClass[paramClasses.length];
            for (int i = 0; i < paramClasses.length; i++) {
                paramCtClasses[i] = pool.get(paramClasses[i].getName());
            }

            CtMethod cm = cc.getDeclaredMethod(method.getName(), paramCtClasses);

            //使用javaassist的反射方法获取方法的参数名
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);

            int staticIndex = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < cm.getParameterTypes().length; i++) {
                paramsMap.put(attr.variableName(i + staticIndex), paramClasses[i]);
            }
        } catch (NotFoundException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return paramsMap;
    }
}
