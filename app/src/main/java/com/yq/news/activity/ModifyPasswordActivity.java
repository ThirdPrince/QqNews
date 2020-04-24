package com.yq.news.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yq.news.R;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.util.LayoutUtil;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * 修改密码
 */
public class ModifyPasswordActivity extends BaseActivity {


    /**
     * 原密码
     */
    private EditText et_pwd_old ;


    /**
     * 新密码
     */
    private EditText et_pwd_new;

    /**
     * 再次输入
     */
    private EditText et_pwd_again ;

    private FancyButton sure_button ;

    private String customNum = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        initToolBar();
        initView();
        OkHttpManager.getInstance().sysConfig(new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                String rsp = "";
                try {
                    jsonObject = new JSONObject(response);
                    rsp = jsonObject.getString("data");
                    jsonObject = new JSONObject(rsp);
                    if(!jsonObject.isNull("customertelnumber"))
                    {
                        customNum = jsonObject.getString("customertelnumber");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void failed(String msg) {

            }
        });
    }
    private void initView()
    {
        toolbar_title.setText("修改密码");
        back_lay.setVisibility(View.VISIBLE);
        et_pwd_old = (EditText) findViewById(R.id.et_pwd_old);
        et_pwd_again = (EditText)findViewById(R.id.et_pwd_again);
        et_pwd_new = (EditText)findViewById(R.id.et_pwd_new);
        sure_button = (FancyButton)findViewById(R.id.sure_button);
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sure_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwdOld = et_pwd_old.getText().toString();
                String pwd_new = et_pwd_new.getText().toString();
                String pwdAgain = et_pwd_again.getText().toString();
                KeyboardUtils.hideSoftInput(ModifyPasswordActivity.this);
                if(TextUtils.isEmpty(pwdOld))
                {
                    ToastUtils.showShort("原密码不能为空！");
                    return ;
                }
                if(TextUtils.isEmpty(pwd_new))
                {
                    ToastUtils.showShort("新密码不能为空！");
                    return ;
                }

                if(TextUtils.isEmpty(pwdAgain))
                {
                    ToastUtils.showShort("确认密码不能为空！");
                    return ;
                }
                if(!pwdAgain.equals(pwd_new))
                {
                    ToastUtils.showShort("两次密码输入不一致！");
                    return ;
                }
                if(pwdAgain.length()<6)
                {
                    ToastUtils.showShort("密码长度不能低于6位！");
                    return ;
                }

                if(pwdAgain.length()>16)
                {
                    ToastUtils.showShort("密码长度不能大于16位！");
                    return ;
                }

                showDialog("保存中...");
                OkHttpManager.getInstance().modifyPwd(et_pwd_old.getText().toString(),et_pwd_new.getText().toString(),
                new NetCallBack(){

                    @Override
                    public void success(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean modify = jsonObject.getBoolean("data");
                            if(modify)
                            {
                                ToastUtils.showShort("修改密码成功");
                                SPUtils.getInstance().put("pwd",pwd_new);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showShort(e.getMessage());
                        }
                      dismissDialog();
                    }

                    @Override
                    public void failed(String msg) {
                        dismissDialog();
                        ToastUtils.showShort(msg);
                    }
                });

            }
        });
    }

    public void forgetPassword(View view) {
        LayoutUtil.showForgetDialog(ModifyPasswordActivity.this,customNum);
    }

}
