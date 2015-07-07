package net.myscloud.pandora.core.annotation.test;

import net.myscloud.pandora.core.annotation.Component;
import net.myscloud.pandora.core.annotation.Injective;

/**
 * Created by user on 2015/7/7.
 */
@Component
public class Test3 {

    @Injective
    private Test2 test;

    public void test(){
        System.out.println("do test3");
        test.test();
    }

}
