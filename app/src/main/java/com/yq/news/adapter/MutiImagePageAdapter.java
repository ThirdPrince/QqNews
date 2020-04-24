package com.yq.news.adapter;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yq.news.R;
import com.yq.news.model.ArticleInfo;
import com.yq.news.net.ApiUtils;
import com.yq.util.RelativeDateFormat;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * 首页 多张图片类 最多三张
 */
public class MutiImagePageAdapter implements ItemViewDelegate<ArticleInfo>  {

    private static final String TAG = "MutiImagePageAdapter";

    private Context mContext ;
    private RequestOptions options ;
    private String imageUrl = "";

    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public float mTextSize ;

    public MutiImagePageAdapter(Context context,float mTextSize)
    {
         mContext = context ;
         options = new RequestOptions();
        options.placeholder(R.drawable.news_icon);

        if(ApiUtils.BASE_URL.startsWith("https"))
        {
            imageUrl = ApiUtils.BASE_URL;
        }else
        {
            imageUrl = ApiUtils.BASE_ADDRESS;
        }

    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.fragment_item_three_pics_news;
    }

    @Override
    public boolean isForViewType(ArticleInfo item, int position) {
        List<ArticleInfo.ImglistBean> list = item.getImglist();
        return (list != null && list.size()>=2);
    }

    @Override
    public void convert(ViewHolder holder, ArticleInfo articleInfo, int position) {
        ImageView imageView1 = holder.getView(R.id.article_img_1);
        ImageView imageView2 = holder.getView(R.id.article_img_2);
        ImageView imageView3 = holder.getView(R.id.article_img_3);
        List<ArticleInfo.ImglistBean> list = articleInfo.getImglist();
        String imageUrl1  = list.get(0).getImgurl();
        String imageUrl2  = list.get(1).getImgurl();
        Glide.with(mContext)
                .load(imageUrl + imageUrl1)
                .apply(options)
                .into(imageView1);
        Glide.with(mContext)
                .load(imageUrl + imageUrl2)
                .apply(options)
                .into(imageView2);
       /* Glide.with(mContext).load(ApiUtils.BASE_ADDRESS + imageUrl1).placeholder(R.drawable.news_temp).error(R.drawable.news_temp).into(imageView1);
        Glide.with(mContext).load(ApiUtils.BASE_ADDRESS + imageUrl2).placeholder(R.drawable.news_temp).error(R.drawable.news_temp).into(imageView2);*/
        String imageUrl3 = "";
        if(list.size()>2) {
            imageUrl3 = list.get(2).getImgurl();
            //Glide.with(mContext).load(ApiUtils.BASE_ADDRESS + imageUrl3).placeholder(R.drawable.news_temp).error(R.drawable.news_temp).into(imageView3);
            Glide.with(mContext)
                    .load(imageUrl + imageUrl3)
                    .apply(options)
                    .into(imageView3);
        }


        TextView title = holder.getView(R.id.article_title);
        title.setText(articleInfo.getTitle());
        Log.e("TextSize",mTextSize+"");
        title.setTextSize(mTextSize);
        holder.setText(R.id.user_name_tv,articleInfo.getDomain());
        holder.setText(R.id.create_time_tv,  RelativeDateFormat.timeStamp2Date(articleInfo.getCreatetime()));
    }
}
