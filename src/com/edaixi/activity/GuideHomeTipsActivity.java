package com.edaixi.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class GuideHomeTipsActivity extends BaseActivity implements OnClickListener {

	private ImageView iv_guide_ok;
	private ImageView iv_guide_city;
	private ImageView iv_guide_area;
	private ImageView iv_guide_washing;
	private int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide_home_tips);
		init(this);
		iv_guide_city = (ImageView) findViewById(R.id.iv_guide_city);
		iv_guide_area = (ImageView) findViewById(R.id.iv_guide_area);
		iv_guide_washing = (ImageView) findViewById(R.id.iv_guide_washing);
		iv_guide_ok = (ImageView) findViewById(R.id.iv_guide_ok);
		iv_guide_ok.setOnClickListener(this);
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_guide_ok:
			count++;
			if (count == 1) {
				iv_guide_city.setVisibility(View.INVISIBLE);
				iv_guide_area.setVisibility(View.VISIBLE);
				iv_guide_washing.setVisibility(View.INVISIBLE);
			}
			if (count == 2) {
				iv_guide_city.setVisibility(View.INVISIBLE);
				iv_guide_area.setVisibility(View.INVISIBLE);
				iv_guide_washing.setVisibility(View.VISIBLE);
			}
			if (count == 3) {
				this.finish();
			}
			break;
		}
	}

}
