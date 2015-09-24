package com.edaixi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.edaixi.adapter.DeliveryListAdapter;
import com.edaixi.modle.OrderDeliveryInfo;
import com.edaixi.modle.OrderInfo;
import com.edaixi.util.Constants;
import com.edaixi.util.LogUtil;
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
public class DeliveryInfoActivity extends BaseActivity {

	@ViewInject(R.id.iv_delivery_time)
	private ImageView iv_delivery_time;
	@ViewInject(R.id.tv_delivery_ordersn_text)
	private TextView tv_delivery_ordersn_text;
	@ViewInject(R.id.tv_delivery_orderstatus_text)
	private TextView tv_delivery_orderstatus_text;
	@ViewInject(R.id.tv_show_jiafang_tips)
	private TextView tv_show_jiafang_tips;
	@ViewInject(R.id.lv_show_order_delivery)
	private com.edaixi.view.ListViewWithNoScrollbar lv_show_order_delivery;

	SaveUtils saveUtils;
	private HashMap<String, String> orderDeliveryParams = new HashMap<String, String>();
	private ParseOrderList parseOrderList;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		private ArrayList<OrderDeliveryInfo> parseOrderDevlivery;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 30:
				String orderDeliveryResultScuess = (String) msg.obj;
				parseOrderDevlivery = parseOrderList
						.parseOrderDevlivery(orderDeliveryResultScuess);
				if (parseOrderDevlivery != null
						&& parseOrderDevlivery.size() > 0) {
					lv_show_order_delivery.setVisibility(View.VISIBLE);
					DeliveryListAdapter deliveryListAdapter = new DeliveryListAdapter(
							parseOrderDevlivery, getContext());
					lv_show_order_delivery.setAdapter(deliveryListAdapter);
				} else {
					lv_show_order_delivery.setVisibility(View.GONE);
				}
				break;
			case 31:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_deliveryinfo);
		ViewUtils.inject(this);
		init(this);

		saveUtils = new SaveUtils(DeliveryInfoActivity.this);
		parseOrderList = new ParseOrderList();

		OrderInfo serializableExtra = (OrderInfo) getIntent()
				.getSerializableExtra("OrderInfo");
		if (serializableExtra != null) {
			tv_delivery_ordersn_text.setText(serializableExtra.getOrder_sn());
			tv_delivery_orderstatus_text.setText(serializableExtra
					.getOrder_status_text());

			switch (serializableExtra.getCategory_id()) {
			case "1":
				iv_delivery_time
						.setImageResource(R.drawable.delivery_clothing_time);
				tv_show_jiafang_tips.setVisibility(View.VISIBLE);
				break;
			case "2":
				iv_delivery_time
						.setImageResource(R.drawable.delivery_shoes_time);
				tv_show_jiafang_tips.setVisibility(View.GONE);
				break;
			case "3":
				iv_delivery_time
						.setImageResource(R.drawable.delivery_jiafang_time);
				tv_show_jiafang_tips.setVisibility(View.GONE);
				break;
			case "4":
				iv_delivery_time
						.setImageResource(R.drawable.delivery_gaoduanyiwu_time);
				tv_show_jiafang_tips.setVisibility(View.GONE);
				break;
			case "5":
				iv_delivery_time
						.setImageResource(R.drawable.delivery_shecipin_time);
				tv_show_jiafang_tips.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		}
		if (serializableExtra != null) {
			getOrderDelivery(serializableExtra.getOrder_id());
		}
	}

	/**
	 * get order delivery information
	 */
	public void getOrderDelivery(String order_id) {
		if (saveUtils.getStrSP("user_id") != null) {
			orderDeliveryParams.put("user_id", saveUtils.getStrSP("user_id"));
		}
		orderDeliveryParams.put("order_id", order_id);
		this.getdate(orderDeliveryParams, Constants.getorderdeliverystatuslist,
				handler, 30, 31, true, true, false);
	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("DeliveryInfoActivity");
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("DeliveryInfoActivity");
		MobclickAgent.onPause(this);
	}

	public void goBack(View view) {
		finish();
	}

	@Override
	protected boolean onBackKeyDown() {
		return false;
	}

}
