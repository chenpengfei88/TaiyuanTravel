package com.chenpengfei.taiyuantravel.pojo;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.SuggestionResult;

/**
 * Created by Frank on 2016/1/15.
 */
public class ChildSuggestionInfo extends SuggestionResult.SuggestionInfo {

    public ChildSuggestionInfo(LatLng latLng, String key) {
        this.pt = latLng;
        this.key = key;
    }
}
