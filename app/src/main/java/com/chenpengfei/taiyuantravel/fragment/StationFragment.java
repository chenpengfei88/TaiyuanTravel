package com.chenpengfei.taiyuantravel.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.chenpengfei.taiyuantravel.R;
import com.chenpengfei.taiyuantravel.activity.PoiAddressActivity;
import com.chenpengfei.taiyuantravel.adapter.BusStationListExpandableAdapter;
import com.chenpengfei.taiyuantravel.pojo.EventType;
import com.chenpengfei.taiyuantravel.util.Const;
import com.ypy.eventbus.EventBus;
import java.util.ArrayList;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description 显示站点内容的fragment
 */
public class StationFragment extends Fragment implements OnGetBusLineSearchResultListener {

    private PoiSearch mPoiSearch, mmPoisearch;
    private TextView searchStationText;
    private BusLineSearch mBusLineSearch = null;
    private String[] busArray;
    private int busPosition = -1;
    private ArrayList<BusLineResult> busLineResultArrayList = new ArrayList<BusLineResult>(); //公交线路结果集合
    private ExpandableListView expandableListView;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            busPosition += 1;
            if(busPosition < busArray.length) {
                String busName = busArray[busPosition];
                mmPoisearch.searchInCity((new PoiCitySearchOption()).city(getResources().getString(R.string.search_city)).keyword(busName));
            } else {
                expandableListView.setAdapter(new BusStationListExpandableAdapter(getActivity().getLayoutInflater(), getActivity().getApplication(), busLineResultArrayList));
            }
        }
    };

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
        View view = getLayoutInflater(getArguments()).inflate(R.layout.activity_main_station, null);
        searchStationText = (TextView) view.findViewById(R.id.text_station_search);
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_station_bus_list);
        expandableListView.setGroupIndicator(null);
        //listview展开事件
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                View view = expandableListView.getChildAt(i);
                if(view!=null){
                    ImageView arrowImageView = (ImageView) view.findViewById(R.id.image_main_route_line_arrow);
                    if(arrowImageView!=null){
                        arrowImageView.setImageResource(R.drawable.icon_up_arrow);
                    }
                }
            }
        });
        //listview合闭事件
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                View view = expandableListView.getChildAt(i);
                if (view != null) {
                    ImageView arrowImageView = (ImageView) view.findViewById(R.id.image_main_route_line_arrow);
                    if (arrowImageView != null) {
                        arrowImageView.setImageResource(R.drawable.icon_down_arrow);
                    }
                }
            }
        });
        mPoiSearch = PoiSearch.newInstance();
        mmPoisearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        mmPoisearch.setOnGetPoiSearchResultListener(mpoiListener);
        mBusLineSearch = BusLineSearch.newInstance();
        mBusLineSearch.setOnGetBusLineSearchResultListener(this);
        return  view;
    }

    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){

        public void onGetPoiResult(PoiResult result){
            if(result == null || result.getAllPoi() == null) {
                Toast.makeText(getActivity(), "ij", Toast.LENGTH_SHORT).show();
                return;
            }
            //遍历所有POI，找到类型为站点的POI
            String busStr = "";
            for (PoiInfo poi : result.getAllPoi()) {
                if (poi.type == PoiInfo.POITYPE.BUS_STATION  || poi.type == PoiInfo.POITYPE.SUBWAY_STATION) {
                    //说明该条POI为站点信息
                    busStr = poi.address;
                    break;
                }
            }
            if(!TextUtils.isEmpty(busStr)) {
                busArray = busStr.split(";");
                if(busArray.length > 0) {
                    mHandler.sendEmptyMessage(0);
                }
            }
        }
        public void onGetPoiDetailResult(PoiDetailResult result){
        }
    };

    OnGetPoiSearchResultListener mpoiListener = new OnGetPoiSearchResultListener(){

        public void onGetPoiResult(PoiResult result){
            if(result == null || result.getAllPoi() == null) {
                mHandler.sendEmptyMessage(0);
                return;
            }
            String busLineId = "";
            for (PoiInfo poi : result.getAllPoi()) {
                if (poi.type == PoiInfo.POITYPE.BUS_LINE  || poi.type == PoiInfo.POITYPE.SUBWAY_LINE) {
                    //说明该条POI为公交信息
                    busLineId = poi.uid;
                    break;
                }
            }
            if(!TextUtils.isEmpty(busLineId)) {
                mBusLineSearch.searchBusLine((new BusLineSearchOption().city(getResources().getString(R.string.search_city)).uid(busLineId)));
            } else {
                mHandler.sendEmptyMessage(0);
            }
        }
        public void onGetPoiDetailResult(PoiDetailResult result){
        }
    };

    public void onEventMainThread(EventType eventType) {
        switch (eventType.getType()){
            //点击搜索按钮
            case Const.EVENTBUS_EVENT_TYPE_SIX:
                if(getActivity() == null) return;
                Intent intent = new Intent(getActivity(), PoiAddressActivity.class);
                intent.putExtra("address_type", Const.MAIN_SEARCH_ADDRESS_RESULT_REQUEST_THREE);
                startActivityForResult(intent, Const.MAIN_SEARCH_ADDRESS_RESULT_REQUEST_THREE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null) return;
        //开始地址选择以后
        if(requestCode == Const.MAIN_SEARCH_ADDRESS_RESULT_REQUEST_THREE && resultCode == Const.MAIN_SEARCH_ADDRESS_RESULT_REQUEST_THREE) {
            SuggestionResult.SuggestionInfo suggestionInfo = (SuggestionResult.SuggestionInfo) data.getParcelableExtra("suggestionsInfo");
            searchStationText.setText(suggestionInfo.key);
            mPoiSearch.searchInCity((new PoiCitySearchOption()).city(getResources().getString(R.string.search_city)).keyword(suggestionInfo.key));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onGetBusLineResult(BusLineResult busLineResult) {
        busLineResultArrayList.add(busLineResult);
        mHandler.sendEmptyMessage(0);
    }
}
