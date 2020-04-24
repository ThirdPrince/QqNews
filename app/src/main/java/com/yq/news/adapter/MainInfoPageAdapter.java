package com.yq.news.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.tools.ScreenUtils;
import com.yq.news.R;
import com.yq.news.model.ArticleInfo;
import com.yq.news.net.ApiUtils;
import com.yq.util.RelativeDateFormat;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import static com.yq.util.Constant.TEXT_FONT_BIG_SIZE;
import static com.yq.util.Constant.TEXT_FONT_SIZE;
import static com.yq.util.Constant.TEXT_FONT_SMALL_SIZE;
import static com.yq.util.Constant.TEXT_FONT_STAND_SIZE;
import static com.yq.util.Constant.TEXT_FONT_SUPER_BIG_SIZE;


/**
 * 首页
 */
public class MainInfoPageAdapter extends MultiItemTypeAdapter<ArticleInfo> {


    private  Context mContext ;

    private float textSize = 0;

    private TextPageAdapter textPageAdapter ;
    private ImagePageAdapter imagePageAdapter ;
    private MutiImagePageAdapter mutiImagePageAdapter ;
    public MainInfoPageAdapter(Context context, List<ArticleInfo> datas)
    {
        super(context, datas);
        mContext = context ;
        refreshTextSize();
        textPageAdapter = new TextPageAdapter(mContext,textSize);
        imagePageAdapter = new ImagePageAdapter(mContext,textSize);
        mutiImagePageAdapter = new MutiImagePageAdapter(mContext,textSize);
        addItemViewDelegate(textPageAdapter);
        addItemViewDelegate(imagePageAdapter);
        addItemViewDelegate(mutiImagePageAdapter);
    }

    public void refreshTextSize()
    {

        int text_size = SPUtils.getInstance().getInt(TEXT_FONT_SIZE);
        switch (text_size)
        {
            case TEXT_FONT_SMALL_SIZE:
                textSize = 16;
                break;

            case TEXT_FONT_STAND_SIZE:
                textSize = 17;
                break;

            case TEXT_FONT_BIG_SIZE:
                textSize = 19;
                break;

            case TEXT_FONT_SUPER_BIG_SIZE:
                textSize = 21;
                break;
        }
        if(textPageAdapter != null)
        {
            textPageAdapter.setmTextSize(textSize);
        }
        if(imagePageAdapter !=null)
        {
            imagePageAdapter.setmTextSize(textSize);
        }
        if(mutiImagePageAdapter != null)
        {
            mutiImagePageAdapter.setmTextSize(textSize);
        }
    }

}
