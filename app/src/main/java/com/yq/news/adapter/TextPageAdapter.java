package com.yq.news.adapter;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.bumptech.glide.Glide;
import com.yq.news.R;
import com.yq.news.model.ArticleInfo;
import com.yq.news.net.ApiUtils;
import com.yq.news.view.WaterMarkBg;
import com.yq.util.RelativeDateFormat;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * 首页  文本类
 */
public class TextPageAdapter implements ItemViewDelegate<ArticleInfo>  {

    private Context mContext ;

    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public float mTextSize ;

    public TextPageAdapter(Context context,float textSize)
    {
        mContext = context ;
        mTextSize = textSize ;
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.fragment_article_txt_item;
    }

    @Override
    public boolean isForViewType(ArticleInfo item, int position) {
        List<ArticleInfo.ImglistBean> list = item.getImglist();
        return list.size() == 0;
    }

    @Override
    public void convert(ViewHolder holder, ArticleInfo articleInfo, int position) {

        TextView title = holder.getView(R.id.article_title);
        title.setText(articleInfo.getTitle());
        Log.e("TextSize",mTextSize+"");
        title.setTextSize(mTextSize);
        holder.setText(R.id.user_name_tv,articleInfo.getDomain());
        holder.setText(R.id.create_time_tv,  RelativeDateFormat.timeStamp2Date(articleInfo.getCreatetime()));
    }
}
