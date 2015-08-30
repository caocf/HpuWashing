package com.edaixi.activity;

import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.edaixi.Enum.CouponEntity;
import com.edaixi.Enum.UseConponType;
import com.edaixi.alipay.Alipay_pay;
import com.edaixi.baidupay.BaiDuPayUtil;
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.dataset.CouponsDataSet;
import com.edaixi.modle.BaiDuPayOrderInfo;
import com.edaixi.modle.ClothingOrderInfo;
import com.edaixi.modle.CouponBean;
import com.edaixi.modle.ExtraCardBean;
import com.edaixi.modle.GetCouponsBean;
import com.edaixi.modle.HttpCommonBean;
import com.edaixi.modle.Icard;
import com.edaixi.modle.OrderInfo;
import com.edaixi.modle.OrderListItemBean;
import com.edaixi.modle.Trade_no;
import com.edaixi.util.Constants;
import com.edaixi.util.LogUtil;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.util.SaveUtils;
import com.edaixi.view.SingleView;
import com.edaixi.wechat.Util;
import com.edaixi.wechat.Wx_MD5;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import de.greenrobot.event.EventBus;

public class PayActivityBak extends BaseActivity implements OnClickListener {

	private static final int GETICARDSUCCED = 4;
	private static final int GETICARDFAILD = 5;
	private static final int GETEXTRAICARDSUCCED = 9;
	private static final int GETEXTRAICARDFAILD = 10;
	private static final int GETCLOTHINGSUCCED = 7;
	private static final int GETCLOTHINGFAILD = 8;
	private static final int GETORDERCOUPONSUCESS = 20;
	private static final int GETORDERCOUPONFAIL = 21;
	private Button bt_pay_btn;
	private HashMap<String, String> parm;
	private Handler pHandler;
	private String order_price;
	private CouponEntity coupon;
	private SaveUtils saveutils;
	private final int GETPAYORDERNUMSUCCED = 2;
	private final int GETPAYORDERNUMFAILD = 3;
	private int method = 1;
	private Icard icard;
	private TextView tv_pay_ordersn;
	private TextView tv_pay_clothes_count;
	private TextView tv_pay_order_value;
	private TextView tv_pay_coupon_value;
	private TextView tv_pay_coupon_text;
	private String order_id = "";
	private String category_id = "";
	private String order_sn = "";
	private IWXAPI api;
	private RelativeLayout coupon_relayout;
	private String coupon_id = "";
	private boolean isPaySupported;
	private Bundle mBundle;
	private String wx_out_trade_no;
	private String exclusive_channels;
	private Double yingfu_price;
	private Trade_no trade_no;
	private String coupon_paid;
	private int WxZhiFu = 2;
	private int ZhiFuBaoZhiFu = 6;
	private int YuEZhiFu = 1;
	private int XianJinZhiFu = 3;
	private int ExtraIcardZhiFu = 10;
	private int BaiDuZhiFu = 11;
	private DecimalFormat df;
	com.edaixi.view.SingleView view_pay_type_wechat;
	com.edaixi.view.SingleView view_pay_type_alipay;
	com.edaixi.view.SingleView view_pay_type_baidupay;
	com.edaixi.view.SingleView view_pay_type_recharge;
	com.edaixi.view.SingleView view_pay_type_cash;
	com.edaixi.view.SingleView view_pay_type_luxury;
	private ArrayList<ClothingOrderInfo> parseClothingDetail;
	private CouponsDataSet mListDataSet = null;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GETCLOTHINGSUCCED:
				Gson mGson = new Gson();
				HttpCommonBean fromJsons = mGson.fromJson((String) msg.obj,
						HttpCommonBean.class);
				if (fromJsons.isRet()) {
					OrderInfo fromJsonOrder = mGson.fromJson(
							fromJsons.getData(), OrderInfo.class);
					if (fromJsonOrder != null) {
					}
					tv_pay_clothes_count
							.setText(fromJsonOrder.getAmount_text());
				}
				break;
			case GETCLOTHINGFAILD:
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
					if (mExtraCardBeans.size() > 0
							&& (category_id.equals("4") || category_id
									.equals("5"))) {
						view_pay_type_luxury.setVisibility(View.VISIBLE);
						view_pay_type_luxury.setPayRechargeVisable(true);
						view_pay_type_luxury.setPayRechargeText("有效期至:"
								+ mExtraCardBeans.get(0).getAvailable_at());
					} else {
						view_pay_type_luxury.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case GETEXTRAICARDFAILD:
				break;
			case GETORDERCOUPONSUCESS: {
				Type contentTypeBak = new TypeToken<GetCouponsBean>() {
				}.getType();
				GetCouponsBean mInfo = null;
				try {
					mInfo = getGson()
							.fromJson((String) msg.obj, contentTypeBak);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				if (mInfo != null && mInfo.isRet()
						&& TextUtils.isEmpty(mInfo.getError())) {
					contentType = new TypeToken<ArrayList<CouponBean>>() {
					}.getType();
					List<CouponBean> mCouponArray = null;
					try {
						mCouponArray = getGson().fromJson(mInfo.getData(),
								contentType);
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					if (mCouponArray != null) {
						for (int i = 0, size = mCouponArray.size(); i < size; i++) {
							mListDataSet.addBean(new CouponEntity(mCouponArray
									.get(i)));
						}
					}
					CouponsDataSet mNewListDataSet = judgeCouponData(mListDataSet);
					if (mNewListDataSet.size() > 0) {
						coupon_relayout.setClickable(true);
						tv_pay_coupon_value.setText("有"
								+ mNewListDataSet.size() + "张可用优惠券  ");
					} else {
						tv_pay_coupon_value.setText("无可用优惠券");
						coupon_relayout.setClickable(false);
					}
				} else {
					showdialog("数据错误");
				}
				break;
			}
			case GETORDERCOUPONFAIL:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_paybak);
		init(this);
		initView();
		AppConfig.getInstance().setCanUseRecharge(true);
		parm = new HashMap<String, String>();
		saveutils = new SaveUtils(this);
		mListDataSet = new CouponsDataSet();
		df = new DecimalFormat("0.00");
		EventBus.getDefault().register(this);
		// -----------微信支付-----------------------------------------
		IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
		msgApi.registerApp(Constants.WXAPP_ID);
		api = WXAPIFactory.createWXAPI(this, Constants.WXAPP_ID, true);
		api.registerApp(Constants.WXAPP_ID);
		wxAppInstalled = msgApi.isWXAppInstalled();
		if (!wxAppInstalled) {
			view_pay_type_wechat.setVisibility(View.GONE);
		} else {
			view_pay_type_wechat.setVisibility(View.VISIBLE);
		}
		// -----------微信支付-----------------------------------------
		DecimalFormat df = new DecimalFormat("0.00");
		if (getIntent() != null) {
			orderItem = (OrderListItemBean) getIntent().getSerializableExtra(
					"PayOrderItem");
			order_price = orderItem.getOrder_price();
			coupon_paid = orderItem.getCoupon_paid();
			order_id = orderItem.getOrder_id();
			category_id = orderItem.getCategory_id();
			order_sn = orderItem.getOrder_sn();
			exclusive_channels = orderItem.getExclusive_channels();
			if (exclusive_channels != null) {
				reInitPayType(exclusive_channels);
			}
			StringBuilder sBuilder = new StringBuilder(order_sn);
			tv_pay_ordersn.setText("订单编号： "
					+ sBuilder.insert(sBuilder.toString().length() - 6, "  "));
			tv_pay_order_value.setText(order_price);
			if (orderItem.getCoupon_sn().length() > 1) {
				tv_pay_coupon_value.setText("优惠券" + orderItem.getCoupon_paid()
						+ "元");
				coupon_relayout.setClickable(false);
			} else {
				coupon_relayout.setClickable(true);
				getOrderCoupon(order_id);
			}
		}
		getClothingDetail();
		getExtraIcard();
		if (coupon_paid != null) {
			yingfu_price = Double.valueOf(order_price)
					- Double.valueOf(coupon_paid);
			yingfu_price = Double.valueOf(df.format(yingfu_price));
			if (!coupon_paid.equals("0.0")) {
				tv_pay_coupon_value.setText("优惠券" + coupon_paid + "元");
				tv_pay_coupon_text.setVisibility(View.GONE);
				coupon_relayout.setClickable(false);
			}
			if (yingfu_price <= 0) {
				yingfu_price = 0.0;
				bt_pay_btn.setText("确定");
				method = 1;
				view_pay_type_wechat.setVisibility(View.GONE);
				view_pay_type_alipay.setVisibility(View.GONE);
				view_pay_type_recharge.setVisibility(View.GONE);
				view_pay_type_luxury.setVisibility(View.GONE);
				view_pay_type_baidupay.setVisibility(View.GONE);
				view_pay_type_cash.setVisibility(View.GONE);
			} else {
				bt_pay_btn.setText("需支付:¥" + df.format(yingfu_price));
			}
		}
		getIcard();
	}

	private void initView() {
		pHandler = new PayHandler();
		view_pay_type_wechat = (SingleView) findViewById(R.id.view_pay_type_wechat);
		view_pay_type_wechat.setPayTypeLogo(R.drawable.wechat_pay_icon);
		view_pay_type_wechat.setPayTitle("微信支付");
		view_pay_type_wechat.setOnClickListener(this);
		view_pay_type_alipay = (SingleView) findViewById(R.id.view_pay_type_alipay);
		view_pay_type_alipay.setPayTypeLogo(R.drawable.ali_pay_icon);
		view_pay_type_alipay.setPayTitle("支付宝支付");
		view_pay_type_alipay.setOnClickListener(this);
		view_pay_type_alipay.setPayTypeChecked(true);
		view_pay_type_baidupay = (SingleView) findViewById(R.id.view_pay_type_baidupay);
		view_pay_type_baidupay.setPayTypeLogo(R.drawable.baidu_pay_icon);
		view_pay_type_baidupay.setPayTitle("百度支付");
		view_pay_type_baidupay.setOnClickListener(this);
		view_pay_type_recharge = (SingleView) findViewById(R.id.view_pay_type_recharge);
		view_pay_type_recharge.setPayTypeLogo(R.drawable.balance_pay_icon);
		view_pay_type_recharge.setPayTitle("余额支付");
		view_pay_type_recharge.setOnClickListener(this);
		view_pay_type_cash = (SingleView) findViewById(R.id.view_pay_type_cash);
		view_pay_type_cash.setPayTypeLogo(R.drawable.cash_pay_icon);
		view_pay_type_cash.setPayTitle("现金支付");
		view_pay_type_cash.setOnClickListener(this);
		view_pay_type_luxury = (SingleView) findViewById(R.id.view_pay_type_luxury);
		view_pay_type_luxury.setPayTypeLogo(R.drawable.luxury_pay_icon);
		view_pay_type_luxury.setPayTitle("奢护年卡");
		view_pay_type_luxury.setOnClickListener(this);
		tv_pay_ordersn = (TextView) findViewById(R.id.tv_pay_ordersn);
		tv_pay_clothes_count = (TextView) findViewById(R.id.tv_pay_clothes_count);
		tv_pay_coupon_value = (TextView) findViewById(R.id.tv_pay_coupon_value);
		tv_pay_order_value = (TextView) findViewById(R.id.tv_pay_order_value);
		findViewById(R.id.pay_back_btn).setOnClickListener(this);
		bt_pay_btn = (Button) findViewById(R.id.bt_pay_btn);
		coupon_relayout = (RelativeLayout) findViewById(R.id.coupon_relayout);
		coupon_relayout.setOnClickListener(this);
		tv_pay_coupon_text = (TextView) findViewById(R.id.tv_pay_coupon_text);
		bt_pay_btn.setOnClickListener(this);

	}

	// 处理优惠券数据，重新排列优惠券顺序和是否可用
	public CouponsDataSet judgeCouponData(CouponsDataSet mListDataSet) {
		CouponsDataSet availableCouponsDataSet = new CouponsDataSet();
		for (int i = 0; i < mListDataSet.size(); i++) {
			mListDataSet.getIndexBean(i).setValid(Double.valueOf(order_price));
			if ((mListDataSet.getIndexBean(i).isValid())
					&& ((mListDataSet.getIndexBean(i).getCategory_id()
							.equals(category_id)) || mListDataSet
							.getIndexBean(i).getCategory_id().equals("0"))) {
				availableCouponsDataSet.addBean(mListDataSet.getIndexBean(i));
			}
		}
		return availableCouponsDataSet;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == Constants.ADSREQUEST && resultCode == RESULT_OK) {
			if (intent != null) {
				coupon = (CouponEntity) intent.getExtras().getSerializable(
						"DATA");
				coupon_id = String.valueOf(coupon.getId());
				exclusive_channels = coupon.getExclusive_channels();
				if (exclusive_channels != null) {
					reInitPayType(exclusive_channels);
				} else {
					reInitPayType("");
				}
				yingfu_price = Double.valueOf(order_price)
						- Double.valueOf(coupon.getCouponPrice());
				tv_pay_coupon_value.setText("优惠券" + coupon.getCouponPrice()
						+ "元");
				tv_pay_coupon_text.setVisibility(View.GONE);
				getIcard();
				if (Double.valueOf(order_price) <= (double) coupon
						.getCouponPrice()) {
					bt_pay_btn.setText("确定");
					method = YuEZhiFu;
					view_pay_type_wechat.setVisibility(View.GONE);
					view_pay_type_alipay.setVisibility(View.GONE);
					view_pay_type_baidupay.setVisibility(View.GONE);
					view_pay_type_luxury.setVisibility(View.GONE);
					view_pay_type_recharge.setVisibility(View.GONE);
					view_pay_type_cash.setVisibility(View.GONE);
				} else {
					bt_pay_btn.setText("需支付¥:" + df.format(yingfu_price));
				}
			}
		}
	}

