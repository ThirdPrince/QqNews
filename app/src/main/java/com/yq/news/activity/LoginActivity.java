package com.yq.news.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.luck.picture.lib.immersive.LightStatusBarUtils;
import com.yq.news.MainActivity;
import com.yq.news.R;
import com.yq.news.model.LoginInfo;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.util.LayoutUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 登录
 */
public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private static final String TAG = "LoginActivity";

    private static final int RC_SD_PERM = 1000;

    private static final int RC_PHONE_PERM = 1001;

    private TextView toolbar_title;

    private TextView register_tv ;

    private FancyButton fancyButton ;

    private String token ;

    private TextInputEditText username ;

    private TextInputEditText password;

    private TextView forgetPassword;

    private ImageView clear_img ;

    private ImageView  eye_img ;
    /**
     * 登陆Dialog
     */
    private ProgressDialog loadingDialog;

    private int count = 0;

    private String customNum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutUtil.notificationColor(this);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();

        EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_sd),
                RC_SD_PERM,
                Manifest.permission.READ_PHONE_STATE);

    }

    private void initView()
    {
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("登录");
        register_tv = findViewById(R.id.register_tv);
        fancyButton = findViewById(R.id.login_button);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        forgetPassword = findViewById(R.id.forgetPassword);
        clear_img = findViewById(R.id.clear_img);
        eye_img = findViewById(R.id.eye_img);
        String spuAccount = SPUtils.getInstance().getString("account");
        String spuPwd = SPUtils.getInstance().getString("pwd");
        if(!TextUtils.isEmpty(spuAccount));
        {
            username.setText(spuAccount);
            username.setSelection(spuAccount.length());
            clear_img.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(spuPwd));
        {
            password.setText(spuPwd);
        }
        toolbar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count == 5)
                {
                     Intent intent = new Intent(LoginActivity.this,SettingActivity.class);
                    // startActivity(intent);
                      count = 0;
                }
            }
        });
        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        clear_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
            }
        });
        eye_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getInputType()== InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eye_img.setImageDrawable(getResources().getDrawable(R.drawable.eye));
                }else
                {
                    password.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eye_img.setImageDrawable(getResources().getDrawable(R.drawable.eye_click));
                }
                password.setSelection(password.length());
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().length()<=0)
                {
                    clear_img.setVisibility(View.GONE);
                }else
                {
                    clear_img.setVisibility(View.VISIBLE);
                }

            }
        });
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String account = username.getEditableText().toString();
                final String pwd = password.getEditableText().toString();
                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showShort("请连接网络！");
                    return;
                }
                if(TextUtils.isEmpty(account))
                {

                    ToastUtils.showShort("账号不能为空！");
                    return ;
                }

                if(TextUtils.isEmpty(pwd))
                {

                    ToastUtils.showShort("密码不能为空！");
                    return ;
                }
                showDialog();
                OkHttpManager.getInstance().getToken(new NetCallBack() {
                    @Override
                    public void success(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject   = new JSONObject(response);
                            token = jsonObject.getString("data");
                            if(!TextUtils.isEmpty(token))
                            {
                                SPUtils.getInstance().put("token",token);
                            }
                            Log.e(TAG,"token::"+token);
                            OkHttpManager.getInstance().login(token, account, pwd, new NetCallBack() {
                                @Override
                                public void success(String response) {
                                    dismissDialog();
                                    LoginInfo loginInfo = new Gson().fromJson(response,LoginInfo.class);
                                    SPUtils.getInstance().put("account",account);
                                    SPUtils.getInstance().put("pwd",pwd);
                                    SPUtils.getInstance().put("uid",loginInfo.getData().getId()+"");
                                    SPUtils.getInstance().put("roleid",loginInfo.getData().getRoleid()+"");
                                    Intent intent  = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("loginInfo",loginInfo);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void failed(String msg) {
                                    dismissDialog();
                                    ToastUtils.showShort(msg);
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showShort(e.getMessage());
                        }
                    }

                    @Override
                    public void failed(String msg) {
                        dismissDialog();
                        ToastUtils.showShort(msg);
                    }
                });



            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutUtil.showForgetDialog(LoginActivity.this,customNum);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        OkHttpManager.getInstance().getToken(new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject   = new JSONObject(response);
                    token = jsonObject.getString("data");
                    if(!TextUtils.isEmpty(token))
                    {
                        SPUtils.getInstance().put("token",token);
                    }
                    Log.e(TAG,"token::"+token);

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

                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.showShort(e.getMessage());
                }
            }

            @Override
            public void failed(String msg) {
                ToastUtils.showShort(msg);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @AfterPermissionGranted(RC_SD_PERM)
    private void permissionGranted() {

      /*  String deviceId = DeviceUtils.getUniqueDeviceId();
        String phoneId = PhoneUtils.getDeviceId();
        String phoneName  = Build.MODEL;;
        Log.e(TAG,"deviceId=="+deviceId+"：：phoneId=="+phoneId+"::phoneName=="+phoneName);*/
    }


    private void showDialog() {
        loadingDialog = ProgressDialog.show(this, null, "登录中...");
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setCancelable(true);
        loadingDialog.show();// 设置圆形旋转进度条
    }

    private void dismissDialog() {
        loadingDialog.dismiss();
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS
                );
            }

        }
        return super.dispatchTouchEvent(ev);
    }
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {


    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        ToastUtils.showShort( getString(R.string.rationale_sd));
        EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_sd),
                RC_SD_PERM,
                Manifest.permission.READ_PHONE_STATE);
    }
}
