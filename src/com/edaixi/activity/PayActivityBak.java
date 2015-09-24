package com.edaixi.activity;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
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
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.Icard;
import com.edaixi.modle.Trade_no;
import com.edaixi.util.Constants;
import com.edaixi.util.LogUtil;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.util.SaveUtils;
import com.edaixi.view.SingleView;
import com.edaixi.wechat.Util;
import com.edaixi.wechat.Wx_MD5;
import com.google.gson.Gson;
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
	private Button pay_btn;
	private TextView pay_order_value;
	private HashMap<String, String> parm;
	private AppConfig mAppConfig;
	private Handler pHandler;
	private String order_price;
	private CouponEntity coupon;
	private SaveUtils saveutils;
	private final int GETPAYORDERNUMSUCCED = 2;
	private final int GETPAYORDERNUMFAILD = 3;
	private int method = 1;
	private Icard icard;
	private TextView pay_coupon_value;
	private String order_id = "";
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
	private TextView pay_coupon_text;
	private String coupon_paid;
	private int WxZhiFu = 2;
	private int ZhiFuBaoZhiFu = 6;
	private int YuEZhiFu = 1;
	private int XianJinZhiFu = 3;
	private DecimalFormat df;
	com.edaixi.view.SingleView view_pay_type_wechat;
	com.edaixi.view.SingleView view_pay_type_alipay;
	com.edaixi.view.SingleView view_pay_type_recharge;
	com.edaixi.view.SingleView view_pay_type_cash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_paybak);
		init(this);
		initView();
		parm = new HashMap<String, String>();
		mAppConfig = AppConfig.getInstance();
		saveutils = new SaveUtils(this);
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
		order_price = getIntent().getExtras().getString("order_price");
		coupon_paid = getIntent().getExtras().getString("coupon_paid");
		order_id = getIntent().getExtras().getString("order_id");
		order_sn = getIntent().getExtras().getString("order_sn");
		if (coupon_paid != null) {
			yingfu_price = Double.valueOf(order_price)
					- Double.valueOf(coupon_paid);
			yingfu_price = Double.valueOf(df.format(yingfu_price));
			if (!coupon_paid.equals("0.0")) {
				pay_coupon_value.setText("-¥" + coupon_paid);
				pay_coupon_text.setVisibility(View.GONE);
				coupon_relayout.setClickable(false);
			}
			if (yingfu_price <= 0) {
				yingfu_price = 0.0;
				pay_btn.setText("确定");
				method = 1;
				view_pay_type_wechat.setVisibility(View.GONE);
				view_pay_type_alipay.setVisibility(View.GONE);
				view_pay_type_recharge.setVisibility(View.GONE);
				view_pay_type_cash.setVisibility(View.GONE);
			} else {
				pay_btn.setText("需支付:¥" + df.format(yingfu_price));
			}
		}

		exclusive_channels = getIntent().getExtras().getString(
				"exclusive_channels");
		if (exclusive_channels != null) {
			reInitPayType(exclusive_channels);
		}
		pay_order_value.setText("¥" + order_price);
		getIcard();
	}

	private void initView() {
		pHandler = new PayHandler();
		view_pay_type_wechat = (SingleView) findViewById(R.id.view_pay_type_wechat);
		view_pay_type_wechat.setPayTypeLogo(R.drawable.wechat);
		view_pay_type_wechat.setPayTitle("微信支付");
		view_pay_type_wechat.setOnClickListener(this);
		view_pay_type_alipay = (SingleView) findViewById(R.id.view_pay_type_alipay);
		view_pay_type_alipay.setPayTypeLogo(R.drawable.alipay);
		view_pay_type_alipay.setPayTitle("支付宝支付");
		view_pay_type_alipay.setOnClickListener(this);
		view_pay_type_alipay.setPayTypeChecked(true);
		view_pay_type_recharge = (SingleView) findViewById(R.id.view_pay_type_recharge);
		view_pay_type_recharge.setPayTypeLogo(R.drawable.balance);
		view_pay_type_recharge.setPayTitle("余额支付");
		view_pay_type_recharge.setOnClickListener(this);
		view_pay_type_cash = (SingleView) findViewById(R.id.view_pay_type_cash);
		view_pay_type_cash.setPayTypeLogo(R.drawable.money);
		view_pay_type_cash.setPayTitle("现金支付");
		view_pay_type_cash.setOnClickListener(this);
		pay_coupon_value = (TextView) findViewById(R.id.pay_coupon_value);
		pay_order_value = (TextView) findViewById(R.id.pay_order_value);
		findViewById(R.id.pay_back_btn).setOnClickListener(this);
		pay_btn = (Button) findViewById(R.id.pay_btn);
		coupon_relayout = (RelativeLayout) findViewById(R.id.coupon_relayout);
		coupon_relayout.setOnClickListener(this);
		pay_coupon_text = (TextView) findViewById(R.id.pay_coupon_text);
		pay_btn.setOnClickListener(this);

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
				yingfu_price = Double.valueOf(df.format(yingfu_price));
				pay_coupon_value.setText("-¥" + coupon.getCouponPrice());
				pay_coupon_text.setVisibility(View.GONE);
				if (Double.valueOf(order_price) <= (double) coupon
						.getCouponPrice()) {
					pay_btn.setText("确定");
					method = YuEZhiFu;
					view_pay_type_wechat.setVisibility(View.GONE);
					view_pay_type_alipay.setVisibility(View.GONE);
					view_pay_type_recharge.setVisibility(View.GONE);
					view_pay_type_cash.setVisibility(View.GONE);
				} else {
					pay_btn.setText("需支付¥:" + df.format(yingfu_price));
				}
			}
		}
	}

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
			view_pay_type_recharge.setPayTypeChecked(false);
			view_pay_type_cash.setPayTypeChecked(false);
			break;
		case R.id.view_pay_type_alipay:
			method = ZhiFuBaoZhiFu;
			view_pay_type_wechat.setPayTypeChecked(false);
			view_pay_type_alipay.setPayTypeChecked(true);
			view_pay_type_recharge.setPayTypeChecked(false);
			view_pay_type_cash.setPayTypeChecked(false);
			break;
		case R.id.view_pay_type_recharge:
			method = YuEZhiFu;
			view_pay_type_wechat.setPayTypeChecked(false);
			view_pay_type_alipay.setPayTypeChecked(false);
			view_pay_type_recharge.setPayTypeChecked(true);
			view_pay_type_cash.setPayTypeChecked(false);
			break;
		case R.id.view_pay_type_cash:
			method = XianJinZhiFu;
			view_pay_type_wechat.setPayTypeChecked(false);
			view_pay_type_alipay.setPayTypeChecked(false);
			view_pay_type_recharge.setPayTypeChecked(false);
			view_pay_type_cash.setPayTypeChecked(true);
			break;
		case R.id.pay_back_btn:
			onBackKeyDown();
			break;
		case R.id.pay_btn:
			getPayOrder(method);
			break;
		case R.id.coupon_relayout:
			Intent intent = new Intent(getContext(), CouponActivity.class);
			mBundle = new Bundle();
			mBundle.putSerializable("TYPE", UseConponType.USE);
			mBundle.putDouble("MONEY", Double.valueOf(order_price));
			mBundle.putString("Order_Id", order_id);
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
						} else {
							initPayType();
							view_pay_type_alipay.setPayTypeChecked(true);
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
		if (exclusive_channels == null || exclusive_channels.equals("")) {
			if (wxAppInstalled) {
				view_pay_type_wechat.setVisibility(View.VISIBLE);
			}
			view_pay_type_alipay.setVisibility(View.VISIBLE);
			view_pay_type_recharge.setVisibility(View.VISIBLE);
			view_pay_type_cash.setVisibility(View.VISIBLE);
		}
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
