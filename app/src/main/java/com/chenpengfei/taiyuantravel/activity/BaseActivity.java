package com.chenpengfei.taiyuantravel.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.MenuItem;
import android.view.View;
import com.baidu.mapapi.SDKInitializer;
import com.chenpengfei.taiyuantravel.R;

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
        SDKInitializer.initialize(getApplicationContext());
        onCreateActivity(savedInstanceState);
    }

    /**
     *
     * @param toolBarTitle
     * @description 设置toolbar样式
     */
    public void setToolBarStyle(String toolBarTitle, int logo) {
        toolBarTitle = "  " + toolBarTitle;
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(logo > 0) {
            mToolbar.setLogo(logo);
        } else
            mToolbar.setLogo(R.drawable.icon_taiyuan_travel);
        SpannableStringBuilder titleContent = new SpannableStringBuilder(toolBarTitle);
        titleContent.setSpan(new AbsoluteSizeSpan(52), 0, toolBarTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //设置字体大小
        mToolbar.setTitle(titleContent);
    }

    /**
     * @param view
     * @description 给toolbar添加view
     */
    public void toolBarAddView(View view) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.addView(view);
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
     * @param content
     * @return String[]
     * @description 根据ID得到string数组内容
     */
    public String[] getStringContentArray(int content) {
        return getResources().getStringArray(content);
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
