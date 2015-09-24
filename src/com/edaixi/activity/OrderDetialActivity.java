package com.edaixi.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.edaixi.adapter.DeliveryListAdapter;
import com.edaixi.modle.OrderDeliveryInfo;
import com.edaixi.modle.OrderListItemBean;
import com.edaixi.util.Constants;
import com.edaixi.util.ParseOrderList;
import com.edaixi.util.SaveUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

/**
 * order detail activity....
 * 
 * @author wei-spring
 */
public class OrderDetialActivity extends BaseActivity {

	protected static final String TAG = "OrderDetial.class";
	public static final int TUREMESSAGEORDER = 8;
	public static final int ERRORMESSAGEORDER = 9;
	public static final int TUREMESSAGEDELIVERY = 10;
	public static final int ERRORMESSAGEDELIVERY = 11;
	private OrderListItemBean orderListItembean;
	@SuppressWarnings("unused")
	private OrderListItemBean orderItemInfo;
	private HashMap<String, String> orderParams = new HashMap<String, String>();
	private HashMap<String, String> orderDeliveryParams = new HashMap<String, String>();
	public SaveUtils saveUtils;
	private ParseOrderList parseOrderList;

	@ViewInject(R.id.activity_orderdetail_back_btn)
	private ImageView activity_orderdetail_back_btn;
	@ViewInject(R.id.show_clothing_detail_btn)
	private Button show_clothing_detail_btn;
	@ViewInject(R.id.order_detail_id_text)
	private TextView order_detail_id_text;
	@ViewInject(R.id.order_id_title)
	private TextView order_id_title;
	@ViewInject(R.id.lv_show_delivery)
	private ListView lv_show_delivery;
	@ViewInject(R.id.order_statue_title)
	private TextView order_statue_title;
	@ViewInject(R.id.order_detail_statue_text)
	private TextView order_detail_statue_text;
	@ViewInject(R.id.order_time_title)
	private TextView order_time_title;
	@ViewInject(R.id.order_detail_time_text)
	private TextView order_detail_time_text;
	@ViewInject(R.id.tv_my_info)
	private TextView tv_my_info;
	@ViewInject(R.id.order_detail_tv_my_name)
	private TextView order_detail_tv_my_name;
	@ViewInject(R.id.order_detail_tv_my_tel)
	private TextView order_detail_tv_my_tel;
	@ViewInject(R.id.order_detail_tv_address)
	private TextView order_detail_tv_address;
	@ViewInject(R.id.tv_wuliuixnix)
	private TextView tv_wuliuixnix;
	@ViewInject(R.id.order_line_1)
	private View order_line_1;
	@ViewInject(R.id.order_line_2)
	private View order_line_2;
	@ViewInject(R.id.line_below_wuliuxinxi)
	private View line_below_wuliuxinxi;
	@ViewInject(R.id.line_above_myinfo)
	private View line_above_myinfo;
	@ViewInject(R.id.iv_line_0)
	private ImageView iv_line_0;
	@ViewInject(R.id.iv_line_1)
	private ImageView iv_line_1;

