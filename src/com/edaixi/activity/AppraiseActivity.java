package com.edaixi.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edaixi.data.KeepingData;
import com.edaixi.modle.OrderListItemBean;
import com.edaixi.util.Constants;
import com.edaixi.util.IsChinese;
import com.edaixi.util.LogUtil;
import com.edaixi.util.SaveUtils;
import com.edaixi.view.RongChainRatingBar.OnFaceIndexListener;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("HandlerLeak")
public class AppraiseActivity extends BaseActivity implements OnClickListener {
	private EditText edit_area;
	private Button commit_apprise;
	private TextView tv_show_comment_length;
	private TextView quyi_noconnect;
	private int quyi_noconnect_Index = 0;
	private TextView songyi_noconnect;
	private int songyi_noconnect_Index = 0;
	private TextView quyi_nosign;
	private int quyi_nosign_Index = 0;
	private TextView very_good;
	private int very_good_Index = 0;
	private ImageView apraise_back_btn;
	private LinearLayout ll_appraise_in;
	private LinearLayout ll_appraise_middle;
	private HashMap<String, String> parm;
	private SaveUtils saveUtils;
	private final int COMMENTSUCCED = 0;
	private final int COMMENTFAID = 1;
	private com.edaixi.view.RongChainRatingBar washing_ratingbar;
	private com.edaixi.view.RongChainRatingBar get_goods_ratingbar;
	private com.edaixi.view.RongChainRatingBar give_goods_ratingbar;
	private com.edaixi.view.RongChainRatingBar rb_whole_appraise;
	private StringBuilder commentStr = new StringBuilder();
	private OrderListItemBean orderitem;
	private Drawable drawablePress;
	private Drawable drawableDefault;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case COMMENTSUCCED:
				try {
					String data = new JSONObject(msg.obj.toString())
							.getString("data");
					JSONObject jsonObjectData = new JSONObject(data);
					String pingjiayue = jsonObjectData.getString("money");
					Intent intentSucess = new Intent();
					intentSucess.setClass(AppraiseActivity.this,
							AppraiseSucessActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("orderItem", orderitem);
					intentSucess.putExtra("TotalScore",
							String.valueOf(rb_whole_appraise.getFaceIndex()));
					intentSucess.putExtra("PingJiaYueE", pingjiayue);
					intentSucess.putExtras(bundle);
					startActivity(intentSucess);
					finish(0, 0);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case COMMENTFAID:
				showdialog("评价失败");
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_apppraise);
		init(this);
		initView();
	}

