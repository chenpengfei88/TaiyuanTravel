package com.chenpengfei.taiyuantravel.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.chenpengfei.taiyuantravel.R;
import com.chenpengfei.taiyuantravel.adapter.BusStationListAdapter;
import com.chenpengfei.taiyuantravel.customview.CustomToast;
import com.chenpengfei.taiyuantravel.pojo.EventType;
import com.chenpengfei.taiyuantravel.util.CommonUtil;
import com.chenpengfei.taiyuantravel.util.Const;
import com.chenpengfei.taiyuantravel.util.DateUtils;
import com.ypy.eventbus.EventBus;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description 显示线路内容的fragment
 */
public class RouteFragment extends BaseFragment implements OnGetBusLineSearchResultListener, OnGetPoiSearchResultListener {

    private BusLineSearch mBusLineSearch = null;
    private PoiSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private EditText searchEdit;
    private ListView routeLineListView;
    private String busLineId; //公交ID
    private TextView routeLineNameText, routeLineStartTimeText, routeLineEndTimeAndStationCountText; //线路名字, 线路开始时间，线路结束时间和站点数量,点击按钮搜索线路
    private LinearLayout timeAndStationCountLinearLayout; //时间和车站数量布局linarlayout
    private ImageView deleteSearchImage; //清空搜索内容
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        if(mSearch != null) mSearch.destroy();
        if(mBusLineSearch != null) mBusLineSearch.destroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = getLayoutInflater(getArguments()).inflate(R.layout.activity_main_route, null);
        searchEdit = (EditText) view.findViewById(R.id.edit_route_line_search); //搜索框
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(searchEdit.getText().toString())) {
                    deleteSearchImage.setVisibility(View.VISIBLE);
                } else {
                    deleteSearchImage.setVisibility(View.GONE);
                }
            }
        });
        routeLineNameText = (TextView) view.findViewById(R.id.text_route_line_name); //线路名
        routeLineStartTimeText = (TextView) view.findViewById(R.id.text_route_line_start_time); //线路开始时间和站点数量
        routeLineEndTimeAndStationCountText = (TextView) view.findViewById(R.id.text_route_line_end_time_station_count); //线路结束时间和站点数量
        timeAndStationCountLinearLayout = (LinearLayout) view.findViewById(R.id.linearlayout_route_line_time_station_count);
        deleteSearchImage = (ImageView) view.findViewById(R.id.image_route_line_search_delete); //清空搜索图标
        routeLineListView = (ListView) view.findViewById(R.id.list_route_line_search);
        mSearch = PoiSearch.newInstance();
        mSearch.setOnGetPoiSearchResultListener(this);
        mBusLineSearch = BusLineSearch.newInstance();
        mBusLineSearch.setOnGetBusLineSearchResultListener(this);
        return view;
    }


    public void onEventMainThread(EventType eventType) {
        switch (eventType.getType()){
            //点击搜索按钮
            case Const.EVENTBUS_EVENT_TYPE_FIVE:
                if(!CommonUtil.isNetworkAvailable(getActivity().getApplicationContext())) return;
                if(!TextUtils.isEmpty(eventType.getContent()))
                    searchEdit.setText(eventType.getContent());
                String addressKeyword = searchEdit.getText().toString();
                if(!TextUtils.isEmpty(addressKeyword)) {
                    deleteSearchImage.setVisibility(View.VISIBLE);
                    showLoadView(view, getActivity());
                    CommonUtil.hideSoftInputWindow(getActivity());
                    mSearch.searchInCity((new PoiCitySearchOption()).city(getResources().getString(R.string.search_city)).keyword(addressKeyword));
                } else {
                    deleteSearchImage.setVisibility(View.GONE);
                }
                break;
            //清空图标按钮事件
            case Const.EVENTBUS_EVENT_TYPE_SEVEN:
                searchEdit.setText("");
                break;
        }
    }
    @Override
    public void onGetBusLineResult(BusLineResult busLineResult) {
        if (busLineResult == null || busLineResult.error != SearchResult.ERRORNO.NO_ERROR || getActivity() == null) {
            CustomToast.makeText(getActivity(), getResources().getString(R.string.toast_no_result), Toast.LENGTH_SHORT).show();
            hideLoadView();
            return;
        }
        routeLineNameText.setText(busLineResult.getBusLineName());
        routeLineNameText.setVisibility(View.VISIBLE);
        routeLineStartTimeText.setText(DateUtils.getDateStr(busLineResult.getStartTime(), DateUtils.DATE_PATTERN_FIVE)); //早班时间
        routeLineEndTimeAndStationCountText.setText(DateUtils.getDateStr(busLineResult.getEndTime(), DateUtils.DATE_PATTERN_FIVE) + "         共" + busLineResult.getStations().size() + getResources().getString(R.string.string_route_line_station_end)); //晚班时间
        timeAndStationCountLinearLayout.setVisibility(View.VISIBLE);
        routeLineListView.setAdapter(new BusStationListAdapter(busLineResult.getStations(), getActivity().getLayoutInflater()));
        hideLoadView();
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            CustomToast.makeText(getActivity(), getResources().getString(R.string.toast_no_result), Toast.LENGTH_SHORT).show();
            hideLoadView();
            return;
        }
        //遍历所有POI，找到类型为公交线路的POI
        for (PoiInfo poi : result.getAllPoi()) {
            if (poi.type == PoiInfo.POITYPE.BUS_LINE ||poi.type == PoiInfo.POITYPE.SUBWAY_LINE) {
                //说明该条POI为公交信息，获取该条POI的UID
                busLineId = poi.uid;
                break;
            }
        }
        if(!TextUtils.isEmpty(busLineId)) {
            mBusLineSearch.searchBusLine((new BusLineSearchOption().city(getResources().getString(R.string.search_city)).uid(busLineId)));
        } else {
            CustomToast.makeText(getActivity(), getResources().getString(R.string.toast_no_result), Toast.LENGTH_SHORT).show();
            hideLoadView();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
    }
}