	// 获取奢侈品年卡信息
	private void getExtraIcard() {
		parm.clear();
		parm.put("user_id", saveutils.getStrSP(KeepingData.USER_ID));
		getdate(parm, Constants.getextraaccount, handler, GETEXTRAICARDSUCCED,
				GETEXTRAICARDFAILD, false, true, false);
	}

	// 获取可用的优惠券
	private void getOrderCoupon(String order_IdString) {
		parm.clear();
		parm.put("user_id", saveutils.getStrSP("user_id"));
		parm.put("order_id", order_IdString);
		getdate(parm, Constants.getcoupons, handler, GETORDERCOUPONSUCESS,
				GETORDERCOUPONFAIL, false, false, false);
	}

	// 获取会员卡信息
	private void getIcard() {
		parm.clear();
		parm.put("user_id", saveutils.getStrSP(KeepingData.USER_ID));
		getdate(parm, Constants.geticard, pHandler, GETICARDSUCCED,
				GETICARDFAILD, false, true, false);
	}

	private void getPayOrder(int method) {
		parm.clear();
		parm.put("user_id", saveutils.getStrSP("user_id"));
		parm.put("order_id", order_id);
		if (method > 0) {
			parm.put("paytype", String.valueOf(method));
		} else {
			return;
		}
		if (!TextUtils.isEmpty(coupon_id)) {
			parm.put("coupon_id", coupon_id);
		}
		postdate(parm, Constants.getpayorder2, pHandler, GETPAYORDERNUMSUCCED,
				GETPAYORDERNUMFAILD, false, false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_pay_type_wechat:
			method = WxZhiFu;
			view_pay_type_wechat.setPayTypeChecked(true);
			view_pay_type_alipay.setPayTypeChecked(false);
			view_pay_type_baidupay.setPayTypeChecked(false);
			view_pay_type_luxury.setPayTypeChecked(false);
			view_pay_type_recharge.setPayTypeChecked(false);
			view_pay_type_cash.setPayTypeChecked(false);
			bt_pay_btn.setText("需支付¥:" + df.format(yingfu_price));
			bt_pay_btn.setBackgroundResource(R.drawable.order_pay_btn);
			bt_pay_btn.setClickable(true);
			bt_pay_btn.setFocusable(true);
			break;
		case R.id.view_pay_type_alipay:
			method = ZhiFuBaoZhiFu;
			view_pay_type_wechat.setPayTypeChecked(false);
			view_pay_type_alipay.setPayTypeChecked(true);
			view_pay_type_baidupay.setPayTypeChecked(false);
			view_pay_type_luxury.setPayTypeChecked(false);
			view_pay_type_recharge.setPayTypeChecked(false);
			view_pay_type_cash.setPayTypeChecked(false);
			bt_pay_btn.setText("需支付¥:" + df.format(yingfu_price));
			bt_pay_btn.setBackgroundResource(R.drawable.order_pay_btn);
			bt_pay_btn.setClickable(true);
			bt_pay_btn.setFocusable(true);
			break;
		case R.id.view_pay_type_baidupay:
			method = BaiDuZhiFu;
			view_pay_type_wechat.setPayTypeChecked(false);
			view_pay_type_alipay.setPayTypeChecked(false);
			view_pay_type_baidupay.setPayTypeChecked(true);
			view_pay_type_luxury.setPayTypeChecked(false);
			view_pay_type_recharge.setPayTypeChecked(false);
			view_pay_type_cash.setPayTypeChecked(false);
			bt_pay_btn.setText("需支付¥:" + df.format(yingfu_price));
			bt_pay_btn.setBackgroundResource(R.drawable.order_pay_btn);
			bt_pay_btn.setClickable(true);
			bt_pay_btn.setFocusable(true);
			break;
		case R.id.view_pay_type_luxury:
			method = ExtraIcardZhiFu;
			view_pay_type_wechat.setPayTypeChecked(false);
			view_pay_type_alipay.setPayTypeChecked(false);
			view_pay_type_baidupay.setPayTypeChecked(false);
			view_pay_type_luxury.setPayTypeChecked(true);
			view_pay_type_recharge.setPayTypeChecked(false);
			view_pay_type_cash.setPayTypeChecked(false);
			bt_pay_btn.setText("需支付¥:" + df.format(yingfu_price));
			bt_pay_btn.setBackgroundResource(R.drawable.order_pay_btn);
			bt_pay_btn.setClickable(true);
			bt_pay_btn.setFocusable(true);
			break;
		case R.id.view_pay_type_recharge:
			method = YuEZhiFu;
			view_pay_type_wechat.setPayTypeChecked(false);
			view_pay_type_alipay.setPayTypeChecked(false);
			view_pay_type_baidupay.setPayTypeChecked(false);
			view_pay_type_luxury.setPayTypeChecked(false);
			view_pay_type_recharge.setPayTypeChecked(true);
			view_pay_type_cash.setPayTypeChecked(false);
			if (AppConfig.getInstance().isCanUseRecharge()) {
				bt_pay_btn.setText("确认余额支付 " + yingfu_price);
				bt_pay_btn.setBackgroundResource(R.drawable.order_pay_btn);
				bt_pay_btn.setClickable(true);
				bt_pay_btn.setFocusable(true);
			} else {
				bt_pay_btn.setText("余额不足 ");
				bt_pay_btn.setClickable(false);
				bt_pay_btn.setFocusable(false);
				bt_pay_btn
						.setBackgroundResource(R.drawable.order_pay_btn_default);
			}
			break;
		case R.id.view_pay_type_cash:
			method = XianJinZhiFu;
			view_pay_type_wechat.setPayTypeChecked(false);
			view_pay_type_alipay.setPayTypeChecked(false);
			view_pay_type_baidupay.setPayTypeChecked(false);
			view_pay_type_luxury.setPayTypeChecked(false);
			view_pay_type_recharge.setPayTypeChecked(false);
			view_pay_type_cash.setPayTypeChecked(true);
			bt_pay_btn.setText("需支付¥:" + df.format(yingfu_price));
			bt_pay_btn.setBackgroundResource(R.drawable.order_pay_btn);
			bt_pay_btn.setClickable(true);
			bt_pay_btn.setFocusable(true);
			break;
		case R.id.pay_back_btn:
			onBackKeyDown();
			break;
		case R.id.bt_pay_btn:
			getPayOrder(method);
			break;
		case R.id.coupon_relayout:
			Intent intent = new Intent(getContext(), CouponActivity.class);
			mBundle = new Bundle();
			mBundle.putSerializable("TYPE", UseConponType.USE);
			mBundle.putDouble("MONEY", Double.valueOf(order_price));
			mBundle.putString("Order_Id", order_id);
			mBundle.putString("Category_Id", category_id);
			mBundle.putString("Order_Price", order_price);
			mBundle.putString("Pay_Coupon", "Pay_Coupon");
			intent.putExtras(mBundle);
			startActivityForResult(intent, Constants.ADSREQUEST);
			break;
		default:
			break;
		}
	}

