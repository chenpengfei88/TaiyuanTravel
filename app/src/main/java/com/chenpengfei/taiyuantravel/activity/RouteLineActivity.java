package com.chenpengfei.taiyuantravel.activity;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.chenpengfei.taiyuantravel.pojo.StationProgramme;
import com.chenpengfei.taiyuantravel.R;
import com.chenpengfei.taiyuantravel.adapter.StationProgrammeExpandableAdapter;
import com.chenpengfei.taiyuantravel.customview.CustomToast;

import java.util.ArrayList;
import java.util.List;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description 线路结果activity，输入起点和终点查询得到的线路list数据
 */
public class RouteLineActivity extends BaseActivity implements OnGetGeoCoderResultListener, OnGetRoutePlanResultListener {

    private GeoCoder geoSearch = null; //地址转换经纬度
    private String startAddress, endAddress;
    private PlanNode startNode, endNode; //开始和结束地点node
    private RoutePlanSearch mSearch = null; // 搜索模块
    private ExpandableListView stationProgrammeExpandableListView;
    private ArrayList<StationProgramme> stationProgrammeArrayList = new ArrayList<StationProgramme>(); //线路方案数据list

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_route_line);
        setActionBarTitle(getStringContent(R.string.title_staion_programme), true);
        initView();
        initMapSearch();
    }

    private void initView() {
        startAddress = getIntent().getStringExtra("startAddress");
        endAddress = getIntent().getStringExtra("endAddress");
        stationProgrammeExpandableListView = (ExpandableListView) findViewById(R.id.expandable_main_station_programme);
        stationProgrammeExpandableListView.setGroupIndicator(null);
    }

    private void initMapSearch() {
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        // 初始化地址转换模块，注册事件监听
        geoSearch = GeoCoder.newInstance();
        geoSearch.setOnGetGeoCodeResultListener(this);
        geoSearch.geocode(new GeoCodeOption().city(getStringContent(R.string.search_city)).address("太原站"));
    }


    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            CustomToast.makeText(RouteLineActivity.this, getStringContent(R.string.toast_no_result), Toast.LENGTH_SHORT).show();
            return;
        }
        if(startNode == null) {
            startNode = PlanNode.withLocation(new LatLng(geoCodeResult.getLocation().latitude, geoCodeResult.getLocation().longitude));
            geoSearch.geocode(new GeoCodeOption().city(getStringContent(R.string.search_city)).address("学府街"));
        } else {
            endNode = PlanNode.withLocation(new LatLng(geoCodeResult.getLocation().latitude, geoCodeResult.getLocation().longitude));
            mSearch.transitSearch((new TransitRoutePlanOption()).from(startNode).city(getStringContent(R.string.search_city)).to(endNode));
        }

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        if (transitRouteResult == null || transitRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            CustomToast.makeText(RouteLineActivity.this, getStringContent(R.string.toast_no_result), Toast.LENGTH_SHORT).show();
        }
        if (transitRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            return;
        }
        if (transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            List<TransitRouteLine> transitRouteLineList = transitRouteResult.getRouteLines();
            int routeLineListSize = transitRouteLineList.size();
            //遍历获取线路详情数据
            for(int i = 0; i < routeLineListSize; i++) {
                StationProgramme stationProgramme = new StationProgramme(getStringContentFormat(R.string.title_station_programme, "-" + (i +1)));
                TransitRouteLine transitRouteLine = transitRouteLineList.get(i);
                List<TransitRouteLine.TransitStep> transitStepList = transitRouteLine.getAllStep();
                int stepListSize = transitStepList.size();
                ArrayList<StationProgramme> transitStepArrayList = new ArrayList<StationProgramme>();
                int walkLength, stationCount; //步行的长度，经过多少站
                String programmeName; //公交;
               for(int j = 0; j < stepListSize; j++){
                    transitStepArrayList.add(new StationProgramme(((TransitRouteLine.TransitStep) transitStepList.get(j)).getInstructions()));
                }
                stationProgramme.setChildStationProgrammeList(transitStepArrayList);
                stationProgrammeArrayList.add(stationProgramme);
            }
            stationProgrammeExpandableListView.setAdapter(new StationProgrammeExpandableAdapter(getLayoutInflater(), stationProgrammeArrayList));
            if(stationProgrammeArrayList.size() > 0) stationProgrammeExpandableListView.expandGroup(0);
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
    }

    private void getCount(String content, String start, String end) {
        int startIndex = content.indexOf(start);
        int endIndex = content.indexOf(end);
        String str = content.substring(startIndex, endIndex);
    }
}
