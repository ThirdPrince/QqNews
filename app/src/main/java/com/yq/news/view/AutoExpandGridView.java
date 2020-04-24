package com.yq.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 工作圈发布图片的自定义GridView
 */
public class AutoExpandGridView extends GridView {

	public AutoExpandGridView(Context context) {
		super(context);
	}
	
	public AutoExpandGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public AutoExpandGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}