	@Override
	protected boolean onBackKeyDown() {
		EventBus.getDefault().post(new OrderListAdapterEvent("Payinterrupt"));
		AppConfig.getInstance().setCouponId(0);
		finish(0, 0);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("PayActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("PayActivity");
		MobclickAgent.onPause(this);
	}

	class PayHandler extends Handler {
		private JSONObject jsonObject;

		public PayHandler() {
			super();
		}

		public PayHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				if ((String) msg.obj == null) {
					return;
				}
				jsonObject = new JSONObject(msg.obj.toString());
				if (jsonObject.getBoolean("ret")) {
					String data = jsonObject.getString("data");
					Gson gson = new Gson();
					trade_no = gson.fromJson(data, Trade_no.class);
				} else {
					showdialog("付款失败:" + jsonObject.getString("error"));
					return;
				}
				switch (msg.what) {
				case GETPAYORDERNUMSUCCED:
					switch (method) {
					case 2:
						wx_out_trade_no = trade_no.getTrade_no();
						AppConfig appConfig = AppConfig.getInstance();
						appConfig.setOrderWxPay(true);
						if (isSupportWXPay()) {
							TalkingDataAppCpa.onOrderPaySucc(
									saveutils.getStrSP(KeepingData.PHONE),
									wx_out_trade_no,
									(int) (yingfu_price * 100), "CNY", "WxPay");
							new GetAccessTokenTask().execute();
						} else {
							showdialog("你的微信版本过低，不支持微信支付，请升级最新版本.");
						}
						break;
					case 6:
						if (method == ZhiFuBaoZhiFu) {
							TalkingDataAppCpa
									.onOrderPaySucc(saveutils
											.getStrSP(KeepingData.PHONE),
											trade_no.getTrade_no(),
											(int) (yingfu_price * 100), "CNY",
											"AliPay");
							Alipay_pay alipay_pay = new Alipay_pay(
									PayActivityBak.this,
									String.valueOf(yingfu_price),
									trade_no.getTrade_no(), true);
							alipay_pay.pay();
							AppConfig.getInstance().setOrderZhifubaoPay(true);
						}
						break;
					case 10:
						// 奢侈品年卡支付
						if (jsonObject.getBoolean("ret")) {
							EventBus.getDefault().post(
									new OrderListAdapterEvent(
											"ExtraIcardPaySucess"));
							finish(0, 0);
						} else {
							showdialog(jsonObject.getString("error"));
						}
						break;
					case 11:
						// 百度支付
						if (method == BaiDuZhiFu) {
							LogUtil.e("开始进入百度支付");
							BaiDuPayOrderInfo baiDuPayOrderInfo = new BaiDuPayOrderInfo();
							baiDuPayOrderInfo.setOrder_no(trade_no
									.getTrade_no());
							Double d_s = Double.valueOf(yingfu_price * 100);
							int price = d_s.intValue();
							baiDuPayOrderInfo.setUnit_amount("" + price);
							BaiDuPayUtil baiDuPayUtil = new BaiDuPayUtil(
									PayActivityBak.this, baiDuPayOrderInfo);
							baiDuPayUtil.pay();
						}
						break;
					case 1:
						if (jsonObject.getBoolean("ret")) {
							TalkingDataAppCpa
									.onOrderPaySucc(saveutils
											.getStrSP(KeepingData.PHONE),
											trade_no.getTrade_no(),
											(int) (yingfu_price * 100), "CNY",
											"YuEPay");
							EventBus.getDefault().post(
									new OrderListAdapterEvent("YuEPaySucess"));
							finish(0, 0);
						} else {
							showdialog(jsonObject.getString("error"));
						}
						break;
					case 3:
						if (jsonObject.getBoolean("ret")) {
							TalkingDataAppCpa.onOrderPaySucc(
									saveutils.getStrSP(KeepingData.PHONE),
									trade_no.getTrade_no(),
									(int) (yingfu_price * 100), "CNY",
									"XianJinPay");
							EventBus.getDefault().post(
									new OrderListAdapterEvent(
											"XianJinPaySucess"));
							finish(0, 0);
						} else {
							showdialog(jsonObject.getString("error"));
						}
						break;
					default:
						break;
					}
					break;
				case GETPAYORDERNUMFAILD:
					break;
				case GETICARDSUCCED:
					JSONObject object = new JSONObject((String) (msg.obj));
					String icarddata = object.getString("data");
					icard = new Gson().fromJson(icarddata, Icard.class);
					if (icard != null && icard.getCoin() != null) {
						DecimalFormat df = new DecimalFormat("0.00");
						double d1 = Double.parseDouble(icard.getCoin());
						view_pay_type_recharge.setPayRechargeVisable(true);
						view_pay_type_recharge.setPayRechargeText("当前余额:"
								+ df.format(d1) + "元");
						if (Double.valueOf(icard.getCoin()) >= Double
								.valueOf(yingfu_price)) {
							initPayType();
							view_pay_type_recharge.setPayTypeChecked(true);
							method = YuEZhiFu;
							AppConfig.getInstance().setCanUseRecharge(true);
						} else {
							initPayType();
							AppConfig.getInstance().setCanUseRecharge(false);
							view_pay_type_alipay.setPayTypeChecked(true);
							view_pay_type_recharge.setPayRechargeTips(true);

							method = ZhiFuBaoZhiFu;
						}
					}
					break;
				case GETICARDFAILD:
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/*** 初始化所有支付类型 **/
	public void initPayType() {
		view_pay_type_wechat.setPayTypeChecked(false);
		view_pay_type_alipay.setPayTypeChecked(false);
		view_pay_type_recharge.setPayTypeChecked(false);
		view_pay_type_cash.setPayTypeChecked(false);
	}

	/*** 根据优惠券类型重新初始化所有支付类型 **/
	public void reInitPayType(String exclusive_channels) {
		view_pay_type_wechat.setVisibility(View.GONE);
		view_pay_type_alipay.setVisibility(View.GONE);
		view_pay_type_recharge.setVisibility(View.GONE);
		view_pay_type_cash.setVisibility(View.GONE);
		view_pay_type_baidupay.setVisibility(View.GONE);
		if (exclusive_channels.contains("2")) {
			if (wxAppInstalled) {
				view_pay_type_wechat.setVisibility(View.VISIBLE);
			}
		}
		if (exclusive_channels.contains("6")) {
			view_pay_type_alipay.setVisibility(View.VISIBLE);
		}
		if (exclusive_channels.contains("1")) {
			view_pay_type_recharge.setVisibility(View.VISIBLE);
		}
		if (exclusive_channels.contains("3")) {
			view_pay_type_cash.setVisibility(View.VISIBLE);
		}
		if (exclusive_channels.contains("11")) {
			view_pay_type_baidupay.setVisibility(View.VISIBLE);
		}
		if (exclusive_channels == null || exclusive_channels.equals("")) {
			if (wxAppInstalled) {
				view_pay_type_wechat.setVisibility(View.VISIBLE);
			}
			view_pay_type_alipay.setVisibility(View.VISIBLE);
			view_pay_type_recharge.setVisibility(View.VISIBLE);
			view_pay_type_cash.setVisibility(View.VISIBLE);
			view_pay_type_baidupay.setVisibility(View.VISIBLE);
		}
	}

	public void getClothingDetail() {
		parm.clear();
		parm.put("user_id", saveutils.getStrSP("user_id"));
		parm.put("order_id", order_id);
		this.getdate(parm, Constants.getorder, handler, GETCLOTHINGSUCCED,
				GETCLOTHINGFAILD, false, false, false);
	}

	// -----------微信支付-----------------------------------------
	private static enum LocalRetCode {
		ERR_OK, ERR_HTTP, ERR_JSON, ERR_OTHER
	}

	public class GetAccessTokenTask extends
			AsyncTask<Void, Void, GetAccessTokenResult> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onPostExecute(GetAccessTokenResult result) {

			if (result.localRetCode == LocalRetCode.ERR_OK) {
				GetPrepayIdTask getPrepayId = new GetPrepayIdTask(
						result.accessToken);
				getPrepayId.execute();
			} else {
				Toast.makeText(PayActivityBak.this, "获取token失败",
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected GetAccessTokenResult doInBackground(Void... params) {
			GetAccessTokenResult result = new GetAccessTokenResult();
			String url = String
					.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
							Constants.WXAPP_ID, Constants.WXAPP_SECRET);
			byte[] buf = Util.httpGet(url);
			if (buf == null || buf.length == 0) {
				result.localRetCode = LocalRetCode.ERR_HTTP;
				return result;
			}
			String content = new String(buf);
			result.parseFrom(content);
			return result;
		}
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, GetPrepayIdResult> {
		private String accessToken;

		public GetPrepayIdTask(String accessToken) {
			this.accessToken = accessToken;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onPostExecute(GetPrepayIdResult result) {

			if (result.localRetCode == LocalRetCode.ERR_OK) {
				sendPayReq(result);
			} else {
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected GetPrepayIdResult doInBackground(Void... params) {

			String url = String.format(
					"https://api.weixin.qq.com/pay/genprepay?access_token=%s",
					accessToken);
			String entity = genProductArgs();
			GetPrepayIdResult result = new GetPrepayIdResult();
			byte[] buf = Util.httpPost(url, entity);
			if (buf == null || buf.length == 0) {
				result.localRetCode = LocalRetCode.ERR_HTTP;
				return result;
			}
			String content = new String(buf);
			result.parseFrom(content);
			return result;
		}
	}

	private static class GetAccessTokenResult {
		public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
		public String accessToken;
		public int expiresIn;
		public int errCode;
		public String errMsg;

		public void parseFrom(String content) {

			if (content == null || content.length() <= 0) {
				localRetCode = LocalRetCode.ERR_JSON;
				return;
			}

			try {
				JSONObject json = new JSONObject(content);
				if (json.has("access_token")) { // success case
					accessToken = json.getString("access_token");
					expiresIn = json.getInt("expires_in");
					localRetCode = LocalRetCode.ERR_OK;
				} else {
					errCode = json.getInt("errcode");
					errMsg = json.getString("errmsg");
					localRetCode = LocalRetCode.ERR_JSON;
				}

			} catch (Exception e) {
				localRetCode = LocalRetCode.ERR_JSON;
			}
		}
	}

	private static class GetPrepayIdResult {

		public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
		public String prepayId;
		public int errCode;
		public String errMsg;

		public void parseFrom(String content) {

			if (content == null || content.length() <= 0) {
				localRetCode = LocalRetCode.ERR_JSON;
				return;
			}

			try {
				JSONObject json = new JSONObject(content);
				if (json.has("prepayid")) {
					prepayId = json.getString("prepayid");
					localRetCode = LocalRetCode.ERR_OK;
				} else {
					localRetCode = LocalRetCode.ERR_JSON;
				}

				errCode = json.getInt("errcode");
				errMsg = json.getString("errmsg");

			} catch (Exception e) {
				localRetCode = LocalRetCode.ERR_JSON;
			}
		}
	}

	public boolean isSupportWXPay() {
		isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		return isPaySupported;
	}

	private String nonceStr, packageValue;

	private void sendPayReq(GetPrepayIdResult result) {
		PayReq req = new PayReq();
		req.appId = Constants.WXAPP_ID;
		req.partnerId = Constants.PARTNER_ID;
		req.prepayId = result.prepayId;
		req.nonceStr = nonceStr;
		req.timeStamp = String.valueOf(timeStamp);
		req.packageValue = "Sign=" + packageValue;

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("appkey", Constants.PaySignKey));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = genSign(signParams);
		api.sendReq(req);
	}

	private String genSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (; i < params.size() - 1; i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append(params.get(i).getName());
		sb.append('=');
		sb.append(params.get(i).getValue());

		String sha1 = Util.sha1(sb.toString());
		return sha1;
	}

	private String getTraceId() {
		return order_id + genTimeStamp();
	}

	private String genNonceStr() {
		Random random = new Random();
		return Wx_MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private long timeStamp;
	private boolean wxAppInstalled;
	private OrderListItemBean orderItem;

	private String genPackage(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.PARTNER_KEY);

		String packageSign = Wx_MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		return URLEncodedUtils.format(params, "utf-8") + "&sign=" + packageSign;
	}

	private String genProductArgs() {
		JSONObject json = new JSONObject();
		try {
			json.put("appid", Constants.WXAPP_ID);
			String traceId = getTraceId();
			json.put("traceid", traceId);
			nonceStr = genNonceStr();
			json.put("noncestr", nonceStr);

			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("bank_type", "WX"));
			packageParams.add(new BasicNameValuePair("body", "e袋洗订单"));
			packageParams.add(new BasicNameValuePair("fee_type", "1"));
			packageParams.add(new BasicNameValuePair("input_charset", "UTF-8"));
			packageParams.add(new BasicNameValuePair("notify_url",
					Constants.WECHAT_NOTIFY_URL));
			packageParams.add(new BasicNameValuePair("out_trade_no",
					wx_out_trade_no));
			packageParams.add(new BasicNameValuePair("partner",
					Constants.PARTNER_ID));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					getUserIp()));
			packageParams.add(new BasicNameValuePair("total_fee",
					(int) (yingfu_price * 100) + ""));
			packageValue = genPackage(packageParams);

			json.put("package", packageValue);
			timeStamp = genTimeStamp();
			json.put("timestamp", timeStamp);

			List<NameValuePair> signParams = new LinkedList<NameValuePair>();
			signParams.add(new BasicNameValuePair("appid", Constants.WXAPP_ID));
			signParams.add(new BasicNameValuePair("appkey",
					Constants.PaySignKey));
			signParams.add(new BasicNameValuePair("noncestr", nonceStr));
			signParams.add(new BasicNameValuePair("package", packageValue));
			signParams.add(new BasicNameValuePair("timestamp", String
					.valueOf(timeStamp)));
			signParams.add(new BasicNameValuePair("traceid", traceId));
			json.put("app_signature", genSign(signParams));
			json.put("sign_method", "sha1");
		} catch (Exception e) {
			return null;
		}
		return json.toString();
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String getUserIp() throws SocketException {
		for (Enumeration<NetworkInterface> en = NetworkInterface
				.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
					.hasMoreElements();) {
				InetAddress inetAddress = enumIpAddr.nextElement();
				if (!inetAddress.isLoopbackAddress()
						&& (inetAddress instanceof Inet4Address)) {
					return inetAddress.getHostAddress().toString();
				}
			}
		}
		return "null";
	}

	public void onEvent(OrderListAdapterEvent event) {

		switch (event.getText()) {
		case "WXPaySucess":
		case "ZhiFuBaoPaySucess":
			finish(0, 0);
			break;
		}
	}
	// -----------微信支付-----------------------------------------

}
