package com.edaixi.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.edaixi.activity.R;

public class DeliveryLineViewMaster extends LinearLayout {

	private ImageView delivery_master_view;

	@SuppressLint("NewApi")
	public DeliveryLineViewMaster(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public DeliveryLineViewMaster(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public DeliveryLineViewMaster(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.delivery_line_master_layout, this,
				true);
		delivery_master_view = (ImageView) v.findViewById(R.id.delivery_master_view);
	}

	public void setViewHeigh(int mHeight) {
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) delivery_master_view
				.getLayoutParams();
		linearParams.height = mHeight;
		linearParams.width = 1;
		delivery_master_view.setLayoutParams(linearParams);
	}

	
}
