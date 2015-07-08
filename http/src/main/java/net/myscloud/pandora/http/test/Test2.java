package net.myscloud.pandora.http.test;

import net.myscloud.pandora.core.annotation.Component;
import net.myscloud.pandora.core.annotation.Injective;
import net.myscloud.pandora.mvc.bind.annotation.Controller;
import net.myscloud.pandora.mvc.bind.annotation.RequestMap;

/**
 * Created by user on 2015/7/7.
 */
@Controller("/")
public class Test2 {

    @Injective
    private Test test;

    @RequestMap("user")
    public void test(){
        System.out.println("do test2");
        test.test();
    }

}
