package com.chenpengfei.taiyuantravel.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.chenpengfei.taiyuantravel.R;
import java.util.List;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description  检索地址list适配器
 */
public class PoiAddressListAdapter extends BaseAdapter {

    private List<SuggestionResult.SuggestionInfo> suggestionsInfoArrayList = null;
    private LayoutInflater layoutInflater;
    private Context context;

    public PoiAddressListAdapter(List<SuggestionResult.SuggestionInfo> suggestionsInfoArrayList, LayoutInflater layoutInflater, Context context) {
        this.suggestionsInfoArrayList = suggestionsInfoArrayList;
        this.layoutInflater = layoutInflater;
        this.context = context;
    }

    @Override
    public int getCount() {
        return suggestionsInfoArrayList == null ? 0 : suggestionsInfoArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return suggestionsInfoArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = layoutInflater.inflate(R.layout.activity_main_poi_address_item, null);
        }
        SuggestionResult.SuggestionInfo suggestionsInfo = (SuggestionResult.SuggestionInfo) getItem(i);
        ((TextView) view.findViewById(R.id.text_main_poi_address_detail)).setText(suggestionsInfo.key); //详情地址
        ((TextView) view.findViewById(R.id.text_main_poi_address_detail_district)).setText(context.getString(R.string.search_city) + suggestionsInfo.district);
        view.setTag(suggestionsInfo);
        return view;
    }
}
