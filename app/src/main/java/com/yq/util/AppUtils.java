package com.yq.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;

/**
 * 工具类
 */
public class AppUtils {

    private static final String TAG = "AppUtils";

    public static long getVersionCode(Context context)
    {
        PackageManager packageManager = context.getPackageManager();

        try {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Log.e(TAG,"versionCode::"+packageInfo.getLongVersionCode());

                return packageInfo.getLongVersionCode();
            }else
            {
                Log.e(TAG,"versionCode::"+packageInfo.versionCode);
                return packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        return -1;
    }

    public static String getAppPackageName(Context context)
    {
        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            return packageInfo.packageName ;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "" ;
    }

    public static void installApk(Context context ,String downloadApk)
    {

        //Intent intent = new Intent(Intent.ACTION_VIEW);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(downloadApk);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", file);
            Log.e(TAG,"apkUri::"+apkUri.toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
      /*  int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);*/
    }
}
