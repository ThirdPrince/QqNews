package com.yq.news.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener;
import com.yq.news.R;
import com.yq.news.adapter.SubScribeAdapter;
import com.yq.news.itf.OnSubcribleClick;
import com.yq.news.model.SubscribleInfo;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.yq.util.Constant.SUBSCRIBLE_BROADCAST;

/**
 * @author dhl
 * 订阅 Fragment UI
 * A simple {@link Fragment} subclass.
 */
public class SubScribeFragment extends BaseFragment {


    private static final String ARG_PARAM1 = "param1";

    private TextView toolbar_title;


    private SubScribeAdapter subScribeAdapter ;

    List<SubscribleInfo.ListBean> listBean ;

    private final int SORT = 1024;

    private Handler handler =  new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case SORT:
                    OkHttpManager.getInstance().updateClassSort(listBean, new NetCallBack() {
                        @Override
                        public void success(String response) {
                            Intent intent = new Intent(SUBSCRIBLE_BROADCAST);
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        }

                        @Override
                        public void failed(String msg) {

                            ToastUtils.showShort(msg);
                        }
                    });
                    break;
            }
        }
    };
    public SubScribeFragment() {
        // Required empty public constructor
    }

    public static SubScribeFragment newInstance(String param1) {
        SubScribeFragment fragment = new SubScribeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_scribe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar_title = view.findViewById(R.id.toolbar_title);
        initRcy(view,true);
        toolbar_title.setText("订阅");
        listBean = new ArrayList<>() ;
        swip_rcy_view.setLongPressDragEnabled(true); // 长按拖拽，默认关闭。
        swip_rcy_view.setItemViewSwipeEnabled(false); // 滑动删除，默认关闭。
        swip_rcy_view.setSwipeItemMenuEnabled(false);
        subScribeAdapter = new SubScribeAdapter(getActivity(),R.layout.fragment_subscribe_item,listBean);
        swip_rcy_view.setAdapter(subScribeAdapter);
        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadMore(false);//不启用上拉加载功能
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                getDataList();
            }
        });
        swip_rcy_view.setOnItemMoveListener(new OnItemMoveListener() {
            @Override
            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                // 不同的ViewType不能拖拽换位置。
                if (srcHolder.getItemViewType() != targetHolder.getItemViewType()) return false;

                // 真实的Position：通过ViewHolder拿到的position都需要减掉HeadView的数量。
                int fromPosition = srcHolder.getAdapterPosition() - swip_rcy_view.getHeaderCount();
                int toPosition = targetHolder.getAdapterPosition() - swip_rcy_view.getHeaderCount();

                Collections.swap(listBean, fromPosition, toPosition);
                subScribeAdapter.notifyItemMoved(fromPosition, toPosition);

                handler.removeMessages(SORT);
                handler.sendEmptyMessageDelayed(SORT,300);
                return true;// 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。
            }

            @Override
            public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
                int adapterPosition = srcHolder.getAdapterPosition();
                int position = adapterPosition - swip_rcy_view.getHeaderCount();

                if (swip_rcy_view.getHeaderCount() > 0 && adapterPosition == 0) { // HeaderView。

                } else { // 普通Item。


                }
            }
        });

    }



    private void getDataList()
    {
        OkHttpManager.getInstance().getClassDataList("", "", new NetCallBack() {
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
                SubscribleInfo tabInfo =  new Gson().fromJson(rsp,SubscribleInfo.class);
                listBean.clear();
                listBean.addAll(tabInfo.getList());
                if(listBean.size()>0)
                {
                    empty_lay.setVisibility(View.GONE);
                }else
                {
                    empty_lay.setVisibility(View.VISIBLE);
                    empty_tv.setText("暂无相关关注");
                }
                if(listBean.size()<=1)
                {
                    swip_rcy_view.setLongPressDragEnabled(false);
                }else
                {
                    swip_rcy_view.setLongPressDragEnabled(true);
                }
                subScribeAdapter.notifyDataSetChanged();
                subScribeAdapter.setOnSubcribleClick(new OnSubcribleClick() {
                    @Override
                    public void onClick(SubscribleInfo.ListBean listBean,View view) {

                        view.setEnabled(false);
                        OkHttpManager.getInstance().subscribeClass(!(listBean.getSubscribe() == 1), listBean.getId()+"", new NetCallBack() {
                            @Override
                            public void success(String response) {
                                handler.removeMessages(SORT);
                                handler.sendEmptyMessageDelayed(SORT,300);
                                getDataList();
                                Intent intent = new Intent(SUBSCRIBLE_BROADCAST);
                                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                                view.setEnabled(true);
                            }

                            @Override
                            public void failed(String msg) {
                                view.setEnabled(true);
                            }
                        });
                    }
                });

                refreshLayout.finishRefresh();
                if(listBean.size()>0) {
                    // 禁止下拉，防止滑动冲突
                    refreshLayout.setEnableRefresh(false);
                }

                handler.removeMessages(SORT);
                handler.sendEmptyMessageDelayed(SORT,300);
            }

            @Override
            public void failed(String msg) {
                refreshLayout.finishRefresh();
                // 禁止下拉，防止滑动冲突
               // refreshLayout.setEnableRefresh(false);
                ToastUtils.showShort(msg);
            }
        });
    }

}
