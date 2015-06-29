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
