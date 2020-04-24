package com.yq.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.blankj.utilcode.util.SPUtils;
import com.yq.news.R;

public class GestureActivitySetting extends BaseActivity {

    /**
     * 开启手势
     */
    private SwitchCompat gesture_switch ;

    private View gesture_setting ;

    private int requestCode = 1024 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_setting);
        initToolBar();
        initView();
        initData();
    }

    private void initView()
    {
        back_lay.setVisibility(View.VISIBLE);
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gesture_switch = (SwitchCompat)findViewById(R.id.gesture_switch);
        gesture_setting = findViewById(R.id.gesture_setting);
        boolean isOpenGesture = SPUtils.getInstance().getBoolean("isOpenHandLock", false);
        gesture_switch.setChecked(isOpenGesture);
        gesture_setting.setVisibility(isOpenGesture ? View.VISIBLE:View.GONE);
        gesture_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Intent intent = new Intent(GestureActivitySetting.this, GestureActivity.class);
                    intent.putExtra("openHandLock",true);
                    startActivityForResult(intent,requestCode);
                }else
                {
                    gesture_setting.setVisibility(View.INVISIBLE);
                    SPUtils.getInstance().put("isOpenHandLock", false);
                }
            }
        });
        gesture_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestureActivitySetting.this, GestureActivity.class);
                intent.putExtra("modifyHandPw",true);
                startActivityForResult(intent,requestCode);
            }
        });
    }
    private void initData()
    {
        toolbar_title.setText("手势密码");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(this.requestCode == requestCode)
        {
            if(resultCode ==RESULT_OK)
            {
                boolean openGesture = SPUtils.getInstance().getBoolean("isOpenHandLock", false) ;
                if(openGesture)
                {
                    gesture_setting.setVisibility(View.VISIBLE);
                    gesture_switch.setChecked(true);
                }else
                {
                    gesture_setting.setVisibility(View.GONE);
                    gesture_switch.setChecked(false);
                }
            }
        }
    }
}
