package com.chenpengfei.taiyuantravel.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

    /**
     *
     * @param context
     * @return boolean
     * @description 检查是否有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        if (null == context)
            return false;
         ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo mobileInfo = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
         NetworkInfo wifiInfo = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
         NetworkInfo wimaxInfo = connec.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);

        boolean connected = (null != wimaxInfo && wimaxInfo.isConnectedOrConnecting());
        if (!connected)
            connected = (null != mobileInfo && mobileInfo.isConnectedOrConnecting());
        if (!connected)
            connected = (null != wifiInfo && wifiInfo.isConnectedOrConnecting());
        if (!connected) {
        }
        return connected;
    }

    /**
     *
     * @param activity
     * @description 隐藏键盘
     */
    public static void hideSoftInputWindow(Activity activity) {
        if (activity == null || activity.getCurrentFocus() == null) return;
        ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE)).
                hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
