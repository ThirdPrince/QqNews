package com.yq.news.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.yq.news.R;
import com.yq.news.model.LoginInfo;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

public class DeviceBindingActivity extends BaseActivity {

    private LoginInfo loginInfo ;

    private FancyButton binding_btn;

    private ImageView binding_img;

    private LinearLayout deviceId_layout;

    private TextView deviceId_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_binding);
        initToolBar();
        toolbar_title.setText("设备绑定");
        back_lay.setVisibility(View.VISIBLE);
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding_btn = (FancyButton)findViewById(R.id.binding_button);
        binding_img = (ImageView) findViewById(R.id.binding_img);
        deviceId_layout = (LinearLayout) findViewById(R.id.deviceId_layout);
        deviceId_tv = (TextView) findViewById(R.id.deviceId_tv);
        deviceId_tv.setText(android.os.Build.MODEL + " 本机");
        loginInfo = (LoginInfo) CacheDiskUtils.getInstance().getSerializable(Constant.LOGIN_CACHE_KEY);
        if(!loginInfo.getData().getEquipmentlist().toString().equals("[]"))
        {
            deviceId_layout.setVisibility(View.VISIBLE);
            binding_img.setImageResource(R.drawable.bd);
            binding_btn.setBackgroundColor(Color.argb(255, 206 , 233, 255));
            binding_btn.setTextColor(Color.argb(255, 0 , 153, 250));
            binding_btn.setText("解除绑定");
        }
        binding_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding();
            }
        });
    }

    public void binding(){
        loginInfo = (LoginInfo) CacheDiskUtils.getInstance().getSerializable(Constant.LOGIN_CACHE_KEY);
        final boolean bindDevice = ObjectUtils.isEmpty(loginInfo.getData().getEquipmentlist());
        //MainActivity.loginInfo.getData().getEquipmentlist() == null;
        OkHttpManager.getInstance().bindOrRebindPhone(bindDevice,new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                boolean bind = false;
                try {
                    jsonObject = new JSONObject(response);
                    bind = jsonObject.getBoolean("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(bind) {

                    OkHttpManager.getInstance().loginInfo(new NetCallBack() {
                        @Override
                        public void success(String response) {
                            LoginInfo loginInfo = new Gson().fromJson(response,LoginInfo.class);
                            if(loginInfo!=null)
                            {
                                CacheDiskUtils.getInstance().put(Constant.LOGIN_CACHE_KEY,loginInfo);
                            }
                        }

                        @Override
                        public void failed(String msg) {

                        }
                    });
                    if(bindDevice)
                    {
                        deviceId_layout.setVisibility(View.VISIBLE);
                        binding_img.setImageResource(R.drawable.bd);
                        binding_btn.setText("解除绑定");
                        binding_btn.setBackgroundColor(Color.argb(255, 206 , 233, 255));
                        binding_btn.setTextColor(Color.argb(255, 0 , 153, 250));
                        ToastUtils.showShort("绑定手机成功！");
                    }else
                    {
                        deviceId_layout.setVisibility(View.INVISIBLE);
                        binding_img.setImageResource(R.drawable.jcbd);
                        binding_btn.setText("绑定");
                        binding_btn.setBackgroundColor(Color.argb(255, 0 , 153, 250));
                        binding_btn.setTextColor(Color.WHITE);
                        ToastUtils.showShort("解除绑定成功！");
                    }
                }
            }

            @Override
            public void failed(String msg) {
                ToastUtils.showShort(msg);
            }
        });
    }

}
