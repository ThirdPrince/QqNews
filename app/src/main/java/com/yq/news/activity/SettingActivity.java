package com.yq.news.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yq.news.R;
import com.yq.news.net.ApiUtils;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * 服务器设置
 */
public class SettingActivity extends AppCompatActivity {


    private TextView toolbar_title ;

    private RelativeLayout back_lay ;

    private EditText setting_et_ip ;

    private EditText setting_et_port ;

    private FancyButton save ;

    String ip;
    String port ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolBar();
    }
    protected   void initToolBar() {
        toolbar_title = findViewById(R.id.toolbar_title);
        back_lay = findViewById(R.id.back_lay);
        back_lay.setVisibility(View.VISIBLE);
        setting_et_ip = findViewById(R.id.setting_et_ip);
        setting_et_port = findViewById(R.id.setting_et_port);
        save = findViewById(R.id.save);
        toolbar_title.setText("服务器设置");

         ip = SPUtils.getInstance().getString("ip");
        if(TextUtils.isEmpty(ip))
        {
            ip = ApiUtils.ADDRESS_IP;
            port = ApiUtils.ADDRESS_PORT;

        }

        setting_et_ip.setText(ip);
        setting_et_port.setText(port);
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ip = setting_et_ip.getEditableText().toString();
                String port = setting_et_port.getEditableText().toString();
                if(TextUtils.isEmpty(ip))
                {
                    ToastUtils.showShort("IP 地址不能为空");
                    return ;
                }

                if(TextUtils.isEmpty(port))
                {
                    ToastUtils.showShort("端口 地址不能为空");
                    return ;
                }
                SPUtils.getInstance().put("port",port);
                finish();
            }
        });
    }
}
