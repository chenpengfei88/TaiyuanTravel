package com.chenpengfei.taiyuantravel.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.chenpengfei.taiyuantravel.R;
import com.chenpengfei.taiyuantravel.activity.PoiAddressActivity;
import com.chenpengfei.taiyuantravel.activity.RouteLineActivity;
import com.chenpengfei.taiyuantravel.customview.CustomToast;
import com.chenpengfei.taiyuantravel.pojo.EventType;
import com.chenpengfei.taiyuantravel.util.BaiduLocationUtil;
import com.chenpengfei.taiyuantravel.util.Const;
import com.ypy.eventbus.EventBus;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description 基类fragment
 */
public class BaseFragment extends Fragment {


    private RelativeLayout loadRelativeLayout;
    private ImageView loadImage;
    private Animation animation;

    /**
     *
     * @param view 加载view
     * @param context
     * @description 显示加载view
     */
    public void showLoadView(View view, Context context) {
        if(context == null) return;
        if(loadRelativeLayout == null)
            loadRelativeLayout = (RelativeLayout) view.findViewById(R.id.relative_load);
        if(loadImage == null)
            loadImage = (ImageView) view.findViewById(R.id.image_load);
        loadRelativeLayout.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.image_load_rotate);
        loadImage.startAnimation(animation);
    }

    public void hideLoadView() {
        if(loadRelativeLayout != null)
            loadRelativeLayout.setVisibility(View.GONE);
        if(loadImage != null && animation != null && !animation.hasEnded()) {
            animation.cancel();
        }
    }

}