	private void initView() {
		saveUtils = new SaveUtils(getContext());
		parm = new HashMap<String, String>();
		orderitem = (OrderListItemBean) getIntent().getSerializableExtra(
				"orderItem");
		drawablePress = getResources().getDrawable(R.drawable.icon_choose_on);
		drawablePress.setBounds(0, 0, drawablePress.getMinimumWidth(),
				drawablePress.getMinimumHeight());
		drawableDefault = getResources()
				.getDrawable(R.drawable.icon_choose_off);
		drawableDefault.setBounds(0, 0, drawableDefault.getMinimumWidth(),
				drawableDefault.getMinimumHeight());
		ll_appraise_middle = (LinearLayout) findViewById(R.id.ll_appraise_middle);
		edit_area = (EditText) findViewById(R.id.edit_area);
		commit_apprise = (Button) findViewById(R.id.commit_apprise);
		commit_apprise.setOnClickListener(this);
		tv_show_comment_length = (TextView) findViewById(R.id.tv_show_comment_length);
		quyi_noconnect = (TextView) findViewById(R.id.quyi_noconnect);
		songyi_noconnect = (TextView) findViewById(R.id.songyi_noconnect);
		quyi_nosign = (TextView) findViewById(R.id.quyi_nosign);
		very_good = (TextView) findViewById(R.id.very_good);
		quyi_noconnect.setOnClickListener(this);
		songyi_noconnect.setOnClickListener(this);
		quyi_nosign.setOnClickListener(this);
		very_good.setOnClickListener(this);
		ll_appraise_in = (LinearLayout) findViewById(R.id.ll_appraise_in);
		apraise_back_btn = (ImageView) findViewById(R.id.apraise_back_btn);
		apraise_back_btn.setOnClickListener(this);
		washing_ratingbar = (com.edaixi.view.RongChainRatingBar) findViewById(R.id.washing_ratingbar);
		washing_ratingbar.setRatingTitle("洗衣质量");
		washing_ratingbar.setRatingType(true);
		washing_ratingbar.selectAllFaceDefault();
		get_goods_ratingbar = (com.edaixi.view.RongChainRatingBar) findViewById(R.id.get_goods_ratingbar);
		get_goods_ratingbar.setRatingTitle("取件人员");
		get_goods_ratingbar.setRatingType(true);
		get_goods_ratingbar.selectAllFaceDefault();
		give_goods_ratingbar = (com.edaixi.view.RongChainRatingBar) findViewById(R.id.give_goods_ratingbar);
		give_goods_ratingbar.setRatingTitle("送件人员");
		give_goods_ratingbar.setRatingType(true);
		give_goods_ratingbar.selectAllFaceDefault();
		rb_whole_appraise = (com.edaixi.view.RongChainRatingBar) findViewById(R.id.rb_whole_appraise);
		rb_whole_appraise.setRatingTitle("整体评价");
		rb_whole_appraise.setRatingType(false);
		rb_whole_appraise.selectAllFaceDefault();
		rb_whole_appraise.setYourListener(new OnFaceIndexListener() {

			@Override
			public void getFaceIndex(int faceIndex) {
				ll_appraise_middle.setVisibility(View.VISIBLE);
				ll_appraise_in.setVisibility(View.VISIBLE);
				commit_apprise.setClickable(true);
				commit_apprise.setFocusable(true);
				commit_apprise.setBackgroundResource(R.drawable.appraise_press);
				if (faceIndex == 5) {
					washing_ratingbar.setFaceImageSource(5);
					get_goods_ratingbar.setFaceImageSource(5);
					give_goods_ratingbar.setFaceImageSource(5);
				}
			}
		});
		commit_apprise.setClickable(false);
		commit_apprise.setFocusable(false);
		commit_apprise.setBackgroundResource(R.drawable.appraise_default);
		if (orderitem != null) {
			if (orderitem.getCan_be_commented().equals("1")) {
				// 已评价
				quyi_noconnect.setClickable(false);
				songyi_noconnect.setClickable(false);
				quyi_nosign.setClickable(false);
				very_good.setClickable(false);
				edit_area.setFocusable(false);
				rb_whole_appraise.setOffClickable();
				rb_whole_appraise.setFaceImageSource(Integer.valueOf(orderitem
						.getTotal_score()));
				washing_ratingbar.setOffClickable();
				washing_ratingbar.setFaceImageSource(Integer.valueOf(orderitem
						.getWashing_score()));
				get_goods_ratingbar.setOffClickable();
				get_goods_ratingbar.setFaceImageSource(Integer
						.valueOf(orderitem.getLogistics_score()));
				give_goods_ratingbar.setOffClickable();
				give_goods_ratingbar.setFaceImageSource(Integer
						.valueOf(orderitem.getService_score()));
				commit_apprise.setClickable(false);
				commit_apprise.setFocusable(false);
				commit_apprise.setBackgroundResource(R.drawable.appraise_default);
				if (!TextUtils.isEmpty(orderitem.getOrder_comments())) {
					edit_area.setText(orderitem.getOrder_comments()
							.replace("取衣前未提前联系", "").replace("送回前未提前联系", "")
							.replace("取衣时未当面封签", "").replace("体验很赞", "")
							.replace(",", ""));
					Drawable nav_up = this.getResources().getDrawable(
							R.drawable.icon_choose_on);
					nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
							nav_up.getMinimumHeight());
					if (orderitem.getOrder_comments().contains("取衣前未提前联系")) {
						quyi_noconnect.setCompoundDrawables(nav_up, null, null,
								null);
					}
					if (orderitem.getOrder_comments().contains("送回前未提前联系")) {
						songyi_noconnect.setCompoundDrawables(nav_up, null,
								null, null);
					}
					if (orderitem.getOrder_comments().contains("取衣时未当面封签")) {
						quyi_nosign.setCompoundDrawables(nav_up, null, null,
								null);
					}
					if (orderitem.getOrder_comments().contains("体验很赞")) {
						very_good
								.setCompoundDrawables(nav_up, null, null, null);
					}
				} else {
					edit_area.setText(" ");
				}
			}
		}
		edit_area.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					String countLengthString = s.length() + "";
					SpannableString ss = new SpannableString(s.length()
							+ "/200");
					ss.setSpan(
							new ForegroundColorSpan(Color.parseColor("#35b6ff")),
							0, countLengthString.length(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					tv_show_comment_length.setText(ss);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quyi_noconnect:
			if ((quyi_noconnect_Index % 2) == 0) {
				quyi_noconnect.setCompoundDrawables(drawablePress, null, null,
						null);
			} else {
				quyi_noconnect.setCompoundDrawables(drawableDefault, null,
						null, null);
			}
			quyi_noconnect_Index++;
			break;
		case R.id.songyi_noconnect:
			if ((songyi_noconnect_Index % 2) == 0) {
				songyi_noconnect.setCompoundDrawables(drawablePress, null,
						null, null);
			} else {
				songyi_noconnect.setCompoundDrawables(drawableDefault, null,
						null, null);
			}
			songyi_noconnect_Index++;
			break;
		case R.id.quyi_nosign:
			if ((quyi_nosign_Index % 2) == 0) {
				quyi_nosign.setCompoundDrawables(drawablePress, null, null,
						null);
			} else {
				quyi_nosign.setCompoundDrawables(drawableDefault, null, null,
						null);
			}
			quyi_nosign_Index++;
			break;
		case R.id.very_good:
			if ((very_good_Index % 2) == 0) {
				very_good.setCompoundDrawables(drawablePress, null, null, null);
			} else {
				very_good.setCompoundDrawables(drawableDefault, null, null,
						null);
			}
			very_good_Index++;
			break;
		case R.id.apraise_back_btn:
			finish(0, 0);
			break;
		case R.id.commit_apprise:
			if (rb_whole_appraise.getFaceIndex() == 0) {
				showdialog("请为整体评价评分");
				return;
			}
			if (washing_ratingbar.getFaceIndex() == 0) {
				showdialog("请为洗衣质量评分");
				return;
			}
			if (get_goods_ratingbar.getFaceIndex() == 0) {
				showdialog("请为取件人员评分");
				return;
			}
			if (give_goods_ratingbar.getFaceIndex() == 0) {
				showdialog("请为送件人员评分");
				return;
			}
			if (IsChinese.iszhongwen(edit_area.getText().toString())) {
				showdialog("评价内容不能含有非法字符");
				return;
			}
			if (edit_area.getText().toString().length() > 200) {
				showdialog("评价内容过长");
				return;
			}
			getSelectComment();
			TCAgent.onEvent(AppraiseActivity.this, "评价提交");
			parm.clear();
			parm.put("user_id", saveUtils.getStrSP(KeepingData.USER_ID));
			parm.put("order_id", orderitem.getOrder_id());
			parm.put("washing_score",
					String.valueOf(washing_ratingbar.getFaceIndex()));
			parm.put("logistics_score",
					String.valueOf(get_goods_ratingbar.getFaceIndex()));
			parm.put("service_score",
					String.valueOf(give_goods_ratingbar.getFaceIndex()));
			parm.put("total_score",
					String.valueOf(rb_whole_appraise.getFaceIndex()));
			parm.put("comment", (commentStr.toString().trim() + edit_area
					.getText().toString().replace("\n", "").replace(" ", "")
					.trim()).trim());
			postdate(parm, Constants.getcreateordercomment, handler,
					COMMENTSUCCED, COMMENTFAID, true, true);
			break;
		default:
			break;
		}
	}

	// 获取用户选择评价的条目
	public void getSelectComment() {
		if ((quyi_noconnect_Index % 2) != 0) {
			commentStr.append("取衣前未提前联系,");
		}
		if ((songyi_noconnect_Index % 2) != 0) {
			commentStr.append("送回前未提前联系,");
		}
		if ((quyi_nosign_Index % 2) != 0) {
			commentStr.append("取衣时未当面封签,");
		}
		if ((very_good_Index % 2) != 0) {
			commentStr.append("体验很赞,");
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
