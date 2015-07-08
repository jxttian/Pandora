/**
 * @Title: HttpServer.java
 * @Package net.myscloud.pandora.http
 * @Description: Copyright: Copyright (c) 2015
 * Company:杭州点望科技有限公司
 */
package net.myscloud.pandora.http.server;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javassist.ClassPool;
import javassist.CtMethod;
import net.myscloud.pandora.common.util.PackageUtil;

import net.myscloud.pandora.core.bean.DefaultBeanFactory;
import net.myscloud.pandora.http.test.Test;
import net.myscloud.pandora.http.test.Test2;
import net.myscloud.pandora.http.test.Test3;
import net.myscloud.pandora.http.test.Test4;
import net.myscloud.pandora.mvc.bind.UrlBind;
import net.myscloud.pandora.mvc.bind.method.MethodDetail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @ClassName: HttpServer
 * @Description:
 */
public final class HttpServer {

    private static final Logger LOGGER = LogManager.getLogger();

    public static ConcurrentHashMap<String, CtMethod> PATHMAP = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Object> BEANMAP = new ConcurrentHashMap<>();

    public void init() {
        try {
            ClassPool pool = ClassPool.getDefault();
            String packageName = "net.myscloud";
            List<String> classNames = PackageUtil.getClassName(packageName);
            if (classNames == null || classNames.size() == 0) {
                return;
            }
//			for (String className : classNames) {
//		        CtClass type = pool.get(className);
//				if (!type.hasAnnotation(Controller.class)) {
//					continue;
//				}
//
//				BEANMAP.put(type.getName(),type);
//
//				for (CtMethod method : type.getMethods()) {
//					Path path=(Path)method.getAnnotation(Path.class);
//					if (path!=null) {
//						PATHMAP.put(path.value(), method);
//					}
//				}
//			}
        } catch (SecurityException
                | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IllegalAccessException {
//        PandoraBootstrap boot = new PandoraBootstrap();
//        boot.bind(80).setBossQuantity(1).setWorkerQuantity(100).start();
        DefaultBeanFactory factory = DefaultBeanFactory.create("net.myscloud");
        UrlBind.init(factory);
        Map<String, MethodDetail> urlMap=UrlBind.getUrlMap();
        Test obj1= factory.getInstance("Test");
        obj1.test();
        Test2 obj2= factory.getInstance("Test2");
        obj2.test();
        Test3 obj3= factory.getInstance("Test3");
        obj3.test();
        Test4 obj4= factory.getInstance("Test4");
        obj4.test();
    }
}
