package com.chenpengfei.taiyuantravel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.busline.BusLineResult;
import com.chenpengfei.taiyuantravel.R;
import com.chenpengfei.taiyuantravel.pojo.StationProgramme;
import com.chenpengfei.taiyuantravel.util.DateUtils;

import java.util.ArrayList;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description  显示单个公交站点关联的公交车listview的适配器
 */
public class BusStationListExpandableAdapter extends BaseExpandableListAdapter {

    private LayoutInflater lf;
    private ArrayList<BusLineResult> list = new ArrayList<BusLineResult>();
    private Context context;

    public BusStationListExpandableAdapter(LayoutInflater lf, Context context, ArrayList<BusLineResult> list){
        this.lf = lf;
        this.list = list;
        this.context = context;
    }
    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getStations().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getStations().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = lf.inflate(R.layout.activity_station_search_bus_item, null);
        }
        BusLineResult busLineResult = (BusLineResult)getGroup(groupPosition);
        ((TextView)convertView.findViewById(R.id.text_route_line_name)).setText(busLineResult.getBusLineName()); //公交线路名
        ((TextView)convertView.findViewById(R.id.text_route_line_start_time)).setText(DateUtils.getDateStr(busLineResult.getStartTime(), DateUtils.DATE_PATTERN_FIVE)); //公交开始时间
        ((TextView)convertView.findViewById(R.id.text_route_line_end_time_station_count)).setText(DateUtils.getDateStr(busLineResult.getEndTime(), DateUtils.DATE_PATTERN_FIVE) + "         共" + busLineResult.getStations().size() + context.getResources().getString(R.string.string_route_line_station_end)); //公交结束时间
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = lf.inflate(R.layout.activity_route_line_search_item, null);
        }
        BusLineResult.BusStation busStation = (BusLineResult.BusStation) getChild(groupPosition, childPosition);
        ((TextView) convertView.findViewById(R.id.text_route_line_search_content)).setText(busStation.getTitle());
        int position = childPosition + 1;
        ((TextView) convertView.findViewById(R.id.text_roule_line_bumber)).setText(position > 9 ? position + "" : "0" + position);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
