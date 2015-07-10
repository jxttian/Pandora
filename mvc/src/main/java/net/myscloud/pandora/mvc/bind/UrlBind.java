package net.myscloud.pandora.mvc.bind;

import com.google.common.collect.Maps;
import net.myscloud.pandora.common.util.AnnotationUtil;
import net.myscloud.pandora.common.util.MethodUtil;
import net.myscloud.pandora.core.bean.BeanDefinition;
import net.myscloud.pandora.core.bean.DefaultBeanFactory;
import net.myscloud.pandora.mvc.bind.annotation.Controller;
import net.myscloud.pandora.mvc.bind.annotation.RequestMap;
import net.myscloud.pandora.mvc.bind.enums.RequestMethod;
import net.myscloud.pandora.mvc.bind.method.MethodDetail;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by user on 2015/7/8.
 */
public final class UrlBind {

    private static Map<String, MethodDetail> urlMap = Maps.newConcurrentMap();

    public static void init(DefaultBeanFactory factory) {
        for (BeanDefinition beanDefinition : factory.getBeanMap().values()) {
            Class clazz = beanDefinition.getBeanClass();
            Controller controller = AnnotationUtil.findAnnotation(clazz, Controller.class);
            if (controller == null) {
                continue;
            }
            String baseUrl = controller.value();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                RequestMap requestMap = method.getAnnotation(RequestMap.class);
                if (requestMap == null) {
                    continue;
                }
                String url=requestMap.value();
                RequestMethod requestMethod = requestMap.method();
                MethodDetail methodDetail = new MethodDetail();
                methodDetail.setUrl(baseUrl + url);
                methodDetail.setMethod(method);
                methodDetail.setClassName(beanDefinition.getBeanName());
                methodDetail.setRequestMethod(requestMethod);
                methodDetail.setParamsMap(MethodUtil.getMethodParameterName(method));
                urlMap.put(methodDetail.getUrl(),methodDetail);
            }
        }
    }

    public static Map<String, MethodDetail> getUrlMap() {
        return urlMap;
    }
}
