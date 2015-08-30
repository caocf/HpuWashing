package com.edaixi.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;
import com.edaixi.modle.OrderListItemBean;
import com.edaixi.util.LogUtil;
import com.edaixi.util.OrderListAdapterEvent;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import de.greenrobot.event.EventBus;

@SuppressLint("HandlerLeak")
public class AppraiseSucessActivity extends BaseActivity implements
		OnClickListener {

	private TextView tv_show_appraise;
	private TextView tv_appraise_ordersn;
	private RatingBar rb_whole_appraise_sucess;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_apppraisesucess);
		init(this);
		findViewById(R.id.commit_apprise_sucess).setOnClickListener(this);
		findViewById(R.id.appraise_back_btn).setOnClickListener(this);
		tv_show_appraise = (TextView) findViewById(R.id.tv_show_appraise);
		tv_appraise_ordersn = (TextView) findViewById(R.id.tv_appraise_ordersn);
		rb_whole_appraise_sucess = (RatingBar) findViewById(R.id.rb_whole_appraise_sucess);

		OrderListItemBean orderitem = (OrderListItemBean) getIntent()
				.getSerializableExtra("orderItem");
		String pingjiayue = getIntent().getExtras().getString("PingJiaYueE");
		String totalScore = getIntent().getExtras().getString("TotalScore");
		if (orderitem != null) {
			String orderSn = orderitem.getOrder_sn();
			StringBuilder sBuilder = new StringBuilder(orderSn);
			tv_appraise_ordersn.setText(sBuilder.insert(orderSn.length() - 6,
					"  "));
			rb_whole_appraise_sucess.setIsIndicator(true);
			if (totalScore != null) {
				rb_whole_appraise_sucess.setRating(Float.valueOf(totalScore));
			}
		}
		tv_show_appraise
				.setText(Html
						.fromHtml("<font size=\"3\" color=\"black\">评价成功,获得</font><font size=\"3\" color=\"#fa7d47\">"
								+ pingjiayue
								+ "</font><font size=\"3\" color=\"black\">元余额</font>"));
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit_apprise_sucess:
		case R.id.appraise_back_btn:
			EventBus.getDefault().post(
					new OrderListAdapterEvent("PingJiaSucess"));
			finish();
			break;
		}
	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("AppraiseActivity");
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("AppraiseActivity");
		MobclickAgent.onPause(this);
	}
}
