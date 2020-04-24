package com.yq.news.fragment;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yq.news.R;
import com.yq.news.model.LoginInfo;
import com.yq.news.view.WaterMarkBg;
import com.yq.news.view.Watermark;
import com.yq.util.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Fragment 基类
 * A simple {@link Fragment} subclass.
 */
public  abstract  class BaseFragment extends Fragment {

    private static final String TITLE = "title";

    /**
     * 共用ToolBar
     */
    protected Toolbar toolbar ;

    /**
     * 没有人员
     */
    protected RelativeLayout no_task;

    protected ImageView no_img;

    protected TextView no_tv;
    /**
     * smartRefresh
     */
    protected RefreshLayout refreshLayout ;
    protected ClassicsHeader mClassicsHeader;
    protected Drawable mDrawableProgress;
    /**
     * rcy
     */
    protected RecyclerView recyclerView ;

    protected SwipeRecyclerView swip_rcy_view ;

    protected LinearLayout empty_lay ;

    protected TextView empty_tv ;


    protected static final int REQUEST_CODE = 1024;

    protected static final int DELAY = 1025;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initToolbar(view);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    protected void initToolbar(View view)
    {
        toolbar = view.findViewById(R.id.tool_bar);

    }
    protected void initRcy(View view)
    {


        recyclerView = view.findViewById(R.id.rcy_view);
        //List<String> labels = new ArrayList<>();
        refreshLayout = view.findViewById(R.id.refreshLayout);
        mClassicsHeader = (ClassicsHeader)refreshLayout.getRefreshHeader();
        //mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis()-deta));
        mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM-dd HH:mm", Locale.CHINA));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        empty_lay  = view.findViewById(R.id.empty_lay);
        empty_tv = view.findViewById(R.id.empty_tv);
    }

    /**
     * 是否启用SwipRcy
     * @param view
     * @param isDrag
     */
    protected void initRcy(View view,boolean isDrag)
    {
        swip_rcy_view = view.findViewById(R.id.rcy_view);
        swip_rcy_view.setVisibility(View.VISIBLE);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        mClassicsHeader = (ClassicsHeader)refreshLayout.getRefreshHeader();
        //mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis()-deta));
        mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM-dd HH:mm", Locale.CHINA));
        swip_rcy_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        empty_lay  = view.findViewById(R.id.empty_lay);
        empty_tv = view.findViewById(R.id.empty_tv);
    }

}
