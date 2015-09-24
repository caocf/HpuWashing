package com.edaixi.fragment;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.edaixi.Enum.UseConponType;
import com.edaixi.Enum.WebPageType;
import com.edaixi.activity.BindEntitycardActivity;
import com.edaixi.activity.CouponActivity;
import com.edaixi.activity.DepositActivity;
import com.edaixi.activity.LoginActivity;
import com.edaixi.activity.MainActivity;
import com.edaixi.activity.MoreActivity;
import com.edaixi.activity.MyAddressActivity;
import com.edaixi.activity.R;
import com.edaixi.activity.WebActivity;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.ExtraCardBean;
import com.edaixi.modle.GetCouponsBean;
import com.edaixi.modle.Icard;
import com.edaixi.util.Constants;
import com.edaixi.util.LogUtil;
import com.edaixi.util.MyhttpUtils;
import com.edaixi.util.NetUtil;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.util.SaveUtils;
import com.edaixi.view.SharePacketsDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tendcloud.tenddata.TCAgent;

import de.greenrobot.event.EventBus;

@SuppressLint("HandlerLeak")
public class MineFragment extends BaseFragment {
	private final static int[] mineListIcons = { R.drawable.mine_coupon_logo,
			R.drawable.mine_address_logo, R.drawable.mine_share_logo,
			R.drawable.mine_bindcard_logo, R.drawable.mine_idea_logo,
			R.drawable.mine_setting_logo };
	private final static String[] mineListTitles = { "优惠券", "常用地址", "分享给好友",
			"绑定实体卡", "意见反馈", "更多设置" };
	private TextView user_tel_text;
	private TextView tv_mine_item_icard;
	private TextView tv_mine_recharge_btn;
	private TextView tv_mine_icard_uxury;
	private TextView tv_mine_luxury_text;
	private TextView tv_mine_luxury_time;
	private ListView my_list;
	private MineAdapter mineAdapter;
	HashMap<String, String> parms;
	boolean isrefresh = false;
	private SaveUtils saveUtils;
	MyhttpUtils utils;
	private String coin;
	private final int GETICARDSUCCED = 0;
	private final int GETICARDFAILD = 1;
	private final int GETEXTRAICARDSUCCED = 2;
	private final int GETEXTRAICARDFAILD = 3;
	private Icard icard;
	private ImageView addresslist_no_order;
	private ImageView addresslist_loading;
	private RelativeLayout rl_mine_title;
	private RelativeLayout rl_mine_recharge;
	private RelativeLayout rl_mine_luxury;

