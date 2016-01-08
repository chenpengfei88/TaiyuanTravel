package com.chenpengfei.taiyuantravel.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chenpengfei.taiyuantravel.R;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description 显示线路内容的fragment
 */
public class RouteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  getLayoutInflater(getArguments()).inflate(R.layout.activity_main_route, null);
    }
}
