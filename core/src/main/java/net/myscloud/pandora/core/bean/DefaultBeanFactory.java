package net.myscloud.pandora.core.bean;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import net.myscloud.pandora.common.util.PackageUtil;
import net.myscloud.pandora.common.util.AnnotationUtil;
import net.myscloud.pandora.common.util.StringUtil;
import net.myscloud.pandora.core.annotation.Component;
import net.myscloud.pandora.core.annotation.Injective;
import net.myscloud.pandora.core.exception.BeanRegisterException;
import net.myscloud.pandora.core.resource.ClassPathClassReader;
import net.myscloud.pandora.core.resource.ClassReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by user on 2015/7/6.
 */
public class DefaultBeanFactory {

    private static final Logger LOGGER = LogManager.getLogger();

    private Map<String, BeanDefinition> beanMap = Maps.newConcurrentMap();
    private Map<String, BeanDefinition> beanTypeMap = Maps.newConcurrentMap();

    private static DefaultBeanFactory factory;

    /**
     * 根据Bean名称获取实例
     *
     * @param beanName
     * @return
     */
    public <T> T getInstance(String beanName) {
        T result = (T) beanMap.get(beanName).getInstance();
        return result;
    }

    /**
     * 根据Bean名称获取Class
     *
     * @param beanName
     * @return
     */
    public Class getBeanClass(String beanName) {
        return beanMap.get(beanName).getBeanClass();
    }

    /**
     * 根据Bean类型获取实例
     *
     * @param type
     * @return
     */
    public <T> T getInstanceByType(String type) {
        T result = (T) beanTypeMap.get(type).getInstance();
        return result;
    }

    /**
     * 根据Bean类型获取Class
     *
     * @param type
     * @return
     */
    public Class getBeanClassByType(String type) {
        return beanTypeMap.get(type).getBeanClass();
    }

    public synchronized static DefaultBeanFactory create(String basePackage) throws IllegalAccessException {
        Preconditions.checkArgument(StringUtil.isNotEmpty(basePackage), "basePackage must not be empty");
        if (factory == null) {
            factory = new DefaultBeanFactory();
            ClassReader classReader = new ClassPathClassReader();
            Set<Class<?>> clazzes = classReader.getClass(basePackage, true);
            clazzes.parallelStream().forEach(clazz -> {
                try {
                    if (AnnotationUtil.hasAnnotation(clazz, Component.class) && !clazz.isAnnotation()) {
                        BeanDefinition bd = new BeanDefinition();
                        bd.setBeanName(clazz.getSimpleName());
                        bd.setBeanClass(clazz);
                        bd.setInstance(clazz.newInstance());
                        factory.registerBean(clazz.getSimpleName(), bd);
                    }
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
                if (injective == null) {
                    //如果字段没有Injective注解，则不执行注入
                    continue;
                }
                //
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                BeanDefinition bean = beanTypeMap.get(field.getGenericType().getTypeName());
                if (bean != null) {
                    //如果在Map中找到相应的Bean，就注入
                    field.set(beanDefinition.getInstance(), bean.getInstance());
                }
                field.setAccessible(isAccessible);
            }
        }
        LOGGER.info("Inject beans end");
    }

    public Map<String, BeanDefinition> getBeanMap() {
        return beanMap;
    }

    public Map<String, BeanDefinition> getBeanTypeMap() {
        return beanTypeMap;
    }
}
