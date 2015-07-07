/**
 * @Title: HttpServer.java
 * @Package net.myscloud.pandora.http
 * @Description: Copyright: Copyright (c) 2015
 * Company:杭州点望科技有限公司
 */
package net.myscloud.pandora.http.server;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javassist.ClassPool;
import javassist.CtMethod;
import net.myscloud.pandora.common.util.PackageUtil;
import net.myscloud.pandora.http.boot.PandoraBootstrap;
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

    public static void main(String[] args) {
//        HttpServer httpServer = new HttpServer();
//        httpServer.start(8081);
        PandoraBootstrap boot = new PandoraBootstrap();
        boot.bind(80).setBossQuantity(1).setWorkerQuantity(100).start();
    }
}
