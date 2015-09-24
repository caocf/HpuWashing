package com.edaixi.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edaixi.adapter.DeliveryListAdapter;
import com.edaixi.data.AppConfig;
import com.edaixi.modle.OrderDeliveryInfo;
import com.edaixi.modle.OrderListItemBean;
import com.edaixi.util.Constants;
import com.edaixi.util.LogUtil;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.util.ParseOrderList;
import com.edaixi.util.SaveUtils;
import com.edaixi.view.CancleOrderDialog;
import com.edaixi.view.CancleOrderDialog.CancleDialogButtonListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

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
	public static final int TRUEMESSAGECANCLEORDER = 12;
	public static final int FALSEMESSAGECANCLEORDER = 13;
	private OrderListItemBean orderListItembean;
	private HashMap<String, String> orderParams = new HashMap<String, String>();
	private HashMap<String, String> orderDeliveryParams = new HashMap<String, String>();
	public SaveUtils saveUtils;
	private ParseOrderList parseOrderList;

	@ViewInject(R.id.activity_orderdetail_back_btn)
	private ImageView activity_orderdetail_back_btn;
	@ViewInject(R.id.show_clothing_detail_btn)
	private Button show_clothing_detail_btn;
	@ViewInject(R.id.tv_cancle_order)
	private TextView tv_cancle_order;
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
	@ViewInject(R.id.fl_order_cancle)
	private FrameLayout fl_order_cancle;

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
			case 12:
				String removeResultSucess = (String) msg.obj;
				LogUtil.e("删除订单成功" + msg.obj);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					if (removeResultSucess != null
							&& jsonObject.getBoolean("ret")) {
						showdialog("订单已取消");

						new Handler().postDelayed(new Runnable() {
							public void run() {
								EventBus.getDefault().post(
										new OrderListAdapterEvent(
												"ShanChuDingDan"));
								finish();
							}
						}, 2500);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 13:
				String removeResultFail = (String) msg.obj;
				LogUtil.e("删除订单失败" + msg.obj);
				if (removeResultFail != null) {
					showdialog("删除订单失败，稍后重试");
				}
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
		EventBus.getDefault().register(this);

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
				TCAgent.onEvent(OrderDetialActivity.this, "物流电话拨出");
			}
		});
		lv_show_delivery.setFocusable(false);
		if (orderListItembean.getCan_be_canceled() != null
				&& orderListItembean.getCan_be_canceled().equals("true")) {
			fl_order_cancle.setVisibility(View.VISIBLE);
			tv_cancle_order.setVisibility(View.VISIBLE);
			tv_cancle_order.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					TCAgent.onEvent(OrderDetialActivity.this, "取消订单");
					if (isHasNet()) {
						CancleOrderDialog cuDialog = new CancleOrderDialog(
								OrderDetialActivity.this,
								R.style.customdialog_style,
								R.layout.cancle_order_dialog, orderListItembean);
						cuDialog.show();
						cuDialog.setYourListener(new CancleDialogButtonListener() {

							@Override
							public void isCancleOrder(boolean isCancle) {
								if (isCancle) {
									getCancleOrder();
								}
							}
						});
					} else {
						showdialog("网络不可用，请检查您的网络连接");
						EventBus.getDefault()
								.post(new OrderListAdapterEvent(
										"NoNetQuXiaoDingDan"));
					}
				}
			});
		} else {
			fl_order_cancle.setVisibility(View.GONE);
			tv_cancle_order.setVisibility(View.GONE);
		}
	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("OrderDetialActivity");
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("OrderDetialActivity");
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

	/**
	 * get order delivery information
	 */
	public void getCancleOrder() {
		orderDeliveryParams.clear();
		if (saveUtils.getStrSP("user_id") != null) {
			orderDeliveryParams.put("user_id", saveUtils.getStrSP("user_id"));
		}
		if (AppConfig.getInstance().getCancleOrderString() != null) {
			orderDeliveryParams.put("reason", AppConfig.getInstance()
					.getCancleOrderString());
		}
		orderDeliveryParams.put("order_id", orderListItembean.getOrder_id());
		this.postdate(orderDeliveryParams, Constants.getcancelorder, handler,
				TRUEMESSAGECANCLEORDER, FALSEMESSAGECANCLEORDER, true, false);
	}

	/**
	 * get order id from order item by event bus
	 */
	public void onEvent(OrderListAdapterEvent event) {
		switch (event.getText()) {
		default:
			break;
		}
	}
}
