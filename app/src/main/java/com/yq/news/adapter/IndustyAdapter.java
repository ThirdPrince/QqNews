package com.yq.news.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yq.news.R;
import com.yq.news.model.ArticleInfo;
import com.yq.news.model.IndustryBean;
import com.yq.news.net.ApiUtils;
import com.yq.util.RelativeDateFormat;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * 首页
 */
public class IndustyAdapter extends CommonAdapter<IndustryBean> {




    private Context context ;

    public IndustyAdapter(Context context, int layoutId, List<IndustryBean> datas) {
        super(context, layoutId, datas);
        this.context = context ;
    }

    @Override
    protected void convert(ViewHolder holder, IndustryBean articleInfo, int position) {

         holder.setText(R.id.tv_name,articleInfo.getName());


    }



}
