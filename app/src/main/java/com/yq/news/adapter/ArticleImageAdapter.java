package com.yq.news.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yq.news.R;
import com.yq.news.activity.ArticleDetailActivity;
import com.yq.news.itf.OnSubcribleClick;
import com.yq.news.model.ArticleDetail;
import com.yq.news.model.SubscribleInfo;
import com.yq.news.net.ApiUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class ArticleImageAdapter extends CommonAdapter<ArticleDetail.ImglistBean> {


    private Context context ;




    public ArticleImageAdapter(Context context, int layoutId, List<ArticleDetail.ImglistBean> datas) {
        super(context, layoutId, datas);
        this.context = context ;
    }

    @Override
    protected void convert(ViewHolder holder, final ArticleDetail.ImglistBean imglistBean, int position) {

        ImageView imageView = holder.getView(R.id.article_img);
        //Glide.with(context).load(ApiUtils.BASE_ADDRESS + imglistBean.getImgurl()).into(imageView);
    }
}
