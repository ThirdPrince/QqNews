package com.yq.news.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yq.news.R;
import com.yq.news.adapter.MainInfoPageAdapter;
import com.yq.news.model.ArticleInfo;
import com.yq.news.model.LoginInfo;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.news.view.Watermark;
import com.yq.util.Constant;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PointListActivity extends BaseActivity {

    List<ArticleInfo> list ;

    private MainInfoPageAdapter mainInfoPageAdapter;
  /*  private RefreshLayout refreshLayout ;
    protected RecyclerView recyclerView ;
    protected ClassicsHeader mClassicsHeader;*/

    private int pageNo = 1 ;

    private int pageSize = 15 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);
        initToolBar();
        initRcy();
        toolbar_title.setText("点赞列表");
        back_lay.setVisibility(View.VISIBLE);
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        List<String> labels = new ArrayList<>();
        LoginInfo loginInfo = (LoginInfo) CacheDiskUtils.getInstance().getSerializable(Constant.LOGIN_CACHE_KEY);
        if(loginInfo != null && loginInfo.getData()!= null) {
            labels.add(loginInfo.getData().getRealname() + " " + loginInfo.getData().getTelphone());
            Watermark.getInstance().show(this, loginInfo.getData().getRealname() + " " + loginInfo.getData().getTelphone());
        }

        list = new ArrayList<>();
        mainInfoPageAdapter = new MainInfoPageAdapter(this,list);
        recyclerView.setAdapter(mainInfoPageAdapter);
       // onLoadData();
    }

    private void onLoadData() {
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                getData();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });

    }

    private void getData()
    {
        OkHttpManager.getInstance().getPointDataList(pageNo+"", pageSize+"", new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                String rsp = "";
                try {
                    jsonObject = new JSONObject(response);
                    rsp = jsonObject.getString("data");
                    JSONArray jsonArray = new JSONObject(rsp).getJSONArray("list");
                    List<ArticleInfo> favoriteList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ArticleInfo>>() {}.getType());
                    list.clear();
                    list.addAll(favoriteList);
                    if(list.size()<15)
                    {
                        refreshLayout.setEnableLoadMore(false);
                    }else
                    {
                        refreshLayout.setEnableLoadMore(true);
                    }
                    if(list.size()>0)
                    {
                        empty_lay.setVisibility(View.GONE);
                    }else
                    {
                        empty_lay.setVisibility(View.VISIBLE);
                    }
                    mainInfoPageAdapter.refreshTextSize();
                    mainInfoPageAdapter.notifyDataSetChanged();
                    mainInfoPageAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                            ArticleInfo articleInfo = list.get(position);
                            ArticleDetailActivity.startActivity(PointListActivity.this,articleInfo.getId()+"");
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

    @Override
    protected void onResume() {
        super.onResume();
        onLoadData();
    }
}
