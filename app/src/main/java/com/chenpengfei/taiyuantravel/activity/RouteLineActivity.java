package com.chenpengfei.taiyuantravel.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
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
public class RouteLineActivity extends BaseActivity implements OnGetRoutePlanResultListener {

    private LatLng startLatLng, endLatLng;
    private PlanNode startNode, endNode; //开始和结束地点node
    private RoutePlanSearch mSearch = null; // 搜索模块
    private ExpandableListView stationProgrammeExpandableListView;
    private ArrayList<StationProgramme> stationProgrammeArrayList = new ArrayList<StationProgramme>(); //线路方案数据list
    private String programmeName = ""; //公交;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_route_line);
        setToolBarStyle(getStringContent(R.string.title_staion_programme));
        initView();
        initMapSearch();
    }

    private void initView() {
        startLatLng = getIntent().getParcelableExtra("start_address_lat");
        startNode = PlanNode.withLocation(startLatLng);
        endLatLng = getIntent().getParcelableExtra("end_address_lat");
        endNode = PlanNode.withLocation(endLatLng);
        stationProgrammeExpandableListView = (ExpandableListView) findViewById(R.id.expandable_main_station_programme);
        stationProgrammeExpandableListView.setGroupIndicator(null);
    }

    private void initMapSearch() {
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        mSearch.transitSearch((new TransitRoutePlanOption()).from(startNode).city("太原").to(endNode));
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
                StringBuffer routeLineStringBuffer = new StringBuffer();
                for(int j = 0; j < stepListSize; j++){
                    String routeLineItem = ((TransitRouteLine.TransitStep) transitStepList.get(j)).getInstructions();
                    routeLineStringBuffer.append(routeLineItem);
                    transitStepArrayList.add(new StationProgramme(routeLineItem));
                }
                programmeName = "";
                getRouteLineDetailData(routeLineStringBuffer.toString(), getStringContent(R.string.string_route_line_transit_start), getStringContent(R.string.string_route_line_transit_end));
                stationProgramme.setProgrammeName(programmeName);
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


    private void getRouteLineDetailData(String content, String startTransit, String endTransit) {
        //截取公交
        int transitStartIndex = content.indexOf(startTransit);
        if(transitStartIndex < 0) return;
        String c = content.substring(transitStartIndex + startTransit.length());
        int transitEndIndex = c.indexOf(endTransit);

        String transitInt = content.substring(transitStartIndex + startTransit.length(), transitStartIndex + startTransit.length() + transitEndIndex);
        programmeName = programmeName + (TextUtils.isEmpty(programmeName) ? "" : "-") + transitInt;
        content = content.replace(startTransit + transitInt + endTransit, "");
        getRouteLineDetailData(content, getStringContent(R.string.string_route_line_transit_start), getStringContent(R.string.string_route_line_transit_end));
    }
}
