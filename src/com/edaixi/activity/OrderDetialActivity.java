package com.edaixi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.edaixi.data.AppConfig;
import com.edaixi.modle.ClothingOrderInfo;
import com.edaixi.modle.HttpCommonBean;
import com.edaixi.modle.OrderDeliveryInfo;
import com.edaixi.modle.OrderInfo;
import com.edaixi.modle.OrderListItemBean;
import com.edaixi.util.Constants;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.util.ParseOrderList;
import com.edaixi.util.SaveUtils;
import com.edaixi.view.CancleOrderDialog;
import com.edaixi.view.CancleOrderDialog.CancleDialogButtonListener;
import com.google.gson.Gson;
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
public class OrderDetialActivity extends BaseActivity implements
		OnClickListener {

	protected static final String TAG = "OrderDetial.class";
	public static final int TUREMESSAGEORDER = 8;
	public static final int ERRORMESSAGEORDER = 9;
	public static final int TUREMESSAGECLOTHING = 20;
	public static final int ERRORMESSAGECLOTHING = 21;
	public static final int TUREMESSAGEDELIVERY = 10;
	public static final int ERRORMESSAGEDELIVERY = 11;
	public static final int TRUEMESSAGECANCLEORDER = 12;
	public static final int FALSEMESSAGECANCLEORDER = 13;
	private OrderListItemBean orderListItembean;
	private HashMap<String, String> orderParams = new HashMap<String, String>();
	private HashMap<String, String> orderDeliveryParams = new HashMap<String, String>();
	public SaveUtils saveUtils;
	private ParseOrderList parseOrderList;
	private ArrayList<ClothingOrderInfo> parseClothingDetail;
	private int showClothingFlag = 0;

	@ViewInject(R.id.rl_order_clothing_info)
	private RelativeLayout rl_order_clothing_info;
	@ViewInject(R.id.rl_order_operate)
	private RelativeLayout rl_order_operate;
	@ViewInject(R.id.rl_order_info)
	private RelativeLayout rl_order_info;
	@ViewInject(R.id.rl_order_status)
	private RelativeLayout rl_order_status;
	@ViewInject(R.id.rl_order_address_info)
	private RelativeLayout rl_order_address_info;
	@ViewInject(R.id.rl_order_pay_info)
	private RelativeLayout rl_order_pay_info;
	@ViewInject(R.id.tv_order_detail_status)
	private TextView tv_order_detail_status;
	@ViewInject(R.id.tv_show_delivery_new)
	private TextView tv_show_delivery_new;
	@ViewInject(R.id.tv_show_delivery_time)
	private TextView tv_show_delivery_time;
	@ViewInject(R.id.order_detail_clothing_count)
	private TextView order_detail_clothing_count;
	@ViewInject(R.id.tv_show_more_clothing)
	private TextView tv_show_more_clothing;
	@ViewInject(R.id.order_detail_pay_type)
	private TextView order_detail_pay_type;
	@ViewInject(R.id.tv_order_detail_fee)
	private TextView tv_order_detail_fee;
	@ViewInject(R.id.tv_order_detail_pay)
	private TextView tv_order_detail_pay;
	@ViewInject(R.id.tv_order_detail_user_name)
	private TextView tv_order_detail_user_name;
	@ViewInject(R.id.tv_order_detail_address_name)
	private TextView tv_order_detail_address_name;
	@ViewInject(R.id.tv_order_detail_ordersn)
	private TextView tv_order_detail_ordersn;
	@ViewInject(R.id.tv_order_detail_order_time)
	private TextView tv_order_detail_order_time;
	@ViewInject(R.id.tv_order_detail_order_type)
	private TextView tv_order_detail_order_type;
	@ViewInject(R.id.tv_order_detail_order_remarks_title)
	private TextView tv_order_detail_order_remarks_title;
	@ViewInject(R.id.tv_order_detail_order_remarks)
	private TextView tv_order_detail_order_remarks;
	@ViewInject(R.id.tv_order_detail_cuidan)
	private TextView tv_order_detail_cuidan;
	@ViewInject(R.id.tv_order_detail_cancle)
	private TextView tv_order_detail_cancle;
	@ViewInject(R.id.tv_order_detail_user_tel)
	private TextView tv_order_detail_user_tel;
	@ViewInject(R.id.tv_order_detail_cancle_tips)
	private TextView tv_order_detail_cancle_tips;
	@ViewInject(R.id.iv_show_more_clothing)
	private ImageView iv_show_more_clothing;
	@ViewInject(R.id.ll_show_more_clothing)
	private LinearLayout ll_show_more_clothing;
	@ViewInject(R.id.lv_order_detail_show_clothing)
	private com.edaixi.view.ListViewWithNoScrollbar lv_order_detail_show_clothing;
	private ClothingAdapter clothingAdapter;
	private ArrayList<ClothingOrderInfo> clothingData;
	private OrderInfo fromJsonOrder;
	private RotateAnimation animationGo;
	private RotateAnimation animationBack;

	@Override
	protected boolean onBackKeyDown() {
		return false;
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		private ArrayList<OrderDeliveryInfo> parseOrderDevlivery;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 8:
				Gson mGson = new Gson();
				HttpCommonBean fromJson = mGson.fromJson((String) msg.obj,
						HttpCommonBean.class);
				if (fromJson.isRet()) {
					fromJsonOrder = mGson.fromJson(fromJson.getData(),
							OrderInfo.class);
					if (fromJsonOrder != null) {
						// 支付信息
						if (fromJsonOrder.getPay_status() != null
								&& fromJsonOrder.getPay_status() == "1") {
							rl_order_pay_info.setVisibility(View.VISIBLE);
							order_detail_pay_type.setText(fromJsonOrder
									.getPay_type());
							tv_order_detail_fee.setText("订单￥"
									+ fromJsonOrder.getOrder_price() + "+运费￥"
									+ fromJsonOrder.getDelivery_fee() + "-优惠券￥"
									+ fromJsonOrder.getCoupon_paid());
							tv_order_detail_pay.setText("实付款：￥"
									+ fromJsonOrder.getYingfu());
						} else {
							rl_order_pay_info.setVisibility(View.GONE);
						}
						// 地址信息
						tv_order_detail_user_name.setText("姓名："
								+ fromJsonOrder.getOrder_username());
						tv_order_detail_user_tel.setText(fromJsonOrder
								.getOrder_tel());
						tv_order_detail_address_name.setText(fromJsonOrder
								.getAddress_qu());
						// 订单信息
						StringBuilder sBuilder = new StringBuilder(
								fromJsonOrder.getOrder_sn());
						tv_order_detail_ordersn
								.setText(sBuilder.insert(fromJsonOrder
										.getOrder_sn().length() - 6, "  "));
						tv_order_detail_order_time.setText(fromJsonOrder
								.getYuyue_qujian_time());
						tv_order_detail_order_type.setText(fromJsonOrder
								.getGood());
						if (fromJsonOrder.getRemark() != null
								&& fromJsonOrder.getRemark() != "") {
							tv_order_detail_order_remarks_title
									.setVisibility(View.VISIBLE);
							tv_order_detail_order_remarks.setText(fromJsonOrder
									.getRemark());
						} else {
							tv_order_detail_order_remarks_title
									.setVisibility(View.GONE);
							tv_order_detail_order_remarks
									.setVisibility(View.GONE);
						}
						tv_order_detail_status.setText(fromJsonOrder
								.getOrder_status_text());
						order_detail_clothing_count.setText(fromJsonOrder
								.getAmount_text());
					}

				} else {
					showdialog(fromJson.getError());
				}
				break;
			case 9:
				break;
			case 10:
				String orderDeliveryResultScuess = (String) msg.obj;
				parseOrderDevlivery = parseOrderList
						.parseOrderDevlivery(orderDeliveryResultScuess);
				if (parseOrderDevlivery != null
						&& parseOrderDevlivery.size() > 0) {
					// 设置最新的物流信息
					rl_order_status.setVisibility(View.VISIBLE);
					tv_show_delivery_new.setText(parseOrderDevlivery.get(0)
							.getText());
					tv_show_delivery_time.setText(parseOrderDevlivery.get(0)
							.getTime());
				} else {
					rl_order_status.setVisibility(View.GONE);
				}
				break;
			case 11:
				break;
			case 12:
				String removeResultSucess = (String) msg.obj;
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
				if (removeResultFail != null) {
					showdialog("删除订单失败，稍后重试");
				}
				break;
			case TUREMESSAGECLOTHING:
				parseClothingDetail = parseOrderList
						.parseClothingDetail((String) msg.obj);
				if (parseClothingDetail != null
						&& parseClothingDetail.size() > 0) {
					lv_order_detail_show_clothing.setVisibility(View.VISIBLE);
					rl_order_clothing_info.setVisibility(View.VISIBLE);
					if (parseClothingDetail.size() > 4) {
						clothingData = getClothingData(parseClothingDetail);
						clothingAdapter = new ClothingAdapter(clothingData);
						lv_order_detail_show_clothing
								.setAdapter(clothingAdapter);
						tv_show_more_clothing.setVisibility(View.VISIBLE);
						iv_show_more_clothing.setVisibility(View.VISIBLE);
						tv_show_more_clothing.setText("展开其他"
								+ (parseClothingDetail.size() - 4) + "件物品详情");
					} else {
						ClothingAdapter clothingAdapter = new ClothingAdapter(
								parseClothingDetail);
						lv_order_detail_show_clothing
								.setAdapter(clothingAdapter);
						tv_show_more_clothing.setVisibility(View.GONE);
						iv_show_more_clothing.setVisibility(View.GONE);
					}

				} else {
					rl_order_clothing_info.setVisibility(View.GONE);
				}
				break;
			case ERRORMESSAGECLOTHING:
				break;
			}
		}

	};

	private ArrayList<ClothingOrderInfo> getClothingData(
			ArrayList<ClothingOrderInfo> parseClothingDetail) {
		ArrayList<ClothingOrderInfo> parseClothingDetailBak = new ArrayList<ClothingOrderInfo>();
		for (int i = 0; i < 4; i++) {
			parseClothingDetailBak.add(i, parseClothingDetail.get(i));
		}
		return parseClothingDetailBak;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event, int voidID) {
		return super.onKeyDown(keyCode, event, voidID);
	}

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
		tv_show_more_clothing.setOnClickListener(this);
		rl_order_status.setOnClickListener(this);
		tv_order_detail_cancle.setOnClickListener(this);
		ll_show_more_clothing.setOnClickListener(this);
		Intent intent = this.getIntent();
		Bundle mbunBundle = intent.getExtras();
		String OrderFlagData = mbunBundle.getString("OrderFlagData");
		if (OrderFlagData != null && OrderFlagData.equals("COMPLETED")) {
			rl_order_operate.setVisibility(View.GONE);
		}
		orderListItembean = (OrderListItemBean) intent
				.getSerializableExtra("OrderListItembean");
		if (orderListItembean.getHas_clothes_detail().equals("true")) {
			lv_order_detail_show_clothing.setVisibility(View.VISIBLE);
		}

		getOrderInfo();
		getOrderDelivery();
		getClothingDetail();
		if (orderListItembean.getCan_be_canceled() != null
				&& orderListItembean.getCan_be_canceled().equals("true")) {
			tv_order_detail_cancle.setVisibility(View.VISIBLE);
			tv_order_detail_cancle.setOnClickListener(new OnClickListener() {

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
			tv_order_detail_cancle.setVisibility(View.GONE);
		}

		if (orderListItembean.getDelivery_status() != null) {

			switch (orderListItembean.getDelivery_status()) {
			case "9":
				tv_order_detail_cuidan.setVisibility(View.VISIBLE);
				if (orderListItembean.getCan_be_paid().equals("true")) {
					tv_order_detail_cuidan
							.setBackgroundResource(R.drawable.order_pay_bg);
					tv_order_detail_cuidan
							.setTextColor(OrderDetialActivity.this
									.getResources()
									.getColor(R.color.fukuanziti));
					tv_order_detail_cuidan.setText("付款");
					tv_order_detail_cuidan
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent();
									intent.setClass(OrderDetialActivity.this,
											PayActivityBak.class);
									Bundle bundle = new Bundle();
									bundle.putSerializable("PayOrderItem",
											orderListItembean);
									intent.putExtras(bundle);
									startActivity(intent);
								}
							});
				} else if (!orderListItembean.getOrder_price().equals("0.00")) {
					tv_order_detail_cuidan
							.setBackgroundResource(R.drawable.order_cuidan_bg);
					tv_order_detail_cuidan.setText("催单");
					tv_order_detail_cuidan
							.setTextColor(OrderDetialActivity.this
									.getResources()
									.getColor(R.color.cuidanziti));
					tv_order_detail_cuidan
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// 拨打取件员电话
									if (orderListItembean.getCourier_phone_qu()
											.length() != 0) {
										Uri uriQu = Uri.parse("tel:"
												+ orderListItembean
														.getCourier_phone_qu());
										Intent intentQu = new Intent(
												Intent.ACTION_DIAL, uriQu);
										startActivity(intentQu);
									}
								}
							});
				} else {
					tv_order_detail_cuidan.setText("催单");
					tv_order_detail_cuidan
							.setBackgroundResource(R.drawable.order_cuidan_bg);
					tv_order_detail_cuidan
							.setTextColor(OrderDetialActivity.this
									.getResources()
									.getColor(R.color.cuidanziti));
					tv_order_detail_cuidan
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// 拨打取件员电话
									if (orderListItembean.getCourier_phone_qu()
											.length() != 0) {
										Uri uriQu = Uri.parse("tel:"
												+ orderListItembean
														.getCourier_phone_qu());
										Intent intentQu = new Intent(
												Intent.ACTION_DIAL, uriQu);
										startActivity(intentQu);
									}
								}
							});
				}
				break;

			case "1":
			case "4":
			case "5":
			case "6":
			case "8":
				tv_order_detail_cuidan.setVisibility(View.VISIBLE);
				tv_order_detail_cuidan.setText("催单");
				tv_order_detail_cuidan
						.setBackgroundResource(R.drawable.order_cuidan_bg);
				tv_order_detail_cuidan.setTextColor(OrderDetialActivity.this
						.getResources().getColor(R.color.cuidanziti));
				tv_order_detail_cuidan
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// 拨打客服电话
								Uri uri = Uri.parse("tel:4008187171");
								Intent intent = new Intent(Intent.ACTION_DIAL,
										uri);
								startActivity(intent);
							}
						});
				break;
			case "2":
			case "7":
				tv_order_detail_cuidan.setVisibility(View.VISIBLE);
				tv_order_detail_cuidan.setText("催单");
				tv_order_detail_cuidan
						.setBackgroundResource(R.drawable.order_cuidan_bg);
				tv_order_detail_cuidan.setTextColor(OrderDetialActivity.this
						.getResources().getColor(R.color.cuidanziti));
				tv_order_detail_cuidan
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// 拨打送件员电话
								if (orderListItembean.getCourier_phone_song()
										.length() != 0) {
									Uri uriSong = Uri.parse("tel:"
											+ orderListItembean
													.getCourier_phone_song());
									Intent intentSong = new Intent(
											Intent.ACTION_DIAL, uriSong);
									startActivity(intentSong);
								}
							}
						});
				break;
			case "3":
				tv_order_detail_cuidan.setVisibility(View.VISIBLE);
				if (orderListItembean.getCan_be_commented().equals("0")) {
					tv_order_detail_cancle_tips.setVisibility(View.VISIBLE);
					tv_order_detail_cuidan
							.setBackgroundResource(R.drawable.order_cuidan_bg);
					tv_order_detail_cuidan
							.setTextColor(OrderDetialActivity.this
									.getResources().getColor(
											R.color.pingjiaziti));
					tv_order_detail_cuidan.setText("评价");
					tv_order_detail_cuidan
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent = new Intent();
									intent.setClass(OrderDetialActivity.this,
											AppraiseActivity.class);
									Bundle bundle = new Bundle();
									bundle.putSerializable("orderItem",
											orderListItembean);
									intent.putExtras(bundle);
									startActivity(intent);
									finish();
								}
							});
				}
				break;
			case "-1":
			case "-2":
			case "11":
			case "0":
				tv_order_detail_cuidan.setVisibility(View.INVISIBLE);
			default:
				break;
			}

		}

		animationGo = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animationGo.setDuration(500);
		animationGo.setRepeatCount(0);
		animationGo.setFillAfter(true);

		animationBack = new RotateAnimation(180f, 360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animationBack.setDuration(500);
		animationBack.setRepeatCount(0);
		animationBack.setFillAfter(true);
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
		orderParams.clear();
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
		orderDeliveryParams.clear();
		if (saveUtils.getStrSP("user_id") != null) {
			orderDeliveryParams.put("user_id", saveUtils.getStrSP("user_id"));
		}
		orderDeliveryParams.put("order_id", orderListItembean.getOrder_id());
		getdate(orderDeliveryParams, Constants.getorderdeliverystatuslist,
				handler, TUREMESSAGEDELIVERY, ERRORMESSAGEDELIVERY, true,
				false, false);
	}

	/**
	 * cancle order
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
				TRUEMESSAGECANCLEORDER, FALSEMESSAGECANCLEORDER, false, false);
	}

	/**
	 * get clothing detail from sevice
	 */
	public void getClothingDetail() {
		orderDeliveryParams.clear();
		if (saveUtils.getStrSP("user_id") != null) {
			orderDeliveryParams.put("user_id", saveUtils.getStrSP("user_id"));
		}
		orderDeliveryParams.put("order_id", orderListItembean.getOrder_id());
		this.getdate(orderDeliveryParams, Constants.getorderclothing, handler,
				TUREMESSAGECLOTHING, ERRORMESSAGECLOTHING, false, false, false);
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

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_order_detail_cancle:
			if (isHasNet()
					&& orderListItembean.getCan_be_canceled().equals("true")) {
				CancleOrderDialog cuDialog = new CancleOrderDialog(
						OrderDetialActivity.this, R.style.customdialog_style,
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
			}
			break;
		case R.id.rl_order_status:
			// 进入物流信息详情
			Intent intent = new Intent();
			intent.setClass(OrderDetialActivity.this,
					DeliveryInfoActivity.class);
			Bundle bundle = new Bundle();
			if (fromJsonOrder != null) {
				bundle.putSerializable("OrderInfo", fromJsonOrder);
			}
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.tv_show_more_clothing:
		case R.id.ll_show_more_clothing:
		case R.id.iv_show_more_clothing:
			// 展开更多衣物详情
			showClothingFlag++;
			if ((showClothingFlag % 2) != 0) {
				iv_show_more_clothing.startAnimation(animationGo);
				tv_show_more_clothing.setText("收起其他"
						+ (parseClothingDetail.size() - 4) + "件物品详情");
				if (clothingAdapter != null && parseClothingDetail != null) {
					clothingAdapter.parseClothingDetail = parseClothingDetail;
					clothingAdapter.notifyDataSetChanged();
				}
			} else {
				iv_show_more_clothing.startAnimation(animationBack);
				tv_show_more_clothing.setText("展开其他"
						+ (parseClothingDetail.size() - 4) + "件物品详情");
				clothingAdapter.parseClothingDetail = clothingData;
				clothingAdapter.notifyDataSetChanged();
			}
			break;

		default:
			break;
		}
	}

	class ClothingAdapter extends BaseAdapter {

		ArrayList<ClothingOrderInfo> parseClothingDetail;

		public ClothingAdapter(ArrayList<ClothingOrderInfo> parseClothingDetail) {
			super();
			this.parseClothingDetail = parseClothingDetail;
		}

		@Override
		public int getCount() {
			return parseClothingDetail.size();
		}

		@Override
		public Object getItem(int position) {
			return parseClothingDetail.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder viHolder = null;
			if (convertView != null) {
				view = convertView;
				viHolder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getActivity(), R.layout.clothing_list_item,
						null);
				viHolder = new ViewHolder();
				viHolder.tv_clothing_item_name = (TextView) view
						.findViewById(R.id.tv_clothing_item_name);
				viHolder.tv_clothing_item_color = (TextView) view
						.findViewById(R.id.tv_clothing_item_color);
				viHolder.tv_clothing_item_flaw = (TextView) view
						.findViewById(R.id.tv_clothing_item_flaw);
				view.setTag(viHolder);
			}
			viHolder.tv_clothing_item_name.setText(parseClothingDetail.get(
					position).getCloth_title());
			viHolder.tv_clothing_item_color.setText(parseClothingDetail.get(
					position).getColor());
			viHolder.tv_clothing_item_flaw.setText(parseClothingDetail.get(
					position).getCloth_condition());
			return view;
		}

	}

	class ViewHolder {
		public TextView tv_clothing_item_name;
		public TextView tv_clothing_item_color;
		public TextView tv_clothing_item_flaw;
	}
}
