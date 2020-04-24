package com.yq.news.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.yq.news.R;
import com.yq.news.model.DownloadBean;
import com.yq.news.net.AppUpdater;
import com.yq.news.net.INetDownloadCallBack;
import com.yq.util.Md5Utils;

import java.io.File;

/**
 * 应用升级弹窗
 */
public class UpdateVersionShowDialog extends DialogFragment {

    private static final String TAG = "UpdateVersionShowDialog";

    private DownloadBean downloadBean ;

    private TextView title ,content,update;

    public static void show(FragmentActivity fragmentActivity, DownloadBean downloadBean)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("download_bean",downloadBean);
        UpdateVersionShowDialog dialog = new UpdateVersionShowDialog();
        dialog.setArguments(bundle);
        dialog.show(fragmentActivity.getSupportFragmentManager(),TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        downloadBean = (DownloadBean) bundle.getSerializable("download_bean");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.dialog_fragment,container,false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        View view = inflater.inflate(R.layout.dialog_fragment,  ((ViewGroup) window.findViewById(android.R.id.content)), false);//需要用android.R.id.content这个view
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;
        //setCancelable(false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initEvent();
    }

    @Override
    public void onStart() {
        super.onStart();
       // resizeDialogFragment();
    }

    private void initView(View view )
    {
        title = view.findViewById(R.id.title);
        content = view.findViewById(R.id.content);
        update = view.findViewById(R.id.update);
        title.setText(downloadBean.title);
        content.setText(downloadBean.content);

    }
    private void initEvent()
    {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update.setEnabled(false);
               // setCancelable(false);
               // String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                //File targetFile = new File(apkPath,"update.apk");
                File targetFile = new File(getActivity().getCacheDir(),"update.apk");
              /*  File apk = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/app-release.apk");
                String localMd5 =  Md5Utils.calcMD5(apk);
               Log.e(TAG,"localMd5::"+localMd5);*/
                AppUpdater.getInstance().getNetManager().download(downloadBean.url, targetFile, new INetDownloadCallBack() {
                    @Override
                    public void success(File apkFile) {
                        Log.e(TAG,"apkFile ::"+apkFile.getPath());

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //com.yq.util.AppUtils.installApk(getActivity(),apkFile.getPath());
                               /* String serverMd5 =  Md5Utils.calcMD5(apkFile);
                                Log.e(TAG,"serverMd5::"+serverMd5);*/
                                AppUtils.installApp(apkFile.getPath());
                                dismiss();

                            }
                        },500);

                    }

                    @Override
                    public void failed(Throwable throwable) {
                        throwable.printStackTrace();
                        update.setEnabled(true);
                    }

                    @Override
                    public void progress(final String progress) {
                        Log.e(TAG,"progress ::"+progress);
                        if(getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    update.setText(progress);
                                }
                            });
                        }

                    }
                },UpdateVersionShowDialog.this);
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        AppUpdater.getInstance().getNetManager().cancel(UpdateVersionShowDialog.this);
    }
    private void resizeDialogFragment() {
        Dialog dialog = getDialog();
        if (null != dialog) {
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
            lp.height = (25 * ScreenUtils.getScreenHeight()/ 32);//获取屏幕的宽度，定义自己的宽度
            lp.width = (8 * ScreenUtils.getScreenHeight() / 9);
            if (window != null) {
                window.setLayout(lp.width, lp.height);
            }
        }
    }

}

