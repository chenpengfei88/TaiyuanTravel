package com.chenpengfei.taiyuantravel.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.mapapi.SDKInitializer;
import com.chenpengfei.taiyuantravel.R;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description ActionBar activity基础类
 */
public class BaseActionBarActivity extends ActionBarActivity {

    private RelativeLayout loadRelativeLayout;
    private ImageView loadImage;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        onCreateActivity(savedInstanceState);
    }

    /**
     *
     * @param context
     * @description 显示加载view
     */
    public void showLoadView(Context context) {
        if(context == null) return;
        if(loadRelativeLayout == null)
            loadRelativeLayout = (RelativeLayout) findViewById(R.id.relative_load);
        if(loadImage == null)
            loadImage = (ImageView) findViewById(R.id.image_load);
        loadRelativeLayout.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.image_load_rotate);
        loadImage.startAnimation(animation);
    }

    public void hideLoadView() {
        if(loadRelativeLayout != null)
            loadRelativeLayout.setVisibility(View.GONE);
        if(loadImage != null && animation != null && !animation.hasEnded()) {
            animation.cancel();
        }
    }

    /**
     *
     * @param toolBarTitle
     * @description 设置toolbar样式
     */
    public void setToolBarStyle(String toolBarTitle) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        SpannableStringBuilder titleContent = new SpannableStringBuilder(toolBarTitle);
        titleContent.setSpan(new AbsoluteSizeSpan(52), 0, toolBarTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //设置字体大小
        mToolbar.setTitle(titleContent);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
    }

    /**
     * @param view
     * @description 给toolbar添加view
     */
    public void toolBarAddView(View view) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
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
