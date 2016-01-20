package com.chenpengfei.taiyuantravel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.chenpengfei.taiyuantravel.R;
import com.chenpengfei.taiyuantravel.adapter.PoiAddressListAdapter;
import com.chenpengfei.taiyuantravel.customview.CustomToast;
import com.chenpengfei.taiyuantravel.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description 通过关键词检索地址activity
 */
public class PoiAddressActivity extends BaseActionBarActivity {

    private ListView poiAddressListView;
    private EditText searchAddressEdit;
    private View actionBarSearchView;
    private SuggestionSearch mSuggestionSearch;
    private ImageView deleteImage;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_poi_address);
        initView();
    }

    @Override
    protected void onDestroy() {
        if(mSuggestionSearch != null) mSuggestionSearch.destroy();
        super.onDestroy();
    }

    private void initView() {
        //添加actionbar上的搜索地址框
        actionBarSearchView = getLayoutInflater().inflate(R.layout.activity_search_address, null);
        toolBarAddView(actionBarSearchView);
        poiAddressListView = (ListView) findViewById(R.id.list_main_search_address);
        searchAddressEdit = (EditText) actionBarSearchView.findViewById(R.id.edit_main_search);
        deleteImage = (ImageView) actionBarSearchView.findViewById(R.id.image_main_delete);
        searchAddressEdit.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(CommonUtil.isNetworkAvailable(PoiAddressActivity.this)) {
                    String addressKeyword = searchAddressEdit.getText().toString();
                    if(!TextUtils.isEmpty(addressKeyword)) {
                        deleteImage.setVisibility(View.VISIBLE);
                        if(!addressKeyword.contains(getStringContent(R.string.search_city)))
                            addressKeyword = getStringContent(R.string.search_city) + addressKeyword;
                        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(addressKeyword).city(getStringContent(R.string.search_city)));
                    } else {
                        deleteImage.setVisibility(View.GONE);
                    }
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
            List<SuggestionResult.SuggestionInfo> resultSuggestionInfoList = new ArrayList<SuggestionResult.SuggestionInfo>();
            List<SuggestionResult.SuggestionInfo> suggestionInfoList = suggestionResult.getAllSuggestions();
            int size = suggestionInfoList.size();
            for(int i = 0; i < size; i++) {
                if(suggestionInfoList.get(i).pt != null) {
                    resultSuggestionInfoList.add(suggestionInfoList.get(i));
                }
            }
            poiAddressListView.setAdapter(new PoiAddressListAdapter(resultSuggestionInfoList, getLayoutInflater(), PoiAddressActivity.this));
            poiAddressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    SuggestionResult.SuggestionInfo suggestionsInfo = (SuggestionResult.SuggestionInfo) view.getTag();
                    Intent intent = getIntent();
                    intent.putExtra("suggestionsInfo", suggestionsInfo);
                    setResult(intent.getIntExtra("address_type", 0), intent);
                    finish();
                }
            });
        }
    };

    public void btnClick(View v) {
        switch (v.getId()){
            case R.id.image_main_delete:
                searchAddressEdit.setText("");
                break;
        }
    }

}
