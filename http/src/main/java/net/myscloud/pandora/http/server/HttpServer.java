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
import net.myscloud.pandora.http.boot.PandoraBootstrap;
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

    public static DefaultBeanFactory factory=null;

    public static void main(String[] args) throws IllegalAccessException {
//        PandoraBootstrap boot = new PandoraBootstrap();
//        boot.bind(80).setBossQuantity(1).setWorkerQuantity(100).start();
        factory = DefaultBeanFactory.create("net.myscloud");
        UrlBind.init(factory);
        Map<String, MethodDetail> urlMap=UrlBind.getUrlMap();
        PandoraBootstrap boot = new PandoraBootstrap();
        boot.bind(80).setBossQuantity(1).setWorkerQuantity(100).start();
//        Test obj1= factory.getInstance("Test");
//        obj1.test();
//        Test2 obj2= factory.getInstance("Test2");
//        obj2.test();
//        Test3 obj3= factory.getInstance("Test3");
//        obj3.test();
//        Test4 obj4= factory.getInstance("Test4");
//        obj4.test();
    }
}
