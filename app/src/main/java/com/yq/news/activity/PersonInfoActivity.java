package com.yq.news.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.yq.news.R;
import com.yq.news.model.LoginInfo;
import com.yq.util.Constant;

/**
 * 个人资料
 */
public class PersonInfoActivity extends BaseActivity {

   private  LoginInfo loginInfo ;

   private TextView username_tv ;

   private TextView account_tv ;

   private TextView phone_tv ;

   private TextView dept_tv ;

   private TextView email_tv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        initToolBar();
        initData();
    }

    private void initData()
    {
        toolbar_title.setText("个人资料");
        back_lay.setVisibility(View.VISIBLE);
        username_tv = (TextView) findViewById(R.id.username_tv);
        account_tv =(TextView) findViewById(R.id.account_tv);
        phone_tv =(TextView) findViewById(R.id.phone_tv);
        dept_tv = (TextView)findViewById(R.id.dept_tv);
        email_tv = (TextView)findViewById(R.id.email_tv);
        loginInfo = (LoginInfo) CacheDiskUtils.getInstance().getSerializable(Constant.LOGIN_CACHE_KEY);
        username_tv.setText(loginInfo.getData().getRealname());
        account_tv.setText(loginInfo.getData().getAccount());
        phone_tv.setText(loginInfo.getData().getTelphone());
        if(loginInfo.getData().getDeptname()!=null) {
            dept_tv.setText((String)loginInfo.getData().getDeptname());
        }
        email_tv.setText(loginInfo.getData().getEmail());
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
