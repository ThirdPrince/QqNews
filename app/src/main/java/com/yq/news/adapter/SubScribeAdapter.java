package com.yq.news.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.yq.news.R;
import com.yq.news.itf.OnSubcribleClick;
import com.yq.news.model.SubscribleInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class SubScribeAdapter extends CommonAdapter<SubscribleInfo.ListBean> {


    private Context context ;
    private OnSubcribleClick onSubcribleClick ;

    public void setOnSubcribleClick(OnSubcribleClick onSubcribleClick) {
        this.onSubcribleClick = onSubcribleClick;
    }

    public SubScribeAdapter(Context context, int layoutId, List<SubscribleInfo.ListBean> datas) {
        super(context, layoutId, datas);
        this.context = context ;
    }

    @Override
    protected void convert(ViewHolder holder, final SubscribleInfo.ListBean listBean, int position) {

        TextView tv =  holder.getView(R.id.name_tv);
        tv.setText(listBean.getName());

        TextView subscribleTv =  holder.getView(R.id.subscribe_tv);
        tv.setText(listBean.getName());

        Drawable leftDrawable = null;
        leftDrawable = mContext.getResources().getDrawable(R.drawable.cancel_follow);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
                leftDrawable.getMinimumHeight());

        if(listBean.getSubscribe()==1)
        {
            leftDrawable = mContext.getResources().getDrawable(R.drawable.follow);
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
                    leftDrawable.getMinimumHeight());
            subscribleTv.setSelected(true);
        }else
        {
            leftDrawable = mContext.getResources().getDrawable(R.drawable.cancel_follow);
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
                    leftDrawable.getMinimumHeight());
            subscribleTv.setSelected(false);
        }
        subscribleTv.setCompoundDrawablePadding(10);
        subscribleTv.setCompoundDrawables(leftDrawable, null,null , null);
        subscribleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSubcribleClick != null)
                {
                    onSubcribleClick.onClick(listBean,subscribleTv);
                }
            }
        });
    }
}
