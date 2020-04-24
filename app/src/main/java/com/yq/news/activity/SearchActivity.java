package com.yq.news.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yq.news.R;
import com.yq.news.adapter.MainInfoPageAdapter;
import com.yq.news.model.ArticleInfo;
import com.yq.news.model.LoginInfo;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.news.view.ClearEditText;
import com.yq.news.view.Watermark;
import com.yq.util.Constant;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends BaseActivity {

    ClearEditText text_search;
    TextView search_tv;

    List<ArticleInfo> list ;

    private MainInfoPageAdapter mainInfoPageAdapter;

    private ImageView backImg ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //initToolBar();
        initRcy();
        List<String> labels = new ArrayList<>();
        LoginInfo loginInfo = (LoginInfo) CacheDiskUtils.getInstance().getSerializable(Constant.LOGIN_CACHE_KEY);
        if(loginInfo != null && loginInfo.getData()!= null) {
            labels.add(loginInfo.getData().getRealname() + " " + loginInfo.getData().getTelphone());
            Watermark.getInstance().show(this, loginInfo.getData().getRealname() + " " + loginInfo.getData().getTelphone());
        }
        backImg = findViewById(R.id.back);
        text_search = (ClearEditText)findViewById(R.id.text_search);
        search_tv = (TextView)findViewById(R.id.search_tv);
        text_search.requestFocus();
        text_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(!textView.getText().toString().isEmpty()){
                    refreshLayout.getLayout().setVisibility(View.VISIBLE);
                    onLoadData(textView.getText().toString());
                    hideKeyboard(text_search);
                }
                return false;
            }
        });
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!text_search.getText().toString().isEmpty()){
                    refreshLayout.getLayout().setVisibility(View.VISIBLE);
                    onLoadData(text_search.getText().toString());
                    hideKeyboard(text_search);
                }
            }
        });

        list = new ArrayList<>();
        mainInfoPageAdapter = new MainInfoPageAdapter(this,list);
        recyclerView.setAdapter(mainInfoPageAdapter);

    }

    private void onLoadData(String keyword) {
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                String states = "3";
                OkHttpManager.getInstance().getArticleDataList("1", "20", "", keyword, states, new NetCallBack() {
                    @Override
                    public void success(String response) {
                        JSONObject jsonObject = null;
                        String rsp = "";
                        try {
                            jsonObject = new JSONObject(response);
                            rsp = jsonObject.getString("data");
                            JSONArray jsonArray = new JSONObject(rsp).getJSONArray("list");
                            List<ArticleInfo> searchList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ArticleInfo>>() {}.getType());
                            list.clear();
                            list.addAll(searchList);
                            if(list.size()<20)
                            {
                                refreshLayout.setEnableLoadMore(false);
                            }
                            if(list.size()>0)
                            {
                                empty_lay.setVisibility(View.GONE);
                            }else
                            {
                                empty_lay.setVisibility(View.VISIBLE);
                            }
                            mainInfoPageAdapter.notifyDataSetChanged();
                            mainInfoPageAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    ArticleInfo articleInfo = list.get(position);
                                    ArticleDetailActivity.startActivity(SearchActivity.this,articleInfo.getId()+"");
                                }

                                @Override
                                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    return false;
                                }
                            });
                            refreshLayout.finishRefresh();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void failed(String msg) {

                    }
                });
            }
        });
    }

    public void back(View view) {
        finish();
    }

    //隐藏软键盘
    public static void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
