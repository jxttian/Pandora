package net.myscloud.pandora.http.test;

import net.myscloud.pandora.core.annotation.Injective;
import net.myscloud.pandora.mvc.bind.annotation.Controller;
import net.myscloud.pandora.mvc.bind.annotation.Route;
import net.myscloud.pandora.mvc.bind.annotation.response.Json;

/**
 * Created by user on 2015/7/7.
 */
@Controller("/")
public class Test2 {

    @Injective
    private Test test;

    @Route("user")
    @Json
    public Pojo test(String test,String test5,String test3,String test4){
        System.out.println("do test2");
        Pojo p = new Pojo();
        p.setTest(test+test5+test3+test4);
        return p;
    }

    @Route("user/a")
    @Json
    public Pojo test(String test,String test5){
        System.out.println("do test2a");
        Pojo p = new Pojo();
        p.setTest(test+test5);
        return p;
    }

    public void test(){
        System.out.println("do test2");
        test.test();
    }

}
