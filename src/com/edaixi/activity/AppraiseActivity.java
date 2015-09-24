package com.edaixi.activity;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.OrderListItemBean;
import com.edaixi.util.Constants;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.util.SaveUtils;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import de.greenrobot.event.EventBus;

@SuppressLint("HandlerLeak")
public class AppraiseActivity extends BaseActivity implements OnClickListener {
	private EditText edit_area;
	private Button commit_apprise;
	private CheckBox quyi_noconnect;
	private CheckBox songyi_noconnect;
	private CheckBox quyi_nosign;
	private CheckBox very_good;
	private ImageView apraise_back_btn;
	private HashMap<String, String> parm;
	private SaveUtils saveUtils;
	private Handler handler;
	private final int COMMENTSUCCED = 0;
	private final int COMMENTFAID = 1;
	private RatingBar ratingBar1;
	private RatingBar ratingBar2;
	private RatingBar ratingBar3;
	private float rating1 = 0.0f;

	private float rating3 = 0.0f;
	private float rating2 = 0.0f;
	private TextView order_num;
	private String str = "";
	private OrderListItemBean orderitem;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_apppraise);
		init(this);
		initView();
	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("AppraiseActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("AppraiseActivity"); // 保证 onPageEnd 在onPause
														// 之前调用,因为 onPause
														// 中会保存信息
		MobclickAgent.onPause(this);
	}

	private void initView() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case COMMENTSUCCED:
					showdialog("感谢您的评价，我们会更加努力");
					new Thread() {
						@Override
						public void run() {
							super.run();
							try {
								Thread.sleep(2002);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							EventBus.getDefault().post(
									new OrderListAdapterEvent("PingJiaSucess"));
							finish(0,0);
						}
					}.start();

					break;
				case COMMENTFAID:
					showdialog("评价失败");
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		saveUtils = new SaveUtils(getContext());
		parm = new HashMap<String, String>();
		edit_area = (EditText) findViewById(R.id.edit_area);
		commit_apprise = (Button) findViewById(R.id.commit_apprise);
		commit_apprise.setOnClickListener(this);
		quyi_noconnect = (CheckBox) findViewById(R.id.quyi_noconnect);
		songyi_noconnect = (CheckBox) findViewById(R.id.songyi_noconnect);
		quyi_nosign = (CheckBox) findViewById(R.id.quyi_nosign);
		very_good = (CheckBox) findViewById(R.id.very_good);
		apraise_back_btn = (ImageView) findViewById(R.id.apraise_back_btn);
		apraise_back_btn.setOnClickListener(this);
		ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
		ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);
		ratingBar3 = (RatingBar) findViewById(R.id.ratingBar3);
		order_num = (TextView) findViewById(R.id.order_num);
		orderitem = (OrderListItemBean) getIntent().getSerializableExtra(
				"orderItem");
		if (orderitem != null) {
			order_num.setText("订单编号：" + orderitem.getOrder_sn());
			if (orderitem.getCan_be_commented().equals("1")) {
				// 已评价
				quyi_noconnect.setClickable(false);
				songyi_noconnect.setClickable(false);
				quyi_nosign.setClickable(false);
				very_good.setClickable(false);
				ratingBar1.setIsIndicator(true);
				ratingBar2.setIsIndicator(true);
				ratingBar3.setIsIndicator(true);
				edit_area.setFocusable(false);
				commit_apprise.setClickable(false);
				commit_apprise.setFocusable(false);
				commit_apprise.setBackgroundResource(R.drawable.n_click_press);
				ratingBar1
						.setRating(Float.valueOf(orderitem.getWashing_score()));
				ratingBar2.setRating(Float.valueOf(orderitem
						.getLogistics_score()));
				ratingBar3.setRating(Float.valueOf(orderitem
						.getLogistics_score()));
				if (!TextUtils.isEmpty(orderitem.getOrder_comments())) {
					edit_area.setText(orderitem.getOrder_comments());
				} else {
					edit_area.setText("  ");
				}

			}
		}

		OnRatingBarChangeListener changeListener = new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				switch (ratingBar.getId()) {
				case R.id.ratingBar1:
					rating1 = ratingBar1.getRating();
					break;
				case R.id.ratingBar2:
					rating2 = ratingBar2.getRating();
					break;
				case R.id.ratingBar3:
					rating3 = ratingBar3.getRating();
					break;
				default:
					break;
				}

			}
		};
		ratingBar1.setOnRatingBarChangeListener(changeListener);
		ratingBar2.setOnRatingBarChangeListener(changeListener);
		ratingBar3.setOnRatingBarChangeListener(changeListener);
		CheckedchangeCallback checkedchangeCallback = new CheckedchangeCallback();
		quyi_noconnect.setOnCheckedChangeListener(checkedchangeCallback);
		songyi_noconnect.setOnCheckedChangeListener(checkedchangeCallback);
		quyi_nosign.setOnCheckedChangeListener(checkedchangeCallback);
		very_good.setOnCheckedChangeListener(checkedchangeCallback);
	}

	private class CheckedchangeCallback implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				str = buttonView.getText().toString().trim() + "," + str;
			} else {
				str = str.replace(buttonView.getText().toString().trim() + ",",
						"");
			}

		}
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0,0);
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.apraise_back_btn:
			finish(0,0);
			break;
		case R.id.commit_apprise:
			if (rating1 == 0.0f) {
				showdialog("请为洗衣质量评分");
				return;
			}
			if (rating2 == 0.0f) {
				showdialog("请为快递速度评分");
				return;
			}
			if (rating3 == 0.0f) {
				showdialog("请为服务态度评分");
				return;
			}
			TCAgent.onEvent(AppraiseActivity.this, "评价提交");
			parm.clear();
			parm.put("user_id", saveUtils.getStrSP(KeepingData.USER_ID));
			parm.put("order_id", orderitem.getOrder_id());
			parm.put("washing_score", String.valueOf(rating1));
			parm.put("logistics_score", String.valueOf(rating2));
			parm.put("service_score", String.valueOf(rating3));
			parm.put("total_score", String.valueOf(5.0f));
			parm.put("comment",
					(str + edit_area.getText().toString().replace("\n", "")
							.trim()).trim());
			postdate(parm, Constants.getcreateordercomment, handler,
					COMMENTSUCCED, COMMENTFAID, true, true);
			break;
		default:
			break;
		}

	}

}
