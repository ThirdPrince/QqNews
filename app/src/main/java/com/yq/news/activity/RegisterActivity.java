package com.yq.news.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yq.news.R;
import com.yq.news.model.IndustryBean;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.util.LayoutUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.yq.util.LayoutUtil.setHintTextSize;

/**
 * 注册UI
 */
public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = "RegisterActivity";

    private TextView toolbar_title ;

    private RelativeLayout back_lay ;

    /**
     * 账号
     */
    private TextInputEditText account_et ;

    /**
     * 密码
     */

    private TextInputEditText pwd_et ;

    /**
     * 真实姓名
     */
    private TextInputEditText name_et ;

    /**
     * 单位布局
     */
    private RelativeLayout department_layout ;
    /**
     * 单位
     */

    private TextView unit_tv;

    /**
     * 行业
     */

    private RelativeLayout industy_lay ;

    private TextView industy_tv ;

    /**
     * city
     */

    private RelativeLayout area_lay ;

    private TextView area_tv ;

    private IndustryBean industryBean ;

    /**
     * 单位
     */

    private String deptId = "";

    /**
     *
     */
    private final int REQUEST_CODE_COM = 1024;

    /**
     * 行业
     */
    private final int REQUEST_CODE_INDUS = 1025;

    /**
     * 地区
     */
    private final int REQUEST_CODE_AREA = 1026;

    private String cityId = "";
    /**
     * 电话
     */
    private TextInputEditText phone_et ;

    /**
     * 邮箱
     */
    private TextInputEditText email_et ;

    /**
     * 备注
     */
    private TextInputEditText remarks_et ;

    private FancyButton register_button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutUtil.notificationColor(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

    }
    private void initView() {
        toolbar_title =findViewById(R.id.toolbar_title);
        toolbar_title.setText("注册账号");
        back_lay = findViewById(R.id.back_lay);
        back_lay.setVisibility(View.VISIBLE);
        account_et = findViewById(R.id.account_et);
        account_et.addTextChangedListener(new SearchWatcher(account_et, false));
        pwd_et = findViewById(R.id.pwd_et);
        name_et = findViewById(R.id.name_et);
        name_et.addTextChangedListener(new SearchWatcher(name_et, true));
        department_layout = findViewById(R.id.department_layout);
        industy_lay = findViewById(R.id.industy_lay);
        industy_tv = findViewById(R.id.industy_tv);
        area_lay = findViewById(R.id.area_lay);
        area_tv = findViewById(R.id.area_tv);
        unit_tv = findViewById(R.id.unit_tv);
        phone_et = findViewById(R.id.phone_et);
        email_et = findViewById(R.id.email_et);
        remarks_et = findViewById(R.id.remarks_et);
        setTextHintSize();
        register_button = findViewById(R.id.register_button);
        department_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,SelectCompanyActivity.class);
                startActivityForResult(intent,REQUEST_CODE_COM);
            }
        });
        industy_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,IndustyActivity.class);
                startActivityForResult(intent,REQUEST_CODE_INDUS);
            }
        });
        area_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,CityActivity.class);
                startActivityForResult(intent,REQUEST_CODE_AREA);
            }
        });
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(account_et.getText().toString()))
                {
                    ToastUtils.showShort("账号不能为空！");
                    return ;
                }

                if(TextUtils.isEmpty(pwd_et.getText().toString()))
                {
                    ToastUtils.showShort("密码不能为空！");
                    return ;
                }

                if(TextUtils.isEmpty(name_et.getText().toString()))
                {
                    ToastUtils.showShort("姓名不能为空！");
                    return ;
                }

                if(TextUtils.isEmpty(deptId))
                {
                    ToastUtils.showShort("单位不能为空！");
                    return ;
                }
                if(TextUtils.isEmpty(phone_et.getText().toString()))
                {
                    ToastUtils.showShort("电话不能为空！");
                    return ;
                }
                if(TextUtils.isEmpty(email_et.getText().toString()))
                {
                    ToastUtils.showShort("邮箱不能为空！");
                    return ;
                }
                if(ObjectUtils.isEmpty(industryBean) )
                {
                    ToastUtils.showShort("行业不能为空！");
                    return ;
                }
                if(TextUtils.isEmpty(cityId))
                {
                    ToastUtils.showShort("地区不能为空！");
                    return ;
                }

                OkHttpManager.getInstance().register(account_et.getText().toString(),pwd_et.getText().toString(),
                        name_et.getText().toString(),phone_et.getText().toString(),email_et.getText().toString(),deptId,
                        industryBean.getId()+"",cityId, remarks_et.getText().toString(), new NetCallBack() {
                            @Override
                            public void success(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean register = jsonObject.getBoolean("data");
                                    if(register)
                                    {
                                        ToastUtils.showShort("注册成功");
                                        finish();
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
        });
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setTextHintSize() {
        setHintTextSize(account_et, getString(R.string.account_hint), 15);
        setHintTextSize(pwd_et, getString(R.string.password_hint), 15);
        setHintTextSize(name_et, getString(R.string.name_hint), 15);
        setHintTextSize(phone_et, getString(R.string.phone_hint), 15);
        setHintTextSize(email_et, getString(R.string.mailbox_hint), 15);
        setHintTextSize(remarks_et, getString(R.string.remarks_hint), 15);
    }

    class SearchWatcher implements TextWatcher {

        private EditText editText;

        private boolean isName;

        public SearchWatcher(EditText editText, boolean isName){
            this.editText = editText;
            this.isName = isName;
        }

        @Override
        public void onTextChanged(CharSequence ss, int start, int before, int count) {
            String editable = editText.getText().toString();
            Log.e("111wd", "onTextChanged: "+editable );
            String str = stringFilter(editable, isName);
            if(!editable.equals(str)){
                editText.setText(str);
                //设置新的光标所在位置
                editText.setSelection(str.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {

        }
    }


    public static String stringFilter(String str, boolean isName)throws PatternSyntaxException {
        String regEx;
        if(isName){
//            regEx = "^[0-9][^a-zA-Z0-9_\\u4e00-\\u9fa5]+$";
            regEx = "^[0-9][^a-zA-Z0-9_\\\\u4e00-\\\\u9fa5]*$";

        }else {
            regEx = "[^a-zA-Z0-9_]";
        }
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_COM:
                if(resultCode ==  RESULT_OK)
                {
                    unit_tv.setTextColor(Color.BLACK);
                    unit_tv.setText(data.getStringExtra("dept"));
                    deptId = data.getStringExtra("id");
                }
                break;
            case REQUEST_CODE_INDUS:
                if(resultCode ==  RESULT_OK)
                {
                    industryBean = (IndustryBean) data.getSerializableExtra("data");
                    industy_tv.setTextColor(Color.BLACK);
                    industy_tv.setText(industryBean.getName());
                    Log.e(TAG,"行业 =="+industryBean.getName()+"::cityId=="+cityId);
                }
                break;
            case REQUEST_CODE_AREA:
                if(resultCode ==  RESULT_OK)
                {
                    area_tv.setTextColor(Color.BLACK);
                    area_tv.setText(data.getStringExtra("city"));
                    cityId = data.getStringExtra("id");

                    Log.e(TAG,"city =="+data.getStringExtra("city")+"::cityId=="+cityId);
                }
                break;
        }


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
}
