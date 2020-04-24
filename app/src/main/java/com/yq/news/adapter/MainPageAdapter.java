package com.yq.news.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yq.news.R;
import com.yq.news.model.ArticleInfo;
import com.yq.news.net.ApiUtils;
import com.yq.util.RelativeDateFormat;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * 首页
 */
public class MainPageAdapter extends CommonAdapter<ArticleInfo> {



    public static final int TYPE_TEXT = 1;
    public static final int TYPE_PICTURE = 2;

    private String url = "https://cn.bing.com//th?id=OHR.CountyBridge_ZH-CN6500717169_1920x1080.jpg";
    private Context context ;
    public MainPageAdapter(Context context, int layoutId, List<ArticleInfo> datas) {
        super(context, layoutId, datas);
        this.context = context ;
    }

    @Override
    protected void convert(ViewHolder holder, ArticleInfo articleInfo, int position) {

        ImageView imageView = holder.getView(R.id.article_img);
        List<ArticleInfo.ImglistBean> list = articleInfo.getImglist();
        String imageUrl = "";
        if(list != null && list.size()>0)
        {
            imageView.setVisibility(View.VISIBLE);
            imageUrl = list.get(0).getImgurl();
            Glide.with(context).load(ApiUtils.BASE_ADDRESS + imageUrl).placeholder(R.drawable.news_temp).error(R.drawable.news_temp).into(imageView);
        }else
        {
            imageView.setVisibility(View.GONE);
        }
       /* if(position == 0)
        {
            imageView.setVisibility(View.GONE);
        }else
        {
            imageView.setVisibility(View.VISIBLE);
        }*/
        holder.setText(R.id.article_title,articleInfo.getTitle());
        holder.setText(R.id.user_name_tv,articleInfo.getUsername());
        holder.setText(R.id.create_time_tv,  RelativeDateFormat.timeStamp2Date(articleInfo.getCreatetime()));
    }




   /* @Override
    public int getItemViewType(int position) {
        ArticleInfo articleInfo = getDatas().get(position);
        List<ArticleInfo.ImglistBean> list = articleInfo.getImglist();
        String imageUrl = "";
        if(list != null && list.size()>0)
        {
                 return  TYPE_TEXT ;
        }else
        {
            return  TYPE_PICTURE ;
        }
       // return super.getItemViewType(position);
    }*/

}
