package com.edaixi.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.edaixi.activity.R;

public class DeliveryLineView extends LinearLayout {

	private View delivery_branch_view;

	@SuppressLint("NewApi")
	public DeliveryLineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public DeliveryLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public DeliveryLineView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.delivery_line_layout, this, true);
		delivery_branch_view = v.findViewById(R.id.delivery_branch_view);
	}

	public void setViewHeigh(int mHeight) {
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) delivery_branch_view
				.getLayoutParams();
		linearParams.height = mHeight;
		linearParams.width = 1;
		delivery_branch_view.setLayoutParams(linearParams);
	}
}
