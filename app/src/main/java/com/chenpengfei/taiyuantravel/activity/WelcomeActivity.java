package com.chenpengfei.taiyuantravel.activity;

import android.content.Intent;
import android.os.Bundle;
import com.chenpengfei.taiyuantravel.R;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description 欢迎页activity
 */
public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_welcome);
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }


}
