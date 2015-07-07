package net.myscloud.pandora.core.bean;

import com.google.common.base.Preconditions;
import net.myscloud.pandora.common.util.PackageUtil;
import net.myscloud.pandora.common.util.AnnotationUtil;
import net.myscloud.pandora.common.util.StringUtil;
import net.myscloud.pandora.core.annotation.Component;
import net.myscloud.pandora.core.annotation.Injective;
import net.myscloud.pandora.core.exception.BeanRegisterException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by user on 2015/7/6.
 */
public class DefaultBeanFactory {

    private static final Logger LOGGER = LogManager.getLogger();

    private Map<String, BeanDefinition> beanMap = new ConcurrentHashMap<>();
    private Map<String, BeanDefinition> beanTypeMap = new ConcurrentHashMap<>();

    private static DefaultBeanFactory factory;

    /**
     * 根据Bean名称获取实例
     *
     * @param beanName
     * @return
     */
    public Object getInstance(String beanName) {
        return beanMap.get(beanName).getInstance();
    }

    /**
     * 根据Bean名称获取Class
     *
     * @param beanName
     * @return
     */
    public Object getBeanClass(String beanName) {
        return beanMap.get(beanName).getBeanClass();
    }

    /**
     * 根据Bean类型获取实例
     *
     * @param type
     * @return
     */
    public Object getInstanceByType(String type) {
        return beanTypeMap.get(type).getInstance();
    }

    /**
     * 根据Bean类型获取Class
     *
     * @param type
     * @return
     */
    public Object getBeanClassByType(String type) {
        return beanTypeMap.get(type).getBeanClass();
    }

    public synchronized static DefaultBeanFactory create(String basePackage) throws IllegalAccessException {
        Preconditions.checkArgument(StringUtil.isNotEmpty(basePackage), "basePackage must not be empty");
        if (factory == null) {
            factory = new DefaultBeanFactory();
            List<String> classNames = PackageUtil.getClassName(basePackage);
            classNames.parallelStream().forEach(item -> {
                try {
                    Class cls = Class.forName(item);
                    if (AnnotationUtil.hasAnnotation(cls, Component.class)) {
                        BeanDefinition bd = new BeanDefinition();
                        bd.setBeanName(cls.getSimpleName());
                        bd.setBeanClass(cls);
                        bd.setInstance(cls.newInstance());
                        factory.registerBean(cls.getSimpleName(), bd);
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.error(e.getMessage(), e);
                } catch (BeanRegisterException e) {
                    LOGGER.error(e.getMessage(), e);
                } catch (InstantiationException e) {
                    LOGGER.error(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            });
            factory.inject();
        }
        return factory;
    }

    /**
     * 注册Bean
     *
     * @param beanName
     * @param beanDefinition
     * @throws BeanRegisterException
     */
    public void registerBean(String beanName, BeanDefinition beanDefinition) throws BeanRegisterException {
        Preconditions.checkArgument(StringUtil.isNotEmpty(beanName), "Bean name must not be empty");
        Preconditions.checkNotNull(beanDefinition, "BeanDefinition must not be null");
        LOGGER.info("Register BeanDefinition name is {},type is {}", beanName, beanDefinition.getBeanClass().getName());
        BeanDefinition oldBean = beanMap.get(beanName);
        if (oldBean != null) {
            throw new BeanRegisterException(String.format("Cannot register bean [ %s ] for bean : There is already [ %s ] bound.", beanName, beanName));
        }
        beanMap.put(beanName, beanDefinition);
        beanTypeMap.put(beanDefinition.getBeanClass().getName(), beanDefinition);
    }

    /**
     * 注入Bean
     *
     * @throws IllegalAccessException
     */
    public void inject() throws IllegalAccessException {
        LOGGER.info("Inject beans start");
        for (BeanDefinition beanDefinition : beanTypeMap.values()) {
            Class beanClass = beanDefinition.getBeanClass();
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                Injective injective = field.getAnnotation(Injective.class);
                if (injective != null) {
                    boolean isAccessible = field.isAccessible();
                    field.setAccessible(true);
                    field.set(beanDefinition.getInstance(), beanTypeMap.get(field.getGenericType().getTypeName()).getInstance());
                    field.setAccessible(isAccessible);
                }
            }
        }
        LOGGER.info("Inject beans end");
    }
}
