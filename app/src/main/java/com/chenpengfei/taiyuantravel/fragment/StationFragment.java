package com.chenpengfei.taiyuantravel.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.chenpengfei.taiyuantravel.R;
import com.chenpengfei.taiyuantravel.activity.PoiAddressActivity;
import com.chenpengfei.taiyuantravel.customview.CustomToast;
import com.chenpengfei.taiyuantravel.pojo.EventType;
import com.chenpengfei.taiyuantravel.util.Const;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description 显示站点内容的fragment
 */
public class StationFragment extends BaseFragment {

    private PoiSearch mPoiSearch;
    private TextView searchStationText;
    private String[] busArray;
    private View view;
    private GridView busNameGridView;


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
        view = getLayoutInflater(getArguments()).inflate(R.layout.activity_main_station, null);
        searchStationText = (TextView) view.findViewById(R.id.text_station_search);
        busNameGridView = (GridView) view.findViewById(R.id.grid_station_bus_name);
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        return  view;
    }

    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){

        public void onGetPoiResult(PoiResult result){
            if(result == null || result.getAllPoi() == null) {
                CustomToast.makeText(getActivity(), getString(R.string.string_station_search_error, searchStationText.getText().toString()), Toast.LENGTH_SHORT).show();
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
                    List<Map<String, Object>> busList = new ArrayList<Map<String, Object>>();
                    for(String busName : busArray) {
                        Map<String,Object> busHashMap = new HashMap<String,Object>();
                        busHashMap.put("bus_name", busName);
                        busList.add(busHashMap);
                    }
                    SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), busList, R.layout.activity_station_search_bus_item, new String[]{ "bus_name"}, new int[]{R.id.text_station_bus_item_name});
                    busNameGridView.setAdapter(simpleAdapter);
                    hideLoadView();
                }
            } else {
                hideLoadView();
                CustomToast.makeText(getActivity(), getString(R.string.string_station_search_error, searchStationText.getText().toString()), Toast.LENGTH_SHORT).show();
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
            showLoadView(view, getActivity());
            mPoiSearch.searchInCity((new PoiCitySearchOption()).city(getResources().getString(R.string.search_city)).keyword(suggestionInfo.key));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
