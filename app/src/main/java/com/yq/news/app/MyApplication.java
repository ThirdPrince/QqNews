package com.yq.news.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.RomUtils;
import com.blankj.utilcode.util.Utils;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.tencent.bugly.Bugly;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yq.news.MainActivity;

import java.util.List;


public class MyApplication extends MultiDexApplication {

    private static final String TAG = "com.yq.news";

    private int mFinalCount;


    private MyLifecycleHandler myLifecycleHandler ;

    // user your appid the key.
    private static final String APP_ID = "2882303761518223219";
    // user your appid the key.
    private static final String APP_KEY = "5621822319219";

    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep
    // com.xiaomi.mipushdemo

    private static DemoHandler sHandler = null;
    private static MainActivity sMainActivity = null;
    @Override
    public void onCreate() {
        super.onCreate();
        //Utils.init(this);
        //CrashUtils.init();
        Bugly.init(this,"0f36fc584a",true);
        if(shouldInit())
        {
            // 不是华为全部小米推送
            if(!RomUtils.isHuawei()) {
                reInitPush(this);
            }
        }
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);
        if (sHandler == null) {
            sHandler = new DemoHandler(getApplicationContext());
        }

        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        myLifecycleHandler = new MyLifecycleHandler();
        registerActivityListener();
        registerActivityLifecycleCallbacks(ParallaxHelper.getInstance());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(myLifecycleHandler);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
    public static void reInitPush(Context ctx) {
        Log.e(TAG,"Mi_registerPush");
        MiPushClient.registerPush(ctx.getApplicationContext(), APP_ID, APP_KEY);
    }
    public static DemoHandler getHandler() {
        return sHandler;
    }

    public static void setMainActivity(MainActivity activity) {
        sMainActivity = activity;
    }

    private void registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(myLifecycleHandler);
        }
    }

    public static class DemoHandler extends Handler {

        private Context context;

        public DemoHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            if (sMainActivity != null) {
                sMainActivity.refreshLogInfo();
            }
            if (!TextUtils.isEmpty(s)) {
                //Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 用来控制手势密码的逻辑
     */
    public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            mFinalCount++;
            Log.e(TAG,"onActivityStarted");
            //如果mFinalCount ==1，说明是从后台到前台
            Log.e(TAG, mFinalCount +"");
            if (mFinalCount == 1){
                //说明从后台回到了前台
                Intent intent = new Intent("GESTURE_BROADCAST");
                LocalBroadcastManager.getInstance(MyApplication.this).sendBroadcast(intent);
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
         // Log.e(TAG,"onActivityResumed");
        }

        @Override
        public void onActivityPaused(Activity activity) {

            //Log.e(TAG,"onActivityPaused");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            //Log.e(TAG,"onActivityStopped");
            mFinalCount--;
            //如果mFinalCount ==0，说明是前台到后台
            Log.i(TAG, mFinalCount +"");
            if (mFinalCount == 0){
                //说明从前台回到了后台
            }

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
