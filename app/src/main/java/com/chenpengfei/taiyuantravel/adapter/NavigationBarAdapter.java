package com.chenpengfei.taiyuantravel.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description  导航栏viewpager适配器
 */
public class NavigationBarAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentArrayList = null;

    public NavigationBarAdapter(FragmentManager supportFragmentManager, ArrayList<Fragment> fragmentArrayList) {
        super(supportFragmentManager);
        this.fragmentArrayList = fragmentArrayList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentArrayList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentArrayList != null ? fragmentArrayList.size() : 0;
    }
}
