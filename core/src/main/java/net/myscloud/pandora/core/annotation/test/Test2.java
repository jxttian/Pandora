package net.myscloud.pandora.core.annotation.test;

import net.myscloud.pandora.core.annotation.Component;
import net.myscloud.pandora.core.annotation.Injective;

/**
 * Created by user on 2015/7/7.
 */
@Component
public class Test2 {

    @Injective
    private Test test;

    public void test(){
        System.out.println("do test2");
        test.test();
    }

}
