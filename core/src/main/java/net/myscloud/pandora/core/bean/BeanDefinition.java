package net.myscloud.pandora.core.bean;

/**
 * Created by user on 2015/7/6.
 */
public class BeanDefinition {

    private String beanName;
    private Class beanClass;
    private Object instance;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}
