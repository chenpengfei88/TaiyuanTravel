package com.chenpengfei.taiyuantravel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.chenpengfei.taiyuantravel.R;
import com.chenpengfei.taiyuantravel.adapter.PoiAddressListAdapter;
import com.chenpengfei.taiyuantravel.customview.CustomToast;

import java.util.List;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description 通过关键词检索地址activity
 */
public class PoiAddressActivity extends BaseActivity {

    private ListView poiAddressListView;
    private EditText searchAddressEdit;
    private View actionBarSearchView;
    private SuggestionSearch mSuggestionSearch;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_poi_address);
        initView();
    }

    private void initView() {
        //添加actionbar上的搜索地址框
        actionBarSearchView = getLayoutInflater().inflate(R.layout.activity_search_address, null);
        addCustomView(actionBarSearchView);
        poiAddressListView = (ListView) findViewById(R.id.list_main_search_address);
        searchAddressEdit = (EditText) actionBarSearchView.findViewById(R.id.edit_main_search);
        searchAddressEdit.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String addressKeyword = searchAddressEdit.getText().toString();
                if(!TextUtils.isEmpty(addressKeyword)) {
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(addressKeyword).city(getStringContent(R.string.search_city)));
                }
            }
        });
        //创建在线建议查询实例
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);
    }

    OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
        @Override
        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
            if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                CustomToast.makeText(PoiAddressActivity.this, getStringContent(R.string.toast_no_result), Toast.LENGTH_SHORT).show();
                return;
            }
            //查询结果
            List<SuggestionResult.SuggestionInfo> suggestionInfoList = suggestionResult.getAllSuggestions();
            poiAddressListView.setAdapter(new PoiAddressListAdapter(suggestionInfoList, getLayoutInflater(), PoiAddressActivity.this));
            poiAddressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    SuggestionResult.SuggestionInfo suggestionsInfo = (SuggestionResult.SuggestionInfo) view.getTag();
                    Intent intent = getIntent();
                    intent.putExtra("suggestionsInfo", suggestionsInfo);
                    setResult(0, intent);
                    finish();
                }
            });
        }
    };


}
