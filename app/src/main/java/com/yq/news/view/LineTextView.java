package com.yq.news.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.yq.news.R;

public class LineTextView extends TextView {
	private Paint mPaint;
	
	public LineTextView(Context context) {
		this(context, null);
	}

	public LineTextView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.textViewStyle);
	}
	
	public LineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(getResources().getColor(R.color.m_divider_line_color));
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 画底线
		int margin = ConvertUtils.dp2px(45);
		canvas.drawLine(margin, getHeight() - 1, getWidth(), getHeight() - 1, mPaint);
	}
}
