package com.chenpengfei.taiyuantravel.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.chenpengfei.taiyuantravel.R;
import com.chenpengfei.taiyuantravel.activity.PoiAddressActivity;
import com.chenpengfei.taiyuantravel.activity.RouteLineActivity;
import com.chenpengfei.taiyuantravel.customview.CustomToast;
import com.chenpengfei.taiyuantravel.pojo.ChildSuggestionInfo;
import com.chenpengfei.taiyuantravel.pojo.EventType;
import com.chenpengfei.taiyuantravel.util.BaiduLocationUtil;
import com.chenpengfei.taiyuantravel.util.Const;
import com.ypy.eventbus.EventBus;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description 显示首页内容的fragment
 */
public class MainFragment extends Fragment {

    private TextView startAddressText, endAddressText;
    private SuggestionResult.SuggestionInfo startSuggestionInfo, endSuggestionInfo; //开始和结束地址信息
    private String startAddress, endAddress;
    private boolean startExchangeAnimationEnd = true, endExchangeAnimationEnd = true, isChange; //地址交换动画是否已经完成, ischange 是否执行过动画

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutInflater(getArguments()).inflate(R.layout.activity_main_main, null);
        startAddressText = (TextView) view.findViewById(R.id.text_main_start_address);
        endAddressText = (TextView) view.findViewById(R.id.text_main_end_address);
        BaiduLocationUtil.locationAddress(new BDLocationListener(){
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                String addressStr = bdLocation.getAddrStr();
                if(!TextUtils.isEmpty(addressStr)) {
                    startAddressText.setText(addressStr.replace(getResources().getString(R.string.tab_main_china), ""));
                    startSuggestionInfo = new ChildSuggestionInfo(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()), addressStr);
                    startAddress = startSuggestionInfo.key;
                } else {
                    startAddressText.setText(getResources().getString(R.string.edit_main_start_address_location_fail));
                }
            }
        }, getActivity().getApplicationContext());
        return view;
    }

    public void onEventMainThread(EventType eventType) {
        switch (eventType.getType()){
            //选择开始地址
            case Const.EVENTBUS_EVENT_TYPE_ONE:
                if(getActivity() == null) return;
                Intent intent = new Intent(getActivity(), PoiAddressActivity.class);
                intent.putExtra("address_type", Const.MAIN_SEARCH_ADDRESS_RESULT_REQUEST_ONE);
                startActivityForResult(intent, Const.MAIN_SEARCH_ADDRESS_RESULT_REQUEST_ONE);
                break;
            //选择结束地址
            case Const.EVENTBUS_EVENT_TYPE_TWO:
                if(getActivity() == null) return;
                Intent intentTwo = new Intent(getActivity(), PoiAddressActivity.class);
                intentTwo.putExtra("address_type", Const.MAIN_SEARCH_ADDRESS_RESULT_REQUEST_TWO);
                startActivityForResult(intentTwo, Const.MAIN_SEARCH_ADDRESS_RESULT_REQUEST_TWO);
                break;
            //点击查询
            case Const.EVENTBUS_EVENT_TYPE_THREE:
                if(getActivity() == null || startSuggestionInfo == null || endSuggestionInfo == null || startSuggestionInfo.pt == null || endSuggestionInfo.pt == null) return;
                Intent intentThree = new Intent(getActivity(), RouteLineActivity.class);
                //地址经纬度
                intentThree.putExtra("start_address_lat", startSuggestionInfo.pt);
                intentThree.putExtra("end_address_lat",  endSuggestionInfo.pt);
                startActivity(intentThree);
                break;
            //地址交换
            case Const.EVENTBUS_EVENT_TYPE_FOUR:
                if(getActivity() == null || (TextUtils.isEmpty(startAddressText.getText().toString()) || TextUtils.isEmpty(endAddressText.getText().toString())) || (!startExchangeAnimationEnd && !endExchangeAnimationEnd)) return;
                if(!TextUtils.isEmpty(startAddress) && !TextUtils.isEmpty(endAddress)){
                    startAddressText.setText(startAddress);
                    startAddressText.setTag(startSuggestionInfo);
                    endAddressText.setText(endAddress);
                    endAddressText.setTag(endSuggestionInfo);
                }
                isChange = true;
                Animation startAddressAnimaiton = AnimationUtils.loadAnimation(getActivity(), R.anim.text_start_address_translate);
                startAddressText.startAnimation(startAddressAnimaiton);
                startAddressAnimaiton.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        startExchangeAnimationEnd = false;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        endAddress = startAddressText.getText().toString();
                        endSuggestionInfo = (SuggestionResult.SuggestionInfo) startAddressText.getTag();
                        startExchangeAnimationEnd = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                Animation endAddressAnimaiton = AnimationUtils.loadAnimation(getActivity(), R.anim.text_end_address_translate);
                endAddressText.startAnimation(endAddressAnimaiton);
                endAddressAnimaiton.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        endExchangeAnimationEnd = false;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startAddress = endAddressText.getText().toString();
                        startSuggestionInfo = (SuggestionResult.SuggestionInfo) endAddressText.getTag();
                        endExchangeAnimationEnd = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null) return;
        //开始地址选择以后
        if(requestCode == Const.MAIN_SEARCH_ADDRESS_RESULT_REQUEST_ONE && resultCode == Const.MAIN_SEARCH_ADDRESS_RESULT_REQUEST_ONE) {
            startSuggestionInfo = (SuggestionResult.SuggestionInfo) data.getParcelableExtra("suggestionsInfo");
            startAddress = startSuggestionInfo.key;
            if(isChange) {
                endAddressText.setText(startAddress);
            } else
                startAddressText.setText(startAddress);
        }
        //结束地址选择以后
        if(requestCode == Const.MAIN_SEARCH_ADDRESS_RESULT_REQUEST_TWO && resultCode == Const.MAIN_SEARCH_ADDRESS_RESULT_REQUEST_TWO) {
            endSuggestionInfo = (SuggestionResult.SuggestionInfo) data.getParcelableExtra("suggestionsInfo");
            endAddress = endSuggestionInfo.key;
            if(isChange){
                startAddressText.setText(endAddress);
            } else
                endAddressText.setText(endAddress);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        BaiduLocationUtil.mLocationClient.stop();
        super.onDestroyView();
    }
}
