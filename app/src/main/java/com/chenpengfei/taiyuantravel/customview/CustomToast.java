package com.chenpengfei.taiyuantravel.customview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.chenpengfei.taiyuantravel.R;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description 重写的toast显示
 */
public class CustomToast {

    public static Toast makeText(Context context, CharSequence text, int duration) {
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View contentView = LayoutInflater.from(context).inflate(R.layout.activity_toast, null);
        TextView conentTextView = ((TextView) contentView.findViewById(R.id.content_tip_id));
        conentTextView.setText(text);
        toast.setView(contentView);
        // 设置显示时间
        toast.setDuration(duration);
        return toast;
    }

}
