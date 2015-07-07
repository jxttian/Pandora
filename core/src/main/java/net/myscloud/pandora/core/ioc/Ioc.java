package net.myscloud.pandora.core.ioc;

import com.google.common.collect.Maps;
import net.myscloud.pandora.common.reflect.util.PackageUtil;
import net.myscloud.pandora.common.util.AnnotationUtil;
import net.myscloud.pandora.core.annotation.Component;
import net.myscloud.pandora.core.annotation.test.Test;
import net.myscloud.pandora.core.bean.BeanDetail;
import net.myscloud.pandora.core.bean.DefaultBeanFactory;
import net.myscloud.pandora.core.exception.BeanRegisterException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2015/7/6.
 */
public class Ioc {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final Map<String, Object> BEAN_MAP = Maps.newHashMap();

    public static void main(String[] args) {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        List<String> classNames = PackageUtil.getClassName("net.myscloud");
        classNames.parallelStream().forEach(item -> {
            try {
                Class cls = Class.forName(item);
                if (AnnotationUtil.hasAnnotation(cls, Component.class)) {
                    BeanDetail bd = new BeanDetail();
                    bd.setBeanClass(cls);
                    factory.registerBean(cls.getSimpleName(), bd);
//                    BEAN_MAP.put(cls.getSimpleName(), cls.newInstance());
                }
            } catch (ClassNotFoundException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (BeanRegisterException e) {
                e.printStackTrace();
            }
        });
        System.out.printf("" + factory.getBean("Test").getBeanClass().getName());
    }
}
