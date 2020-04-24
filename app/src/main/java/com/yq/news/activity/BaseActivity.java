package com.yq.news.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.yq.news.R;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;




/**
 *   基类：activity
 * @author dhl
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";


    protected TextView toolbar_title;

    protected RelativeLayout back_lay;


    protected LinearLayout empty_lay ;

    public static long lastGestureTime = 0;




    private Handler handler = new Handler(Looper.getMainLooper());
    /**
     * 登陆Dialog
     */
    private ProgressDialog loadingDialog;

    public boolean wasBackground = false;    //声明一个布尔变量,记录当前的活动背景

    public static boolean isFirstCreate = true ;

    /**
     * smartRefresh
     */
    protected RefreshLayout refreshLayout ;
    protected ClassicsHeader mClassicsHeader;
    protected Drawable mDrawableProgress;
    /**
     * rcy
     */


    protected RecyclerView recyclerView ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /**
         * Android 6.0 以上设置状态栏颜色
         */
        //initSwipeBackFinish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 设置状态栏底色白色
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.WHITE);
            // 设置状态栏字体黑色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.onCreate(savedInstanceState);
      /*  if(isFirstCreate) {
            goGesture();
            isFirstCreate = false ;
        }*/

    }



    protected void initToolBar() {
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        back_lay = (RelativeLayout) findViewById(R.id.back_lay);
    }


    protected void initRcy()
    {
        recyclerView = (RecyclerView) findViewById(R.id.rcy_view);
        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        mClassicsHeader = (ClassicsHeader)refreshLayout.getRefreshHeader();
        //mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis()-deta));
        mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM-dd HH:mm", Locale.CHINA));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        empty_lay  = findViewById(R.id.empty_lay);
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if (wasBackground) {//
            Log.e(TAG, "从后台回到前台");
            //goGesture();
        }
        wasBackground = false;*/

        validToken();

        //isUIProcess();
    }

    @Override
    protected void onPause() {
        super.onPause();

       /* if (isApplicationBroughtToBackground())
            wasBackground = true;*/

        //isUIProcess();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void validToken() {
        OkHttpManager.getInstance().validToken(new NetCallBack() {
            @Override
            public void success(String response) {

                //Log.e(TAG, "Token:" + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean data = jsonObject.getBoolean("data");
                    if (!data) {
                        getToken();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(String msg) {
                getToken();

            }

        });
    }

    private void getToken() {
        OkHttpManager.getInstance().getToken(new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String token = jsonObject.getString("data");
                    if (!TextUtils.isEmpty(token)) {
                        SPUtils.getInstance().put("token", token);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                ToastUtils.showShort(msg);
            }
        });
    }

    protected void showDialog(String tip) {
        loadingDialog = ProgressDialog.show(this, null, tip);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setCancelable(true);
        loadingDialog.show();// 设置圆形旋转进度条
    }

    protected void dismissDialog() {
        if(loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

   /* private boolean isApplicationBroughtToBackground() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        Log.e(TAG, "tasks::"+tasks.get(0).id);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            Log.e(TAG, "topActivity.getPackageName()::"+topActivity.getPackageName());
            if (!topActivity.getPackageName().equals(getPackageName())) {
                return true;
            }
        }
        return false;
    }*/


    /**
     * 跳到手势密码的逻辑
     */
    protected void goGesture()
    {
        long nowTime = Calendar.getInstance().getTimeInMillis();
        boolean openGesture = SPUtils.getInstance().getBoolean("isOpenHandLock", false);
        if ((nowTime - lastGestureTime > 1000) && openGesture ) {
            lastGestureTime = nowTime;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(BaseActivity.this, GestureActivity.class);
                    startActivity(intent);
                   // overridePendingTransition(0, 0);
                }
            }, 200);

        }
    }



}
