package com.chenpengfei.taiyuantravel.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description  工具类
 */
public class CommonUtil {

    /**
     *
     * @param str 字符串
     * @return boolean
     * @description 判断字符串是否是数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if(!isNum.matches()) {
            return false;
        }
        return true;
    }
}
