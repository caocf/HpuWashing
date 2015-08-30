package com.edaixi.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class GuideAddressTipsActivity extends BaseActivity implements
		OnClickListener {

	private ImageView iv_guide_address_ok;
	private ImageView iv_guide_add_address;
	private ImageView iv_guide_edit_address;
	private ImageView iv_guide_delete_address;
	private int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide_address_tips);
		init(this);
		iv_guide_add_address = (ImageView) findViewById(R.id.iv_guide_add_address);
		iv_guide_edit_address = (ImageView) findViewById(R.id.iv_guide_edit_address);
		iv_guide_delete_address = (ImageView) findViewById(R.id.iv_guide_delete_address);
		iv_guide_address_ok = (ImageView) findViewById(R.id.iv_guide_address_ok);
		iv_guide_address_ok.setOnClickListener(this);
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_guide_address_ok:
			count++;
			if (count == 1) {
				iv_guide_add_address.setVisibility(View.INVISIBLE);
				iv_guide_edit_address.setVisibility(View.VISIBLE);
				iv_guide_delete_address.setVisibility(View.INVISIBLE);
			}
			if (count == 2) {
				iv_guide_add_address.setVisibility(View.INVISIBLE);
				iv_guide_edit_address.setVisibility(View.INVISIBLE);
				iv_guide_delete_address.setVisibility(View.VISIBLE);
			}
			if (count == 3) {
				this.finish();
			}
			break;
		}
	}

}
