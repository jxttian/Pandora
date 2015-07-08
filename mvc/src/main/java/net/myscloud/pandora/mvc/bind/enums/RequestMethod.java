package net.myscloud.pandora.mvc.bind.enums;

import io.netty.handler.codec.http.HttpMethod;

/**
 * @ClassName: RequestMethod
 * @Description: 
 */
public enum RequestMethod {
	GET(HttpMethod.GET),POST(HttpMethod.POST),PUT(HttpMethod.PUT),DELETE(HttpMethod.DELETE);
	private HttpMethod method;
	RequestMethod(HttpMethod method){
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
