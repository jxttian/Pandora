package net.myscloud.pandora.mvc.bind.method;

import net.myscloud.pandora.mvc.bind.enums.RequestMethod;

import java.lang.reflect.Method;

/**
 * Created by user on 2015/7/8.
 */
public class MethodDetail {
    private String url;
    private Method method;
    private RequestMethod requestMethod;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }
}
