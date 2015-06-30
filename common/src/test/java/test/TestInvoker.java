package test;

import net.myscloud.pandora.common.reflect.Invokers;

import java.lang.reflect.Method;
import java.util.Date;


/**
 * @ClassName: TestInvoker
 * @Description: 
 */
public class TestInvoker {
	 private static long l = 333333L;  
	    private static int times = 100000000;  
	  
	    public static void main(String[] args) throws Exception {  
	        //要分别独立跑，一齐跑相互有影响的。  
//	        test();  
//	    	testInvoker();  
	        testJDK();  
	        //testFastMethod();  
	    }  
	  
	    public static void test() {  
	        Date date = new Date(l);  
	        long t0 = System.currentTimeMillis();  
	        for (int i = 0; i < times; i++) {  
	            date.getTime();  
	            date.setTime(333333333L);  
	        }  
	        long t1 = System.currentTimeMillis();  
	        System.out.println("直接调用耗时：" + (t1 - t0) + "ms");  
	    }  
	  
	    public static void testInvoker() throws Exception {  
	        Date date = new Date();  
	        Method getMethod = Date.class.getMethod("getTime");  
	        getMethod.setAccessible(true);  
	        Method setMethod = Date.class.getMethod("setTime", Long.TYPE);  
	        setMethod.setAccessible(true);  
	        Invokers.Invoker get = Invokers.newInvoker(getMethod);
	        Invokers.Invoker set = Invokers.newInvoker(setMethod);
	        long t0 = System.currentTimeMillis();  
	        for (int i = 0; i < times; i++) {  
	            get.invoke(date, new Object[] {});  
	            set.invoke(date, new Object[] { 333333L });  
	        }  
	        long t1 = System.currentTimeMillis();  
	        System.out.println("Invoker调用耗时：" + (t1 - t0) + "ms");  
	    }  
	  
	    public static void testJDK() throws Exception {  
	  
	        Date date = new Date();  
	        Method getMethod = Date.class.getMethod("getTime");  
	        getMethod.setAccessible(true);  
	        Method setMethod = Date.class.getMethod("setTime", Long.TYPE);  
	        setMethod.setAccessible(true);  
	        long t0 = System.currentTimeMillis();  
	        for (int i = 0; i < times; i++) {  
	            getMethod.invoke(date, new Object[] {});  
	            setMethod.invoke(date, new Object[] { 333333L });  
	        }  
	        long t1 = System.currentTimeMillis();  
	        System.out.println("JDK反射调用耗时：" + (t1 - t0) + "ms");  
	    }  
}
