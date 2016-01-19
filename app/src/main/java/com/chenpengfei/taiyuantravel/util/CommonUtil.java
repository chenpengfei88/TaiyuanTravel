package com.chenpengfei.taiyuantravel.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.chenpengfei.taiyuantravel.R;
import com.chenpengfei.taiyuantravel.customview.CustomToast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description  工具类
 */
public class CommonUtil {

    // 屏幕的宽参数
    public static int screenWidth;
    public static float screenDensity;
    // 屏幕的宽参数
    public static int screenHeight;

    /**
     *
     * @Description描述:初始化屏幕信息
     * @return void
     */
    public static void initScreeenInfomation(Resources resources) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = resources.getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        screenDensity = dm.density;
    }

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
         ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
         NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
         NetworkInfo wimaxInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
        boolean connected = (null != wimaxInfo && wimaxInfo.isConnectedOrConnecting());
        if (!connected)
            connected = (null != mobileInfo && mobileInfo.isConnectedOrConnecting());
        if (!connected)
            connected = (null != wifiInfo && wifiInfo.isConnectedOrConnecting());
        if(!connected) {
            CustomToast.makeText(context, context.getString(R.string.tip_network_fail), Toast.LENGTH_SHORT).show();
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
