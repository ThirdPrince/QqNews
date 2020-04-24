
package com.yq.news.hwpush;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.RomUtils;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;


/**
 * 华为推送
 */
public class MyPushService extends HmsMessageService {
    private static final String TAG = "PushDemoLog";
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i(TAG, "receive token:" + s);
        if(!TextUtils.isEmpty(s))
        {
            OkHttpManager.getInstance().putRegID(s, RomUtils.getRomInfo().getName(), new NetCallBack() {
                @Override
                public void success(String response) {

                }

                @Override
                public void failed(String msg) {

                }
            });
        }

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().length() > 0) {
            Log.i(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onMessageSent(String s) {
    }

    @Override
    public void onSendError(String s, Exception e) {
    }
}
