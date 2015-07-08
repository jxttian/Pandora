package net.myscloud.pandora.common.util;


/**
 * Created by user on 2015/7/6.
 */
public class StringUtil {

    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    public static final char FILE_SEPARATOR_CHAR = '/';

    /**
     * 判断字符串是否为空
     * @param cs
     * @return
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 判断字符串是否不为空
     * @param cs
     * @return
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }
}
