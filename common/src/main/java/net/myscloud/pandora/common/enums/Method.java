/**
 * @Title: Method.java
 * @Package net.myscloud.pandora.controller.enums
 * @Description: 
 * Copyright: Copyright (c) 2015 
 * Company:杭州点望科技有限公司
 */
package net.myscloud.pandora.common.enums;

import io.netty.handler.codec.http.HttpMethod;

/**
 * @ClassName: Method
 * @Description: 
 */
public enum Method {
	GET(HttpMethod.GET),POST(HttpMethod.POST),PUT(HttpMethod.PUT),DELETE(HttpMethod.DELETE);
	private HttpMethod method;
	Method(HttpMethod method){
		this.method=method;
	}
	/**
	 * getter method
	 * @return the method
	 */
	public HttpMethod getMethod() {
		return method;
	}
	/**
	 * setter method
	 * @param method the method to set
	 */
	public void setMethod(HttpMethod method) {
		this.method = method;
	}
}
