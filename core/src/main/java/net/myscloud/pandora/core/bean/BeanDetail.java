package net.myscloud.pandora.core.bean;

/**
 * Created by user on 2015/7/6.
 */
public class BeanDetail {
    private String beanName;
    private Class beanClass;

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
}
