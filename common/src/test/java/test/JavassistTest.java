/**
 * @Title: JavassistTest.java
 * @Package net.myscloud.pandora.test
 * @Description: 
 * Copyright: Copyright (c) 2015 
 * Company:杭州点望科技有限公司
 */
package test;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * @ClassName: JavassistTest
 * @Description: 
 */
public class JavassistTest {

	public static void main(String[] args) {  
        testReflectParamName();  
    }  
  
    /** 
     * 反射获取方法参数名称 
     */  
    @SuppressWarnings("rawtypes")
	public static void testReflectParamName() {  
        Class clazz = MyClass.class;  
        try {  
            ClassPool pool = ClassPool.getDefault();  
            CtClass cc = pool.get(clazz.getName());  
            CtMethod cm = cc.getDeclaredMethod("concatString");  
  
            // 使用javaassist的反射方法获取方法的参数名  
            MethodInfo methodInfo = cm.getMethodInfo();  
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();  
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute  
                    .getAttribute(LocalVariableAttribute.tag);  
            if (attr == null) {  
                // exception  
            }  
            String[] paramNames = new String[cm.getParameterTypes().length];  
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;  
            for (int i = 0; i < paramNames.length; i++)  
                paramNames[i] = attr.variableName(i + pos);  
            // paramNames即参数名  
            for (int i = 0; i < paramNames.length; i++) {  
                System.out.println("参数名" + i + ":" + paramNames[i]);  
            }  
  
        } catch (NotFoundException e) {  
            e.printStackTrace();  
        }  
    }  
}  
  
class MyClass {  
    public String concatString(String test, String param2) {  
        return test + param2;  
    }  
}  