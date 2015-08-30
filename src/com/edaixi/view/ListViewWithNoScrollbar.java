package com.edaixi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 覆写ListView，实现无滚动条的情况 注意这里只适用于没有divider分割线的情况，否则会出现没有考虑分割线导致计算不完全
 * 
 * @author A Shuai
 * 
 */
public class ListViewWithNoScrollbar extends ListView {

	public ListViewWithNoScrollbar(Context context) {
		this(context, null);
	}

	public ListViewWithNoScrollbar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewWithNoScrollbar(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}