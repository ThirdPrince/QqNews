package com.yq.news.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yq.news.R;
import com.yq.news.activity.AddArticleActivity;
import com.yq.news.activity.ArticleDetailActivity;
import com.yq.news.adapter.MainPageAdapter;
import com.yq.news.model.ArticleInfo;
import com.yq.news.model.TabInfo;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 首页Fragment 管理员
 * A simple {@link Fragment} subclass.
 */
public class ManagerFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";

    /**
     * tabLayout
     */


    List<ArticleInfo> list ;

    private MainPageAdapter mainPageAdapter ;

    private String articleId  = "";

    private FloatingActionButton fb ;
    public ManagerFragment() {
        // Required empty public constructor
    }
    public static ManagerFragment newInstance(String param1) {
        ManagerFragment fragment = new ManagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRcy(view);
        fb = view.findViewById(R.id.fb);
        //ViewCompat.setNestedScrollingEnabled(recyclerView, true);
        list = new ArrayList<>();
        mainPageAdapter = new MainPageAdapter(getActivity(),R.layout.fragment_article_item,list);
        recyclerView.setAdapter(mainPageAdapter);
        refreshLayout.setEnableLoadMore(false);//不启用上拉加载功能\
        onLoadData();
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddArticleActivity.startActivity(getActivity());

            }
        });
    }
    /**
     * 加载数据
     */
    private void onLoadData() {

        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                String uIdSpu = SPUtils.getInstance().getString("uid");
                String states = "0,1,2";

                OkHttpManager.getInstance().getSubscribleDataList( new NetCallBack() {
                    @Override
                    public void success(String response) {
                        JSONObject jsonObject = null;
                        String rsp = "";
                        try {
                            jsonObject = new JSONObject(response);
                            rsp = jsonObject.getString("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        TabInfo tabInfo = new Gson().fromJson(response, TabInfo.class);
                        if (tabInfo.getData().size() > 0) {
                            articleId = tabInfo.getData().get(0).getId() + "";
                            OkHttpManager.getInstance().getArticleDataList("1", "20", articleId, "", states, new NetCallBack() {
                                @Override
                                public void success(String response) {
                                    JSONObject jsonObject = null;
                                    String rsp = "";
                                    try {
                                        jsonObject = new JSONObject(response);
                                        rsp = jsonObject.getString("data");
                                        JSONArray jsonArray = new JSONObject(rsp).getJSONArray("list");
                                        List<ArticleInfo> articleInfos = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ArticleInfo>>() {
                                        }.getType());
                                        list.clear();
                                        list.addAll(articleInfos);
                                        mainPageAdapter.notifyDataSetChanged();
                                        mainPageAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                                ArticleInfo articleInfo = list.get(position);
                                                ArticleDetailActivity.startActivity(getActivity(), articleInfo.getId() + "");
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
                        else {
                            refreshLayout.finishRefresh();
                        }
                    }

                    @Override
                    public void failed(String msg) {

                    }
                });



            }
        });


    }
}
