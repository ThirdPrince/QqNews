package com.yq.news.fragment;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.yq.news.MainActivity;
import com.yq.news.R;
import com.yq.news.activity.DeviceBindingActivity;
import com.yq.news.activity.FavoritesListActivity;
import com.yq.news.activity.GestureActivitySetting;
import com.yq.news.activity.LoginActivity;
import com.yq.news.activity.ModifyPasswordActivity;
import com.yq.news.activity.PersonInfoActivity;
import com.yq.news.activity.PointListActivity;
import com.yq.news.dialog.UpdateVersionShowDialog;
import com.yq.news.model.DownloadBean;
import com.yq.news.model.LoginInfo;
import com.yq.news.net.ApiUtils;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.news.view.LineTextView;
import com.yq.util.Constant;
import com.yq.util.LayoutUtil;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * 我的 UI
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {


    private static final String TAG = "MineFragment";

    private static final String ARG_PARAM1 = "param1";

    private  LoginInfo loginInfo ;

    private TextView toolbar_title ;

    /**
     * 收藏
     */
    private  LinearLayout collection_lay ;

    private TextView collection_tv;

    /**
     * 点赞
     */
    private LinearLayout point_lay ;
    private TextView point_tv;

    private LinearLayout name_lay ;

    private TextView name_tv ;

    private TextView address_tv ;

    /**
     * 修改密码
     */

    private LineTextView modify_pwd_lay ;

    /**
     * 手势密码
     */
    private LineTextView gesture_password ;

    private LineTextView font_setting ;

    private LineTextView bind_phone ;

    private LineTextView check_app ;

    private int requestCode = 1024;

    /**
     * 突出登录
     */
    private FancyButton logout_button ;


    private TextView  version_tv ;

    public MineFragment() {
        // Required empty public constructor
    }

    public static MineFragment newInstance(String param1) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        updateApp(false,false);
    }

    private void initData() {
       OkHttpManager.getInstance().countCollectArticle(new NetCallBack() {
           @Override
           public void success(String response) {

           }

           @Override
           public void failed(String msg) {

           }
       });
        OkHttpManager.getInstance().countPointArticle(new NetCallBack() {
            @Override
            public void success(String response) {

            }

            @Override
            public void failed(String msg) {

            }
        });
    }

    private void initView(View view)
    {
        toolbar_title = view.findViewById(R.id.toolbar_title);
        name_lay = view.findViewById(R.id.name_lay);
        modify_pwd_lay = view.findViewById(R.id.modify_pwd_lay);
        gesture_password = view.findViewById(R.id.gesture_password);
        font_setting = view.findViewById(R.id.font_setting);
        toolbar_title.setText("我的");
        logout_button = view.findViewById(R.id.logout_button);
        name_tv = view.findViewById(R.id.name_tv);
        address_tv = view.findViewById(R.id.address_tv);
        bind_phone = view.findViewById(R.id.bind_phone);
        collection_lay  = view.findViewById(R.id.collection_lay);
        point_lay = view.findViewById(R.id.point_lay);
        collection_tv = view.findViewById(R.id.collection_tv);
        point_tv = view.findViewById(R.id.point_tv);
        version_tv = view.findViewById(R.id.version_tv);
        version_tv.setText("当前版本："+AppUtils.getAppVersionName());
        check_app = view.findViewById(R.id.check_app);
        initNum();
        loginInfo = (LoginInfo) CacheDiskUtils.getInstance().getSerializable(Constant.LOGIN_CACHE_KEY);
//        if(loginInfo.getData().getEquipmentlist() !=null)
//        {
//            bind_phone.setText("解除绑定");
//        }
        if(loginInfo != null && loginInfo.getData() !=null) {
            name_tv.setText(loginInfo.getData().getAccount());
            address_tv.setText((String) loginInfo.getData().getDeptname());
        }
        //address_tv.setText(MainActivity.loginInfo.getData().getDeptname());
        modify_pwd_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ModifyPasswordActivity.class));
            }
        });
        name_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PersonInfoActivity.class));
            }
        });
        collection_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FavoritesListActivity.class));
            }
        });
        point_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PointListActivity.class));
            }
        });

        gesture_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GestureActivitySetting.class);
                intent.putExtra("openHandLock",true);
                startActivity(intent);
            }
        });
        font_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutUtil.showFontDialog(getActivity());
            }
        });
        bind_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DeviceBindingActivity.class));
            }
        });
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        check_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateApp(true,true);
            }
        });
    }

    private void initNum() {
        OkHttpManager.getInstance().countCollectArticle(new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                String rsp = "";
                try {
                    jsonObject = new JSONObject(response);
                    rsp = jsonObject.getString("data");
                    int count = new JSONObject(rsp).getInt("count");
                    collection_tv.setText(count + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {

            }
        });
        OkHttpManager.getInstance().countPointArticle(new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                String rsp = "";
                try {
                    jsonObject = new JSONObject(response);
                    rsp = jsonObject.getString("data");
                    int count = new JSONObject(rsp).getInt("count");
                    point_tv.setText(count + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {

            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) {
            initNum();
            updateApp(false,false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void updateApp(boolean popDialog,boolean isShowAppVersion)
    {
        OkHttpManager.getInstance().updateApp(new NetCallBack() {
            @Override
            public void success(String response) {
                Log.e(TAG, "rsp==" + response);
                JSONObject jsonObject = null;
                String rsp = "";
                try {
                    jsonObject = new JSONObject(response);
                    rsp = jsonObject.getString("data");
                    jsonObject = new JSONObject(rsp);
                    DownloadBean downLoadBean = DownloadBean.parse(jsonObject);
                    if (downLoadBean == null) {
                        ToastUtils.showShort( "接口返回数据异常");
                        return;
                    }
                    // 检测是否需要弹窗
                    long versionCode = Long.parseLong(downLoadBean.versionCode);
                    Drawable rightDrawable = null;
                    Drawable leftDrawable = null;
                    leftDrawable = getActivity().getResources().getDrawable(R.drawable.jcgx);
                    leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
                            leftDrawable.getMinimumHeight());
                    if (versionCode > com.yq.util.AppUtils.getVersionCode(getActivity())) {


                        rightDrawable = getActivity().getResources().getDrawable(R.drawable.new_check);
                        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
                                rightDrawable.getMinimumHeight());
                       check_app.setCompoundDrawables(leftDrawable,null,rightDrawable,null);
                        if(popDialog) {
                            UpdateVersionShowDialog.show(getActivity(), downLoadBean);
                        }
                        return;
                    }else
                    {
                        rightDrawable = getActivity().getResources().getDrawable(R.drawable.mine_arrow);
                        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
                                rightDrawable.getMinimumHeight());
                        check_app.setCompoundDrawables(leftDrawable,null,rightDrawable,null);
                        if(isShowAppVersion)
                        {
                            ToastUtils.showShort("已是最新版本");
                        }
                    }
                }catch (JSONException e)
                {
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

}
