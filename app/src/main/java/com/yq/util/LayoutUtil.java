package com.yq.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.yq.news.R;

import static com.yq.util.Constant.SUBSCRIBLE_BROADCAST;
import static com.yq.util.Constant.TEXT_FONT_BIG_SIZE;
import static com.yq.util.Constant.TEXT_FONT_SIZE;
import static com.yq.util.Constant.TEXT_FONT_SIZE_BROADCAST;
import static com.yq.util.Constant.TEXT_FONT_SMALL_SIZE;
import static com.yq.util.Constant.TEXT_FONT_STAND_SIZE;
import static com.yq.util.Constant.TEXT_FONT_SUPER_BIG_SIZE;

/**
 * Created by xk on 2019/11/2.
 * 布局控件工具类
 */

public class LayoutUtil {

    /**
     * 设置通知栏字体颜色 沉浸式
     * @param activity 对象
     */
    public static void notificationColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 设置状态栏底色白色
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.WHITE);
            // 设置状态栏字体黑色
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    /**
     * 设置EditText的属性
     * @param editText 控件对象
     * @param hintText et提示语
     * @param size 字体大小
     */
    public static void setHintTextSize(EditText editText, String hintText, int size){
        SpannableString ss = new SpannableString(hintText);//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(new SpannedString(ss));
    }


    /**
     * 展示密码的dialog
     * @param activity 对象
     */
    public static void showForgetDialog(Activity activity,String phone){
        View view = LayoutInflater.from(activity).inflate(R.layout.password_dialog_layout, null);

        TextView phoneTv = view.findViewById(R.id.phone);
        if(!TextUtils.isEmpty(phone)) {
            phoneTv.setText(phone);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//去掉圆角背景背后的棱角
        dialog.setCanceledOnTouchOutside(true);   //失去焦点dismiss
        dialog.show();
    }

    public static void showFontDialog(Activity activity){
        View view = LayoutInflater.from(activity).inflate(R.layout.font_dialog_layout, null);


        RelativeLayout font_small  = view.findViewById(R.id.font_small);
        RelativeLayout font_standard  = view.findViewById(R.id.font_standard);
        RelativeLayout font_big  = view.findViewById(R.id.font_big);
        RelativeLayout font_super_big  = view.findViewById(R.id.font_super_big);
        RadioButton font_small_rb  = view.findViewById(R.id.font_small_rb);
        RadioButton font_standard_rb  = view.findViewById(R.id.font_standard_rb);
        RadioButton font_big_rb  = view.findViewById(R.id.font_big_rb);
        RadioButton font_super_big_rb  = view.findViewById(R.id.font_super_big_rb);


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//去掉圆角背景背后的棱角
        dialog.setCanceledOnTouchOutside(true);   //失去焦点dismiss
        dialog.show();

        int text_size = SPUtils.getInstance().getInt(TEXT_FONT_SIZE);
        switch (text_size)
        {
            case TEXT_FONT_SMALL_SIZE:
                font_small_rb.setChecked(true);
                break;

            case TEXT_FONT_STAND_SIZE:
                font_standard_rb.setChecked(true);
                break;

            case TEXT_FONT_BIG_SIZE:
                font_big_rb.setChecked(true);
                break;

            case TEXT_FONT_SUPER_BIG_SIZE:
                font_super_big_rb.setChecked(true);
                break;
        }


        font_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SPUtils.getInstance().put(TEXT_FONT_SIZE,TEXT_FONT_SMALL_SIZE);
                Intent intent = new Intent(TEXT_FONT_SIZE_BROADCAST);
                LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
               // font_small_rb.setChecked(true);
                dialog.dismiss();

            }
        });
        font_standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.getInstance().put(TEXT_FONT_SIZE,TEXT_FONT_STAND_SIZE);
                Intent intent = new Intent(TEXT_FONT_SIZE_BROADCAST);
                LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                // font_small_rb.setChecked(true);
                dialog.dismiss();
            }
        });
        font_big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.getInstance().put(TEXT_FONT_SIZE,TEXT_FONT_BIG_SIZE);
                Intent intent = new Intent(TEXT_FONT_SIZE_BROADCAST);
                LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                // font_small_rb.setChecked(true);
                dialog.dismiss();
            }
        });
        font_super_big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.getInstance().put(TEXT_FONT_SIZE,TEXT_FONT_SUPER_BIG_SIZE);
               // font_small_rb.setChecked(true);
                Intent intent = new Intent(TEXT_FONT_SIZE_BROADCAST);
                LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                dialog.dismiss();
            }
        });

    }

}
