package com.yq.news.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yq.news.R;
import com.yq.news.adapter.IndustyAdapter;
import com.yq.news.adapter.MainPageAdapter;
import com.yq.news.model.ArticleInfo;
import com.yq.news.model.DepartmentBean;
import com.yq.news.model.IndustryBean;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IndustyActivity extends BaseActivity {


    private RefreshLayout refreshLayout;
    protected RecyclerView recyclerView;
    protected ClassicsHeader mClassicsHeader;

    private List<IndustryBean> list;

    private IndustyAdapter industyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industy);
        initToolBar();
        toolbar_title.setText("行业");
        back_lay.setVisibility(View.VISIBLE);
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initRcy();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        industyAdapter = new IndustyAdapter(this,R.layout.item_industy,list);
        recyclerView.setAdapter(industyAdapter);
        onLoadData();
    }

    private void onLoadData() {
        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                OkHttpManager.getInstance().getIndustry(new NetCallBack() {
                    @Override
                    public void success(String response) {
                        JSONObject jsonObject = null;
                        String data = "";
                        try {
                            jsonObject = new JSONObject(response);
                            data = jsonObject.getString("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        List<IndustryBean> industryBeans = new Gson().fromJson(data, new TypeToken<List<IndustryBean>>() {
                        }.getType());
                        list.clear();
                        list.addAll(industryBeans);
                        industyAdapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                        industyAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                Intent intent = new Intent();
                                intent.putExtra("data",list.get(position));
                                setResult(RESULT_OK,intent);
                                finish();
                            }

                            @Override
                            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                return false;
                            }
                        });

                    }

                    @Override
                    public void failed(String msg) {

                    }
                });
            }
        });
    }
}