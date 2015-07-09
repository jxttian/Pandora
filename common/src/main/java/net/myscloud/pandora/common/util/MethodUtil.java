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
public class MethodUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        Class clazz = MethodUtil.class;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            getMethodParameterName(clazz.getMethods()[1]);
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    public static void test(String test, String test2,String test3, String test4) {

    }

    public static Map<String, Class> getMethodParameterName(Method method) {
        Map<String, Class> paramsMap = Maps.newHashMap();
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