	@Override
	protected boolean onBackKeyDown() {
		return false;
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		private OrderListItemBean parseOrderInfo;
		private ArrayList<OrderDeliveryInfo> parseOrderDevlivery;
		private DeliveryListAdapter deliveryListAdapter;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 8:
				String orderResultSucess = (String) msg.obj;
				parseOrderInfo = parseOrderList
						.parseOrderInfo(orderResultSucess);
				if (parseOrderInfo != null) {
					order_line_1.setVisibility(View.VISIBLE);
					order_id_title.setVisibility(View.VISIBLE);
					order_statue_title.setVisibility(View.VISIBLE);
					order_line_2.setVisibility(View.VISIBLE);
					order_time_title.setVisibility(View.VISIBLE);
					tv_my_info.setVisibility(View.VISIBLE);
					line_above_myinfo.setVisibility(View.VISIBLE);
					iv_line_0.setVisibility(View.VISIBLE);
					order_detail_id_text.setText(parseOrderInfo.getOrder_sn());
					order_detail_statue_text.setText(parseOrderInfo
							.getOrder_status_text());
					order_detail_time_text.setText(parseOrderInfo
							.getWashing_date()
							+ " "
							+ parseOrderInfo.getWashing_time());
					order_detail_tv_my_name.setText(parseOrderInfo
							.getOrder_username());
					order_detail_tv_my_tel.setText(parseOrderInfo
							.getOrder_tel());
					order_detail_tv_address.setText(parseOrderInfo
							.getAddress_qu());
				}
				break;
			case 9:
				String orderResultFail = (String) msg.obj;
				Log.d(TAG, "order-error-info" + orderResultFail);
				break;
			case 10:
				line_below_wuliuxinxi.setVisibility(View.VISIBLE);
				iv_line_1.setVisibility(View.VISIBLE);
				String orderDeliveryResultScuess = (String) msg.obj;
				parseOrderDevlivery = parseOrderList
						.parseOrderDevlivery(orderDeliveryResultScuess);
				if (parseOrderDevlivery != null
						&& parseOrderDevlivery.size() > 0) {
					tv_wuliuixnix.setVisibility(View.VISIBLE);
					if (deliveryListAdapter == null) {
						deliveryListAdapter = new DeliveryListAdapter(
								parseOrderDevlivery, getContext());
						lv_show_delivery.setAdapter(deliveryListAdapter);
					}
				}
				break;
			case 11:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_orderdetail);
		ViewUtils.inject(this);

		init(this);
		saveUtils = new SaveUtils(getApplicationContext());
		parseOrderList = new ParseOrderList();
		Intent intent = this.getIntent();
		orderListItembean = (OrderListItemBean) intent
				.getSerializableExtra("OrderListItembean");
		if (orderListItembean.getHas_clothes_detail().equals("true")) {
			show_clothing_detail_btn.setVisibility(View.VISIBLE);
		}
		getOrderInfo();
		getOrderDelivery();
		show_clothing_detail_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getContext(), ClothingDetialActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("order_sn", orderListItembean.getOrder_sn());
				bundle.putString("order_id", orderListItembean.getOrder_id());
				intent.putExtras(bundle);
				getContext().startActivity(intent);
			}
		});
		lv_show_delivery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TCAgent.onEvent(OrderDetialActivity.this,"物流电话拨出");
			}
		});
		lv_show_delivery.setFocusable(false);
	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("OrderDetialActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("OrderDetialActivity"); // 保证 onPageEnd 在onPause
														// 之前调用,因为 onPause
														// 中会保存信息
		MobclickAgent.onPause(this);
	}

	public void goBack(View view) {
		finish();
	}

	/**
	 * judge is show clothing detail button
	 * 
	 * @return
	 */
	public boolean isShowClothingDetail() {

		return true;
	}

	/**
	 * get order all information
	 */
	public void getOrderInfo() {

		if (saveUtils.getStrSP("user_id") != null) {
			orderParams.put("user_id", saveUtils.getStrSP("user_id"));
		}
		orderParams.put("order_id", orderListItembean.getOrder_id());
		this.getdate(orderParams, Constants.getorder, handler,
				TUREMESSAGEORDER, ERRORMESSAGEORDER, false, true, false);
	}

	/**
	 * get order delivery information
	 */
	public void getOrderDelivery() {
		if (saveUtils.getStrSP("user_id") != null) {
			orderDeliveryParams.put("user_id", saveUtils.getStrSP("user_id"));
		}
		orderDeliveryParams.put("order_id", orderListItembean.getOrder_id());
		this.getdate(orderDeliveryParams, Constants.getorderdeliverystatuslist,
				handler, TUREMESSAGEDELIVERY, ERRORMESSAGEDELIVERY, true, true,
				false);
	}
}
