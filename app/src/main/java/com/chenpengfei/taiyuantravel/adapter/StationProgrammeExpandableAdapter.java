package com.chenpengfei.taiyuantravel.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.chenpengfei.taiyuantravel.Pojo.StationProgramme;
import com.chenpengfei.taiyuantravel.R;
import java.util.ArrayList;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description  显示线路方案listview的适配器
 */
public class StationProgrammeExpandableAdapter extends BaseExpandableListAdapter {

    private LayoutInflater lf;
    private ArrayList<StationProgramme> list = new ArrayList<StationProgramme>();

    public StationProgrammeExpandableAdapter(LayoutInflater lf, ArrayList<StationProgramme> list){
        this.lf = lf;
        this.list = list;
    }
    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getChildStationProgrammeList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getChildStationProgrammeList().get(childPosition);
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
            convertView = lf.inflate(R.layout.activity_station_programme_title_item, null);
        }
        ((TextView)convertView.findViewById(R.id.text_main_title_station_programme)).setText(((StationProgramme)getGroup(groupPosition)).getProgrammeName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = lf.inflate(R.layout.activity_station_programme_item, null);
        }
        StationProgramme groupName = (StationProgramme)getGroup(groupPosition);
        ((TextView) convertView.findViewById(R.id.text_main_station_programme)).setText(groupName.getChildStationProgrammeList().get(childPosition).getProgrammeName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
