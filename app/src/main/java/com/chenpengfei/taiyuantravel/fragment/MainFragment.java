package com.chenpengfei.taiyuantravel.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.chenpengfei.taiyuantravel.R;
import com.chenpengfei.taiyuantravel.activity.PoiAddressActivity;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description 显示首页内容的fragment
 */
public class MainFragment extends Fragment {

    private EditText startAddressEdit, endAddressEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutInflater(getArguments()).inflate(R.layout.activity_main_main, null);
        startAddressEdit = (EditText) view.findViewById(R.id.edit_main_start_address);
        endAddressEdit = (EditText) view.findViewById(R.id.edit_main_end_address);
        return view;
    }
}
