package com.chenpengfei.taiyuantravel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import com.chenpengfei.taiyuantravel.R;
import com.chenpengfei.taiyuantravel.adapter.NavigationBarAdapter;
import com.chenpengfei.taiyuantravel.fragment.MainFragment;
import com.chenpengfei.taiyuantravel.fragment.RouteFragment;
import com.chenpengfei.taiyuantravel.fragment.StationFragment;
import java.util.ArrayList;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description 主页activity，负责显示导航栏，还有导航栏里单个按钮对应的fragmeng显示。
 */
public class MainActivity extends BaseActivity {

    private int TAB_COUNT = 3;
    private ViewPager contentViewPager; //显示fragment内容viewpager
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<Fragment>();
    private ArrayList<RadioButton> radioButtonArrayList = new ArrayList<RadioButton>();
    private FragmentManager fragmentManager;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        setActionBarTitle(getStringContent(R.string.app_name), false);
        fragmentManager = getSupportFragmentManager();
        onGetInstanceState(savedInstanceState);
        initView();
    }

    //初始化view
    private void initView(){
        //初始化三个fragment，然后添加到list中，通过适配器展示在视图中。
        contentViewPager = (ViewPager) findViewById(R.id.viewpager_main_content);
        if(fragmentArrayList.size() < 3) {
            fragmentArrayList.clear();
            fragmentArrayList.add(new MainFragment());
            fragmentArrayList.add(new StationFragment());
            fragmentArrayList.add(new RouteFragment());
        }
        contentViewPager.setAdapter(new NavigationBarAdapter(fragmentManager, fragmentArrayList));
        contentViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { //page滑动改变
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                radioButtonArrayList.get(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        //初始化三个radiobutton，然后添加到list中，方便找到某个radiobutton然后选中
        radioButtonArrayList.add((RadioButton) findViewById(R.id.radiobutton_main_main));
        radioButtonArrayList.add((RadioButton) findViewById(R.id.radiobutton_main_station));
        radioButtonArrayList.add((RadioButton) findViewById(R.id.radiobutton_main_route));
    }

    //相应点击事件
    public void btnClick(View v) {
        switch (v.getId()){
            case R.id.radiobutton_main_main:
                contentViewPager.setCurrentItem(0);
                break;
            case R.id.radiobutton_main_station:
                contentViewPager.setCurrentItem(1);
                break;
            case R.id.radiobutton_main_route:
                contentViewPager.setCurrentItem(2);
                break;
            case R.id.button_main_search: //点击查询按钮
                startActivity(new Intent(MainActivity.this, RouteLineActivity.class));
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        if(fragmentArrayList == null || fragmentArrayList.size() == 0) return;
//        for (int i = 0; i < TAB_COUNT; i++) {
//            Fragment fragment = fragmentArrayList.get(i);
//            if (fragment != null) {
//                fragmentManager.putFragment(outState, "fragment" + i, fragment);
//            }
//        }
    }

    //如果activity不是在正常情况下被杀死就会调用onSaveInstanceState方法保存当前的fragment，然后重新创建activity的时候从outState中取出来
    public void onGetInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            for (int i = 0; i < TAB_COUNT; i++) {
                Fragment fragment = fragmentManager.getFragment(savedInstanceState, "fragment" + i);
                fragmentArrayList.add(fragment);
            }
        }
    }
}
