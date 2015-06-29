/**
 * @Title: GsonUtil.java
 * @Package net.myscloud.pandora.utils
 * @Description: 
 * Copyright: Copyright (c) 2015 
 * Company:杭州点望科技有限公司
 */
package net.myscloud.pandora.common.util;

import com.google.gson.Gson;

/**
 * @ClassName: GsonUtil
 * @Description: 
 */
public class GsonUtil {
	private static Gson gson = new Gson();

	/**
	 * getter method
	 * @return the gson
	 */
	public static Gson getGson() {
		return gson;
	}
}
