package com.chenpengfei.taiyuantravel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.chenpengfei.taiyuantravel.R;
import java.util.List;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description  公交车站点list适配器
 */
public class BusStationListAdapter extends BaseAdapter {

    private List<BusLineResult.BusStation> busStationArrayList = null;
    private LayoutInflater layoutInflater;
    private Context context;

    public BusStationListAdapter(List<BusLineResult.BusStation> busStationArrayList, LayoutInflater layoutInflater) {
        this.busStationArrayList = busStationArrayList;
        this.layoutInflater = layoutInflater;
        this.context = context;
    }

    @Override
    public int getCount() {
        return busStationArrayList == null ? 0 : busStationArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return busStationArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = layoutInflater.inflate(R.layout.activity_route_line_search_item, null);
        }
        BusLineResult.BusStation busStation = (BusLineResult.BusStation) getItem(i);
        ((TextView) view.findViewById(R.id.text_route_line_search_content)).setText(busStation.getTitle());
        ((TextView) view.findViewById(R.id.text_roule_line_bumber)).setText(i + 1 + "");
        return view;
    }
}
