package net.myscloud.pandora.core.bean;

import com.google.common.base.Preconditions;
import net.myscloud.pandora.common.util.StringUtil;
import net.myscloud.pandora.core.exception.BeanRegisterException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by user on 2015/7/6.
 */
public class DefaultBeanFactory {
    private Map<String, BeanDetail> beanMap = new ConcurrentHashMap<>();

    public BeanDetail getBean(String beanName) {
        return beanMap.get(beanName);
    }

    public void registerBean(String beanName, BeanDetail beanDetail) throws BeanRegisterException {
        Preconditions.checkArgument(StringUtil.isNotEmpty(beanName), "Bean name must not be empty");
        Preconditions.checkNotNull(beanDetail, "BeanDefinition must not be null");
        BeanDetail oldBean = beanMap.get(beanName);
        if (oldBean != null) {
            throw new BeanRegisterException(String.format("Cannot register bean [ %s ] for bean : There is already [ %s ] bound.", beanName, beanName));
        }
        beanMap.put(beanName, beanDetail);
    }
}
