package net.myscloud.pandora.core.ioc;

import net.myscloud.pandora.core.annotation.test.Test;
import net.myscloud.pandora.core.annotation.test.Test2;
import net.myscloud.pandora.core.annotation.test.Test3;
import net.myscloud.pandora.core.bean.DefaultBeanFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by user on 2015/7/6.
 */
public class Ioc {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws IllegalAccessException {
        DefaultBeanFactory factory = DefaultBeanFactory.create("net.myscloud");
        Object obj1= factory.getInstance("Test");
        ((Test)obj1).test();
        Object obj2= factory.getInstance("Test2");
        ((Test2)obj2).test();
        Object obj3= factory.getInstance("Test3");
        ((Test3)obj3).test();
    }
}