	public MineFragment() {

	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GETICARDSUCCED:
				my_list.setVisibility(View.VISIBLE);
				rl_mine_recharge.setVisibility(View.VISIBLE);
				addresslist_no_order.setVisibility(View.GONE);
				try {
					JSONObject object = new JSONObject((String) msg.obj);
					if (object.getBoolean("ret")) {
						Gson gson = new Gson();
						icard = gson.fromJson(object.getString("data"),
								Icard.class);
						coin = icard.getCoin();
						saveUtils.saveStrSP("coin", coin);
						if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
							if (!TextUtils.isEmpty(coin) && coin != "") {
								DecimalFormat df = new DecimalFormat("0.00");
								double d1 = Double.parseDouble(coin);
								tv_mine_item_icard.setTextColor(getResources()
										.getColor(R.color.red));
								tv_mine_item_icard.setText("￥" + df.format(d1));
								mineAdapter.notifyDataSetChanged();
							}
						} else {
							tv_mine_item_icard.setText("暂无信息");
							tv_mine_item_icard.setTextColor(getResources()
									.getColor(R.color.falseaddress));
						}
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case GETICARDFAILD:
				break;
			case GETEXTRAICARDSUCCED:
				String extracardString = msg.obj.toString();
				Gson gson = new Gson();
				GetCouponsBean fromJson = gson.fromJson(extracardString,
						GetCouponsBean.class);
				String data = fromJson.getData();
				Type contentType = new TypeToken<ArrayList<ExtraCardBean>>() {
				}.getType();
				List<ExtraCardBean> mExtraCardBeans = null;
				try {
					mExtraCardBeans = gson.fromJson(data, contentType);
					if (mExtraCardBeans.size() > 0) {
						rl_mine_luxury.setVisibility(View.VISIBLE);
						tv_mine_luxury_text.setVisibility(View.VISIBLE);
						tv_mine_luxury_time.setVisibility(View.VISIBLE);
						tv_mine_luxury_time.setText(mExtraCardBeans.get(0)
								.getCreated_at()
								+ "到"
								+ mExtraCardBeans.get(0).getAvailable_at());
					} else {
						rl_mine_luxury.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case GETEXTRAICARDFAILD:
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine, container, false);
		user_tel_text = (TextView) view.findViewById(R.id.user_tel_text);
		tv_mine_item_icard = (TextView) view
				.findViewById(R.id.tv_mine_item_icard);
		tv_mine_recharge_btn = (TextView) view
				.findViewById(R.id.tv_mine_recharge_btn);
		tv_mine_recharge_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TCAgent.onEvent(getActivity(), "我的页面_充值");
				if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
					Intent intent = new Intent(getActivity(),
							DepositActivity.class);
					intent.putExtra("coin", coin);
					startActivity(intent);
				} else {
					startActivity(new Intent(getActivity(), LoginActivity.class));
				}
			}
		});
		tv_mine_icard_uxury = (TextView) view
				.findViewById(R.id.tv_mine_icard_uxury);
		tv_mine_icard_uxury = (TextView) view
				.findViewById(R.id.tv_mine_icard_uxury);
		tv_mine_luxury_text = (TextView) view
				.findViewById(R.id.tv_mine_luxury_text);
		tv_mine_luxury_time = (TextView) view
				.findViewById(R.id.tv_mine_luxury_time);
		addresslist_no_order = (ImageView) view
				.findViewById(R.id.addresslist_no_order);
		addresslist_loading = (ImageView) view
				.findViewById(R.id.addresslist_loading);
		my_list = (ListView) view.findViewById(R.id.my_list);
		my_list.setVerticalScrollBarEnabled(false);
		rl_mine_title = (RelativeLayout) view.findViewById(R.id.rl_mine_title);
		rl_mine_recharge = (RelativeLayout) view
				.findViewById(R.id.rl_mine_recharge);
		rl_mine_luxury = (RelativeLayout) view
				.findViewById(R.id.rl_mine_luxury);

		if (!NetUtil.getNetworkState(getActivity())) {
			addresslist_no_order.setVisibility(View.VISIBLE);
			my_list.setVisibility(View.GONE);
			rl_mine_recharge.setVisibility(View.GONE);
			rl_mine_title.setVisibility(View.VISIBLE);
		}
		initview();
		addresslist_no_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addresslist_loading.setVisibility(View.VISIBLE);
				new Thread(new Runnable() {
					public void run() {
						try {
							if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
								geticard();
							}
							Thread.sleep(1000);
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									addresslist_loading
											.setVisibility(View.GONE);
								}
							});
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();

			}
		});
		EventBus.getDefault().register(this);
		return view;
	}

	public void initview() {
		saveUtils = new SaveUtils(getActivity());
		mineAdapter = new MineAdapter(coin);
		my_list.setAdapter(mineAdapter);
		if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
			user_tel_text.setText(saveUtils.getStrSP(KeepingData.PHONE));
		} else {
			user_tel_text.setText("立即登录");
			user_tel_text.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(getActivity(), LoginActivity.class));
				}
			});
		}
		my_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
						TCAgent.onEvent(getActivity(), "我的页面_优惠券");
						Intent mIntent = new Intent(getActivity(),
								CouponActivity.class);
						Bundle mBundle = new Bundle();
						mBundle.putSerializable("TYPE", UseConponType.CHECK);
						mIntent.putExtras(mBundle);
						startActivity(mIntent);
					} else {
						startActivity(new Intent(getActivity(),
								LoginActivity.class));
					}
					break;
				case 1:
					if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
						startActivity(new Intent(getActivity(),
								MyAddressActivity.class));
					} else {
						startActivity(new Intent(getActivity(),
								LoginActivity.class));
					}
					break;
				case 2:
					// 分享APP给好友
					SharePacketsDialog showShare = new SharePacketsDialog(
							getActivity(), R.style.customdialog_style,
							R.layout.share_packets_dialog, true);
					showShare.show();
					break;
				case 3:
					if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
						if (icard == null || icard.getRecard_sn() == null) {
							startActivity(new Intent(getActivity(),
									BindEntitycardActivity.class));
						}
					} else {
						startActivity(new Intent(getActivity(),
								LoginActivity.class));
					}
					break;
				case 4:
					TCAgent.onEvent(getActivity(), "我的_意见反馈");
					Intent intentIdea = new Intent();
					Bundle mBundles = new Bundle();
					mBundles.putInt("TYPE", WebPageType.FEEDBACK.getType());
					intentIdea.putExtras(mBundles);
					intentIdea.setClass(getActivity(), WebActivity.class);
					startActivity(intentIdea);
					break;
				case 5:
					startActivity(new Intent(getActivity(), MoreActivity.class));
					break;
				}
			}
		});
		if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
			getExtraIcard();
		}
	}

	private class MineAdapter extends BaseAdapter {
		String coinBak;

		public MineAdapter(String coin) {
			super();
			this.coinBak = coin;
		}

		@Override
		public int getCount() {
			return mineListTitles.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view;
			ViewHolder viHolder = null;
			if (convertView != null) {
				view = convertView;
				viHolder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getActivity(), R.layout.mine_list_item,
						null);
				viHolder = new ViewHolder();
				viHolder.iv_mine_item_left = (ImageView) view
						.findViewById(R.id.iv_mine_item_left);
				viHolder.mine_item_divide_line = (View) view
						.findViewById(R.id.mine_item_divide_line);
				viHolder.tv_mine_item_text = (TextView) view
						.findViewById(R.id.tv_mine_item_text);
				viHolder.mine_recharge_btn = (TextView) view
						.findViewById(R.id.mine_recharge_btn);
				viHolder.tv_mine_item_icard = (TextView) view
						.findViewById(R.id.tv_mine_item_icard);
				viHolder.tv_mine_item_icard_bak = (TextView) view
						.findViewById(R.id.tv_mine_item_icard_bak);
				viHolder.mine_item_short_line = (View) view
						.findViewById(R.id.mine_item_short_line);
				view.setTag(viHolder);
			}
			viHolder.iv_mine_item_left
					.setImageResource(mineListIcons[position]);
			viHolder.tv_mine_item_text.setText(mineListTitles[position]);
			switch (position) {
			case 3:
				viHolder.mine_recharge_btn.setVisibility(View.INVISIBLE);
				viHolder.mine_item_divide_line.setVisibility(View.VISIBLE);
				if (icard != null && icard.getRecard_sn() != null) {
					if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
						viHolder.tv_mine_item_icard.setText(icard
								.getRecard_sn() + "(已绑定)");
						viHolder.tv_mine_item_icard.setTextColor(getResources()
								.getColor(R.color.lowblack));
					}
				} else {
					viHolder.tv_mine_item_icard.setText("");
				}
				break;
			default:
				viHolder.mine_recharge_btn.setVisibility(View.INVISIBLE);
				viHolder.mine_item_divide_line.setVisibility(View.GONE);
				viHolder.tv_mine_item_icard.setText("");
				break;
			}
			if (position == 3 || position == 5) {
				viHolder.mine_item_short_line.setVisibility(View.INVISIBLE);
			} else {
				viHolder.mine_item_short_line.setVisibility(View.VISIBLE);
			}
			return view;
		}
	}

	class ViewHolder {
		public ImageView iv_mine_item_left;
		public TextView tv_mine_item_text;
		public TextView mine_recharge_btn;
		public TextView tv_mine_item_icard;
		public TextView tv_mine_item_icard_bak;
		public View mine_item_short_line;
		public View mine_item_divide_line;

	}

	private void geticard() {
		parms = new HashMap<String, String>();
		parms.clear();
		parms.put("user_id", saveUtils.getStrSP("user_id"));
		((MainActivity) getActivity()).getdate(parms, Constants.geticard,
				handler, GETICARDSUCCED, GETICARDFAILD, false, false, true);
	}

	// 获取奢侈品年卡信息
	private void getExtraIcard() {
		HashMap<String, String> parm = new HashMap<String, String>();
		parm.put("user_id", saveUtils.getStrSP(KeepingData.USER_ID));
		((MainActivity) getActivity()).getdate(parm, Constants.getextraaccount,
				handler, GETEXTRAICARDSUCCED, GETEXTRAICARDFAILD, false, true,
				false);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!NetUtil.getNetworkState(getActivity())) {
			addresslist_no_order.setVisibility(View.VISIBLE);
			my_list.setVisibility(View.GONE);
			rl_mine_recharge.setVisibility(View.GONE);
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (!NetUtil.getNetworkState(getActivity())) {
				addresslist_no_order.setVisibility(View.VISIBLE);
				my_list.setVisibility(View.GONE);
				rl_mine_recharge.setVisibility(View.GONE);
				rl_mine_title.setVisibility(View.VISIBLE);
			} else {
				my_list.setVisibility(View.VISIBLE);
				rl_mine_title.setVisibility(View.VISIBLE);
				rl_mine_recharge.setVisibility(View.VISIBLE);
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
									geticard();
								}
							}
						});
					}
				}, 100);
			}
		}
	}

	public void onEvent(OrderListAdapterEvent event) {
		switch (event.getText()) {
		case "getprice":
			geticard();
			break;
		case "DepositWXPaySucess":
		case "DepositALiPaySucess":
		case "DepositIcardPaySucess":
			geticard();
			break;
		case "Backmore":
			getId();
			break;
		default:
			break;
		}
	}

}
