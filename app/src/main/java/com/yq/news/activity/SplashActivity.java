package com.yq.news.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.yq.news.MainActivity;
import com.yq.news.R;
import com.yq.news.model.LoginInfo;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;

import org.json.JSONException;
import org.json.JSONObject;

import pub.devrel.easypermissions.EasyPermissions;

/**
 *
 */
public class SplashActivity extends AppCompatActivity {


    private static final String TAG = "SplashActivity";
    /**
     * 账号
     */
    private  String spuAccount ;

    /**
     * 密码
     */
    private String spuPwd ;

    private String token ;

    LottieAnimationView lottieAnimationView ;

    private ImageView imageView;

    private static final int RC_SD_PERM = 1000;

    long splashTime = 2000;
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 设置状态栏底色白色
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.WHITE);
            // 设置状态栏字体黑色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }*/
        super.onCreate(savedInstanceState);

        if (!this.isTaskRoot() && getIntent() != null) {
            String action = getIntent().getAction();
            Log.e(TAG,"action:"+action);
            if (getIntent().hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        } else {
            setContentView(R.layout.activity_splash);
            //lottieAnimationView = findViewById(R.id.splash_animation);
            imageView = findViewById(R.id.image);
            spuAccount = SPUtils.getInstance().getString("account");
            spuPwd = SPUtils.getInstance().getString("pwd");
            //startAnimation(lottieAnimationView, "news.json");
         /*   AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(imageView, "alpha", 0.88f, 1f),
                    ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.11f),
                    ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.11f)
            );
            set.setDuration(splashTime);
            set.start();*/
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    login();

                }
            },splashTime);
        }
    }

    public void jumpToMain() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void login()
    {
        if(TextUtils.isEmpty(spuAccount))
        {
            jumpToMain();
            return ;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            jumpToMain();
            return;
        }
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
                    OkHttpManager.getInstance().login(token, spuAccount, spuPwd, new NetCallBack() {
                        @Override
                        public void success(String response) {
                            LoginInfo loginInfo = new Gson().fromJson(response,LoginInfo.class);
                            SPUtils.getInstance().put("uid",loginInfo.getData().getId()+"");
                            SPUtils.getInstance().put("roleid",loginInfo.getData().getRoleid()+"");
                            Intent intent  = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("loginInfo",loginInfo);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void failed(String msg) {
                            ToastUtils.showShort(msg);
                            jumpToMain();
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                ToastUtils.showShort(msg);
                jumpToMain();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAnimation(lottieAnimationView);
    }

    private void startAnimation(LottieAnimationView mLottieAnimationView, String animationName) {
        mLottieAnimationView.setAnimation(animationName);
        mLottieAnimationView.playAnimation();
    }
    private void cancelAnimation(LottieAnimationView mLottieAnimationView) {
        if (mLottieAnimationView != null) {
            mLottieAnimationView.cancelAnimation();
        }
    }
}
