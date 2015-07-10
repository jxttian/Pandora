package net.myscloud.pandora.mvc.bind.method;

import net.myscloud.pandora.mvc.bind.enums.RequestMethod;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by user on 2015/7/8.
 */
public class MethodDetail {
    private String url;
    private Method method;
    private String className;
    private RequestMethod requestMethod;
    private Map<String, Class> paramsMap;

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Map<String, Class> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, Class> paramsMap) {
        this.paramsMap = paramsMap;
    }
}
