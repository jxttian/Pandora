package net.myscloud.pandora.mvc.bind;

import com.google.common.collect.Maps;
import net.myscloud.pandora.common.util.AnnotationUtil;
import net.myscloud.pandora.common.util.MethodUtil;
import net.myscloud.pandora.core.bean.BeanDefinition;
import net.myscloud.pandora.core.bean.DefaultBeanFactory;
import net.myscloud.pandora.mvc.bind.annotation.Controller;
import net.myscloud.pandora.mvc.bind.annotation.Route;
import net.myscloud.pandora.mvc.bind.enums.RequestMethod;
import net.myscloud.pandora.mvc.bind.method.MethodDetail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by user on 2015/7/8.
 */
public final class UrlBind {

    private static final Logger LOGGER = LogManager.getLogger();

    private static Map<String, MethodDetail> urlMap = Maps.newConcurrentMap();

    public static void init(DefaultBeanFactory factory) {
        LOGGER.info("Do request map");
        for (BeanDefinition beanDefinition : factory.getBeanMap().values()) {
            Class clazz = beanDefinition.getBeanClass();
            Controller controller = AnnotationUtil.findAnnotation(clazz, Controller.class);
            if (controller == null) {
                continue;
            }
            String baseUrl = controller.value();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                Route route = method.getAnnotation(Route.class);
                if (route == null) {
                    continue;
                }
                String url= route.value();
                RequestMethod requestMethod = route.method();
                MethodDetail methodDetail = new MethodDetail();
                methodDetail.setUrl(baseUrl + url);
                methodDetail.setMethod(method);
                methodDetail.setClassName(beanDefinition.getBeanName());
                methodDetail.setRequestMethod(requestMethod);
                methodDetail.setParamsMap(MethodUtil.getMethodParameterName(method));
                LOGGER.info("Map [{}] -> [{}]",methodDetail.getUrl(),methodDetail.getMethod().toGenericString());
                urlMap.put(methodDetail.getUrl(),methodDetail);
            }
        }
    }

    public static Map<String, MethodDetail> getUrlMap() {
        return urlMap;
    }
}
