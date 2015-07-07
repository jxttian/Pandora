package net.myscloud.pandora.http.test;

import net.myscloud.pandora.core.annotation.Component;
import net.myscloud.pandora.core.annotation.Injective;
import net.myscloud.pandora.mvc.bind.annotation.Repository;

/**
 * Created by user on 2015/7/7.
 */
@Repository
public class Test3 {

    @Injective
    private Test2 test;

    public void test(){
        System.out.println("do test3");
        test.test();
    }

}
