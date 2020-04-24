package com.yq.news.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.luck.picture.lib.dialog.CustomDialog;
import com.yq.news.R;
import com.yq.news.view.LockIndicator;
import com.yq.news.view.LocusPassWordView;
import com.yq.util.Md5Utils;


/**
 * 手势密码
 * @author dhl
 */
public class GestureActivity extends BaseActivity {





    private LockIndicator mPwdView_small;
    private LocusPassWordView mPwdView;

    private TextView passWordText ;
    private TextView passWordWarnText;
    private  String pwd = null;

    private int passedTime = 0;
    private int pwdTime = 0;

    // 设置手势密码时，重设的标识
    private boolean pwdVerify = false;
    // 修改手势密码时，手势密码验证通过标识
    private boolean modifyVerified = false;

    private ImageView person_img;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);
        initToolBar();
        initView();
        initData();
    }

    private void initData()
    {
        pwd = SPUtils.getInstance().getString("password", "");
        intent = this.getIntent();
        if (intent.getBooleanExtra("modifyHandPw", false)) {
            // 从修改手势密码进入
            passWordText.setText(R.string.more_watch_old);
            pwdVerify = true;
            toolbar_title.setText("修改手势密码");
            back_lay.setVisibility(View.VISIBLE);
            //OPENGESTRUE = false;
        } else if (intent.getBooleanExtra("openHandLock", false)) {
            // 从打开手势密码进入
            pwd = "";
            //setTitle(R.string.more_watch_set);
            pwdVerify = false;
           // OPENGESTRUE = false;
            person_img.setVisibility(View.GONE);
            mPwdView_small.setVisibility(View.VISIBLE);
            toolbar_title.setText("设置手势密码");
            back_lay.setVisibility(View.VISIBLE);
        } else if (intent.getBooleanExtra("cancelHandLock", false)) {
            // 取消手势密码
           // setTitle(R.string.more_watch_validate);
            //OPENGESTRUE = false;
        } else if (intent.getBooleanExtra("IsFirstLogin", false)) {
            // 用户第一次登陆客户端，跳出手势密码设置界面
            //backImage.setVisibility(View.GONE);
            setTitle(R.string.more_watch_set);
            pwdVerify = false;
            //OPENGESTRUE = false;
            person_img.setVisibility(View.GONE);
            mPwdView_small.setVisibility(View.VISIBLE);
        } else {
            // 验证手势密码
            passWordText.setText(R.string.more_watch);
            toolbar_title.setText(R.string.more_watch);
            //topLay.setVisibility(View.INVISIBLE);
           // pwdSet.setVisibility(View.VISIBLE);
           // OPENGESTRUE = true;
        }
    }
    private void initView()
    {
        mPwdView =  (LocusPassWordView)findViewById(R.id.mPassWordView);
        mPwdView_small =  (LockIndicator)findViewById(R.id.mPassWordView_small);
        passWordText = (TextView)findViewById(R.id.multi_tv_token_time_hint);
        passWordWarnText =  (TextView)findViewById(R.id.multi_tv_false_hint);
        person_img = (ImageView) findViewById(R.id.person_img);
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPwdView.setOnCompleteListener(new LocusPassWordView.OnCompleteListener() {
            @Override
            public void onComplete(String mPassword) {
                Md5Utils md5 = new Md5Utils();
                boolean passed = false;
                if (pwd.length() == 0) {
                    pwd = md5.toMd5(mPassword, "");
                    mPwdView.clearPassword();
                    mPwdView.invalidate();
                    passWordText.setText(R.string.more_watch_draw_next);
                    passWordText.setTextColor(Color.parseColor("#8e8e8e"));
                    mPwdView_small.setPath(mPassword);
                    passWordText.setTextColor(Color.parseColor("#8e8e8e"));
                    passWordText.invalidate();
                } else {
                    String encodedPwd = md5.toMd5(mPassword, "");
                    if (encodedPwd.equals(pwd)) {
                        passed = true;
                        pwdTime = 0;
                        if (pwdVerify) {
                            pwdVerify = false;
                            modifyVerified = true;
                        }
                        passWordWarnText.setVisibility(View.INVISIBLE);
                    } else {
                        mPwdView.markError();

                        Animation shake = AnimationUtils.loadAnimation(
                                GestureActivity.this,
                                R.anim.gesture_shake);// 加载动画资源文件
                        if (intent.getBooleanExtra("openHandLock", false)
                                || modifyVerified
                                || intent
                                .getBooleanExtra("IsFirstLogin", false)) {
                            passWordText
                                    .setText(R.string.more_watch_draw_error);
                            passWordText.setTextColor(Color
                                    .parseColor("#ffff695e"));
                            passWordText.startAnimation(shake);
                            if (!pwdVerify) {
                               /* showRightTxt(Utils
                                        .getString(R.string.more_watch_reset));*/
                            }
                        } else {
                            pwdTime += 1;
                            if (pwdTime == 5) {
                                {
                                    SPUtils.getInstance().put("isOpenHandLock", false);
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(GestureActivity.this);
                                builder.setCancelable(false);
                                builder.setMessage(R.string.more_watch_error)
                                        .setPositiveButton(
                                               "确定",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        SPUtils.getInstance().remove("account");
                                                        SPUtils.getInstance().remove("pwd");
                                                        ActivityUtils.finishAllActivities();
                                                        startActivity(new Intent(GestureActivity.this, LoginActivity.class));
                                                        finish();
                                                    }
                                                }).create().show();

                            }
                            passWordText.setText(GestureActivity.this.getResources().getString(
                                    (R.string.more_watch_tip))
                                    + (5 - pwdTime)
                                    + GestureActivity.this.getResources().getString(R.string.more_watch_ci));
                            passWordText.setTextColor(Color
                                    .parseColor("#ffff695e"));
                            if (intent.getBooleanExtra("modifyHandPw", false)) {
                                if (!pwdVerify) {
                                   /* showRightTxt(Utils
                                            .getString(R.string.more_watch_reset));*/
                                }
                            }
                        }
                        passWordWarnText.setTextColor(Color
                                .parseColor("#ffff695e"));
                        passWordText.startAnimation(shake);
                        passWordWarnText.startAnimation(shake);
                        if (person_img.getVisibility() == View.VISIBLE) {
                            person_img.startAnimation(shake);// 给组件播放动画效果
                        }
                    }
                }

                if (passed) {

                    if (intent.getBooleanExtra("modifyHandPw", false)) {
                        passedTime += 1;
                        pwd = "";
                        mPwdView.clearPassword();
                        mPwdView.invalidate();
                        passWordText.setText(R.string.more_watch_draw);
                        setTitle(R.string.more_watch_set);
                        person_img.setVisibility(View.GONE);
                        mPwdView_small.setVisibility(View.VISIBLE);
                        passWordText.setTextColor(Color.parseColor("#8e8e8e"));
                        passWordText.invalidate();
                        if (passedTime == 2) {
                            SPUtils.getInstance().put("password", md5.toMd5(mPassword, ""));
                            // sy 2016-5-23
                           /* Global.getInstance().changeGesturesPassword(
                                    md5.toMd5(mPassword, ""));
                            showToast(R.string.more_watch_set_success);
                            mPasswordCopy = mPassword;*/
                            finish();
                        }

                    } else if (intent.getBooleanExtra("openHandLock", false)
                            || intent.getBooleanExtra("IsFirstLogin", false)) {
                        SPUtils.getInstance().put("password", md5.toMd5(mPassword, ""));
                       {
                            /*Intent data = new Intent();
                            data.putExtra("HASPWDSET", true);
                            setResult(RESULT_OK, data);*/
                        }
                        SPUtils.getInstance().put("isOpenHandLock", true);


                        Intent data = new Intent();
                        //data.putExtra("HASPWDCANCEL", true);
                        setResult(RESULT_OK, data);
                       /* Global.getInstance().changeGesturesPassword(
                                md5.toMd5(mPassword, ""));*/
                        //showToast(R.string.more_watch_set_success);
                       // mPasswordCopy = mPassword;
                        finish();
                    } else if (intent.getBooleanExtra("cancelHandLock", false)) {
                        SPUtils.getInstance().put("password", md5.toMd5("", ""));
                        SPUtils.getInstance().put("isOpenHandLock", false);
                        Intent data = new Intent();
                        data.putExtra("HASPWDCANCEL", true);
                        setResult(RESULT_OK, data);
                        finish();
                    } else {
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (intent.getBooleanExtra("modifyHandPw", false)
                || intent.getBooleanExtra("openHandLock", false)
                || intent.getBooleanExtra("IsFirstLogin", false)) {
            if (intent.getBooleanExtra("openHandLock", false)) {
                Intent data = new Intent();
                data.putExtra("HASPWDSET", false);
                setResult(RESULT_OK, data);
            }
            if (intent.getBooleanExtra("IsFirstLogin", false)
                   ) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            } else {
                finish();
            }
        } else {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();

            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
