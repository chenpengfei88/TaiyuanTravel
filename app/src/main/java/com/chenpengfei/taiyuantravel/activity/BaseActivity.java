package com.chenpengfei.taiyuantravel.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.MenuItem;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description activity基础类
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateActivity(savedInstanceState);
    }

    /**
     *
     * @param actionBarTitle title内容
     * @description 设置title的字体内容和字体大小
     */
    public void setActionBarTitle(String actionBarTitle, boolean isDiaplayHomeAsUp) {
        ActionBar actionBar = getActionBar();
        if(actionBar == null) return;
        SpannableStringBuilder titleContent = new SpannableStringBuilder(actionBarTitle);
        titleContent.setSpan(new AbsoluteSizeSpan(55), 0, actionBarTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //设置字体大小
        actionBar.setTitle(titleContent);
        actionBar.setDisplayHomeAsUpEnabled(isDiaplayHomeAsUp);
    }

    /**
     *
     * @param content 内容ID
     * @return String
     * @description 根据ID得到string内容
     */
    public String getStringContent(int content) {
        return getResources().getString(content);
    }

    /**
     *
     * @param content 内容ID
     * @return String
     * @description 根据ID得到string  替换以后的内容
     */
    public String getStringContentFormat(int content, String replace) {
        return getResources().getString(content, replace);
    }

    protected void onCreateActivity(Bundle savedInstanceState){
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
