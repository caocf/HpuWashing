package com.edaixi.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class GuideOrderTipsActivity extends BaseActivity implements
		OnClickListener {

	private ImageView iv_guide_order_ok;
	private ImageView iv_guide_price;
	private ImageView iv_guide_address;
	private ImageView iv_guide_ordertime;
	private int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide_order_tips);
		init(this);
		iv_guide_price = (ImageView) findViewById(R.id.iv_guide_price);
		iv_guide_address = (ImageView) findViewById(R.id.iv_guide_address);
		iv_guide_ordertime = (ImageView) findViewById(R.id.iv_guide_ordertime);
		iv_guide_order_ok = (ImageView) findViewById(R.id.iv_guide_order_ok);
		iv_guide_order_ok.setOnClickListener(this);
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_guide_order_ok:
			count++;
			if (count == 1) {
				iv_guide_price.setVisibility(View.INVISIBLE);
				iv_guide_address.setVisibility(View.VISIBLE);
				iv_guide_ordertime.setVisibility(View.INVISIBLE);
			}
			if (count == 2) {
				iv_guide_price.setVisibility(View.INVISIBLE);
				iv_guide_address.setVisibility(View.INVISIBLE);
				iv_guide_ordertime.setVisibility(View.VISIBLE);
			}
			if (count == 3) {
				this.finish();
			}
			break;
		}
	}

}
