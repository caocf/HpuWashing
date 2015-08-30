package com.edaixi.activity;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
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

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edaixi.alipay.Alipay_pay;
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.Icard;
import com.edaixi.modle.RechargeSettingInfo;
import com.edaixi.modle.Trade_no;
import com.edaixi.util.Constants;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.util.ParseRechargeSetting;
import com.edaixi.util.SaveUtils;
import com.edaixi.wechat.Util;
import com.edaixi.wechat.Wx_MD5;
import com.google.gson.Gson;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class DepositActivity extends BaseActivity implements OnClickListener {

	private TextView yuee_text;
	private EditText chongzhi_edit;
	private Button rechage_btn;
	private RadioGroup radio_group;
	private Button rechage_commit_btn;
	private HashMap<String, String> parm;
	private final int RECHAERGESUCCED = 0;
	private final int RECHAERGEFAILD = 1;
	private final int ICRECHAERGESUCCED = 2;
	private final int ICRECHAERGEFAILD = 3;
	private final int GETRECHARGESUCCED = 200;
	private final int GETRECHARGEFAILED = 300;
	private RadioButton radio1_btn;
	private RadioButton radio2_btn;
	private RadioButton radio1_btn_money;
	private RadioButton radio2_btn_money;
	private RadioButton radio3_btn_money;
	private EditText money_charge;
	private RadioButton radio_btn4;
	private RelativeLayout wechat_layout;
	private RelativeLayout alipay_layout;
	private int method = 2;
	private String rechage_cash;
	private Trade_no trade_no;
	private SaveUtils saveUtils;
	private ImageView deposit_back_btn;
	private static final int GETICARDFAILD = 4;
	private static final int GETICARDSUCCED = 5;
	private Icard icard;
	private IWXAPI api;
	private String moneyString;
	private String rechargeAdsUrl = "http://7xi5cf.com1.z0.glb.clouddn.com/recharge_banner.png";
	private ParseRechargeSetting parseRechargeSeting;
	private ArrayList<RechargeSettingInfo> parseRechargeSettingRes;
	private ImageView iv_recharge_ad;
	private Bitmap bitmap;
	ProgressDialog progressDialog;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		private JSONObject object;
		private String msgstr = "";

		@Override
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case RECHAERGESUCCED:
					object = new JSONObject((String) msg.obj);
					if (object.getBoolean("ret")) {
						msgstr = "充值成功";
						geticard();
						chongzhi_edit.setText("");
					} else {
						msgstr = object.getString("error");
					}
					EventBus.getDefault().post(
							new OrderListAdapterEvent("DepositIcardPaySucess"));
					showdialog(msgstr);
					break;
				case RECHAERGEFAILD:
					msgstr = "充值失败";
					showdialog(msgstr);
					break;
				case ICRECHAERGESUCCED:
					object = new JSONObject((String) (msg.obj));
					String data = object.getString("data");
					Gson gson = new Gson();
					trade_no = gson.fromJson(data, Trade_no.class);
					if (method == 6) {
						Alipay_pay alipay_pay = new Alipay_pay(
								DepositActivity.this, rechage_cash,
								trade_no.getTrade_no(), false);
						alipay_pay.pay();
					}
					if (method == 2) {
						AppConfig appConfig = AppConfig.getInstance();
						appConfig.setOrderWxPay(false);
						progressDialog = new ProgressDialog(
								DepositActivity.this);
						if (isSupportWXPay()) {
							new GetAccessTokenTask().execute();
						} else {
							showdialog("你的微信版本过低，不支持微信支付，请升级最新版本.");
						}
					}
					break;
				case ICRECHAERGEFAILD:
					showdialog("充值失败");
					break;
				case GETICARDFAILD:

					break;
				case GETICARDSUCCED:
					object = new JSONObject((String) (msg.obj));
					String icarddata = object.getString("data");
					icard = new Gson().fromJson(icarddata, Icard.class);
					if (icard != null && icard.getCoin() != null) {
						DecimalFormat df = new DecimalFormat("0.00");
						double d1 = Double.parseDouble(icard.getCoin());
						yuee_text.setText("账户余额:" + df.format(d1) + "元");
					}
					break;
				case GETRECHARGESUCCED:
					parseRechargeSettingRes = parseRechargeSeting
							.parseRechargeSettingInfos(msg.obj.toString());
					if (parseRechargeSettingRes.size() > 0) {
						rechage_cash = parseRechargeSettingRes.get(0).getMin();
						SpannableString ss_1 = new SpannableString(
								new StringBuffer()
										.append("充")
										.append(parseRechargeSettingRes.get(0)
												.getMin())
										.append("返")
										.append(parseRechargeSettingRes.get(0)
												.getMoney_give()));
						ss_1.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
								parseRechargeSettingRes.get(0).getMin()
										.length() + 1,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						ss_1.setSpan(
								new ForegroundColorSpan(Color
										.parseColor("#ffc600")),
								parseRechargeSettingRes.get(0).getMin()
										.length() + 1, parseRechargeSettingRes
										.get(0).getMin().length()
										+ parseRechargeSettingRes.get(0)
												.getMoney_give().length() + 2,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						radio1_btn_money.setText(ss_1);
						SpannableString ss_2 = new SpannableString(
								new StringBuffer()
										.append("充")
										.append(parseRechargeSettingRes.get(1)
												.getMin())
										.append("返")
										.append(parseRechargeSettingRes.get(1)
												.getMoney_give()));
						ss_2.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
								parseRechargeSettingRes.get(1).getMin()
										.length() + 1,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						ss_2.setSpan(
								new ForegroundColorSpan(Color
										.parseColor("#ffc600")),
								parseRechargeSettingRes.get(1).getMin()
										.length() + 1, parseRechargeSettingRes
										.get(1).getMin().length()
										+ parseRechargeSettingRes.get(1)
												.getMoney_give().length() + 2,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						radio2_btn_money.setText(ss_2);
						SpannableString ss_3 = new SpannableString(
								new StringBuffer()
										.append("充")
										.append(parseRechargeSettingRes.get(2)
												.getMin())
										.append("返")
										.append(parseRechargeSettingRes.get(2)
												.getMoney_give()));
						ss_3.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
								parseRechargeSettingRes.get(2).getMin()
										.length() + 1,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						ss_3.setSpan(
								new ForegroundColorSpan(Color
										.parseColor("#ffc600")),
								parseRechargeSettingRes.get(2).getMin()
										.length() + 1, parseRechargeSettingRes
										.get(2).getMin().length()
										+ parseRechargeSettingRes.get(2)
												.getMoney_give().length() + 2,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						radio3_btn_money.setText(ss_3);
					}
					break;
				case GETRECHARGEFAILED:
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_deposit);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		init(this);
		saveUtils = new SaveUtils(getContext());
		parm = new HashMap<String, String>();
		radio1_btn_money = (RadioButton) findViewById(R.id.radio_btn1);
		radio1_btn_money.setOnClickListener(this);
		radio2_btn_money = (RadioButton) findViewById(R.id.radio_btn2);
		radio2_btn_money.setOnClickListener(this);
		radio3_btn_money = (RadioButton) findViewById(R.id.radio_btn3);
		radio3_btn_money.setOnClickListener(this);
		radio1_btn = (RadioButton) findViewById(R.id.radio1_btn);
		radio1_btn.setOnClickListener(this);
		radio2_btn = (RadioButton) findViewById(R.id.radio2_btn);
		radio2_btn.setOnClickListener(this);
		getrecharge_settings();
		geticard();
		api = WXAPIFactory.createWXAPI(this, Constants.WXAPP_ID, true);
		api.registerApp(Constants.WXAPP_ID);
		EventBus.getDefault().register(this);
		parseRechargeSeting = new ParseRechargeSetting();
		parseRechargeSettingRes = new ArrayList<RechargeSettingInfo>();
		iv_recharge_ad = (ImageView) findViewById(R.id.iv_recharge_ad);
		new LoadUrlImage().execute(rechargeAdsUrl);
		initView();
		AppConfig.getInstance().setCanRecharge(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		DepositActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				geticard();
			}
		});
		if (!TextUtils.isEmpty(moneyString)) {
			money_charge.requestFocus();
			visibleInputmethod(money_charge);
			radio_btn4.setChecked(true);
			radio_btn4.setVisibility(View.GONE);
			money_charge.setVisibility(View.VISIBLE);
			money_charge.setText(moneyString);
		}
		MobclickAgent.onPageStart("DepositActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("DepositActivity");
		MobclickAgent.onPause(this);
	}

	private void geticard() {
		parm.clear();
		parm.put("user_id", saveUtils.getStrSP("user_id"));
		getdate(parm, Constants.geticard, handler, GETICARDSUCCED,
				GETICARDFAILD, false, true, true);
	}

	@SuppressLint("HandlerLeak")
	private void initView() {
		yuee_text = (TextView) findViewById(R.id.yuee_text);
		deposit_back_btn = (ImageView) findViewById(R.id.deposit_back_btn);
		deposit_back_btn.setOnClickListener(this);
		chongzhi_edit = (EditText) findViewById(R.id.chongzhi_edit);
		rechage_btn = (Button) findViewById(R.id.rechage_btn);
		rechage_btn.setOnClickListener(this);
		wechat_layout = (RelativeLayout) findViewById(R.id.wechat_layout);
		wechat_layout.setOnClickListener(this);
		alipay_layout = (RelativeLayout) findViewById(R.id.alipay_layout);
		alipay_layout.setOnClickListener(this);
		radio_group = (RadioGroup) findViewById(R.id.radio_group);
		rechage_commit_btn = (Button) findViewById(R.id.rechage_commit_btn);
		rechage_commit_btn.setOnClickListener(this);
		money_charge = (EditText) findViewById(R.id.money_charge);
		radio_btn4 = (RadioButton) findViewById(R.id.radio_btn4);
		radio_btn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				radio_btn4.setVisibility(View.GONE);
				money_charge.setVisibility(View.VISIBLE);
				money_charge.setText("");
			}
		});
		radio_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_btn4:
					radio_btn4.setVisibility(View.GONE);
					money_charge.setVisibility(View.VISIBLE);
					money_charge.setText("");
					money_charge.requestFocus();
					visibleInputmethod(money_charge);
					break;
				case R.id.radio_btn1:
					money_charge.setText("");
					radio_btn4.setVisibility(View.VISIBLE);
					money_charge.setVisibility(View.GONE);
					if (parseRechargeSettingRes.size() > 2) {
						rechage_cash = parseRechargeSettingRes.get(0).getMin();
					}
					break;
				case R.id.radio_btn2:
					money_charge.setText("");
					radio_btn4.setVisibility(View.VISIBLE);
					money_charge.setVisibility(View.GONE);
					if (parseRechargeSettingRes.size() > 2) {
						rechage_cash = parseRechargeSettingRes.get(1).getMin();
					}
					break;
				case R.id.radio_btn3:
					money_charge.setText("");
					radio_btn4.setVisibility(View.VISIBLE);
					money_charge.setVisibility(View.GONE);
					if (parseRechargeSettingRes.size() > 2) {
						rechage_cash = parseRechargeSettingRes.get(2).getMin();
					}
					break;
				default:
					break;
				}

			}
		});
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.deposit_back_btn:
			finish(0, 0);
			break;
		case R.id.rechage_btn:
			if (TextUtils.isEmpty(chongzhi_edit.getText().toString())) {
				showdialog("充值卡密码不能为空");
				return;
			}
			parm.clear();
			parm.put("user_id", saveUtils.getStrSP(KeepingData.USER_ID));
			parm.put("sncode", chongzhi_edit.getText().toString());
			postdate(parm, Constants.getbindrecharge, handler, RECHAERGESUCCED,
					RECHAERGEFAILD, true, true);
			// 充值卡充值
			break;
		case R.id.alipay_layout:
			// 支付宝支付
			method = 6;
			radio2_btn.setChecked(true);
			break;
		case R.id.wechat_layout:
			// 微信支付
			method = 2;
			radio1_btn.setChecked(true);
			break;
		case R.id.rechage_commit_btn:
			moneyString = money_charge.getText().toString();
			money_charge.setVisibility(View.GONE);
			radio_btn4.setVisibility(View.VISIBLE);
			if (TextUtils.isEmpty(moneyString) && radio_btn4.isChecked()) {
				showdialog("充值金额不能为空");
				return;
			}
			if (!TextUtils.isEmpty(moneyString) && radio_btn4.isChecked()
					&& isFormatNum(moneyString)
					&& Double.valueOf(moneyString) <= 0.0) {
				showdialog("充值金额不能为0");
				return;
			}
			if (radio_btn4.isChecked() && !isFormatNum(moneyString)) {
				showdialog("充值金额格式不合法");
				return;
			}
			if (!TextUtils.isEmpty(moneyString) && isFormatNum(moneyString)) {
				rechage_cash = moneyString;
			}
			if (AppConfig.getInstance().isCanRecharge()) {
				parm.clear();
				parm.put("user_id", saveUtils.getStrSP(KeepingData.USER_ID));
				parm.put("paytype", String.valueOf(method));
				parm.put("fee", rechage_cash);
				postdate(parm, Constants.geticardrecharge, handler,
						ICRECHAERGESUCCED, ICRECHAERGEFAILD, false, true);
			}
			AppConfig.getInstance().setCanRecharge(false);
			break;
		default:
			break;
		}

	}

	public void getrecharge_settings() {
		parm.clear();
		getdate(parm, Constants.getrecharge_settings, handler,
				GETRECHARGESUCCED, GETRECHARGEFAILED, false, true, false);
	}

	private class LoadUrlImage extends AsyncTask<String, String, Bitmap> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected Bitmap doInBackground(String... args) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(
						rechargeAdsUrl).getContent());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap image) {
			if (image != null) {
				iv_recharge_ad.setImageBitmap(image);
			} else {
				iv_recharge_ad.setImageResource(R.drawable.banner_default);
			}
		}
	}

	public static boolean isFormatNum(String str) {

		try {
			new BigDecimal(str);
			return true;
		} catch (Exception e) {
			return false;
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
			}
		}

		@Override
		protected GetAccessTokenResult doInBackground(Void... params) {
			GetAccessTokenResult result = new GetAccessTokenResult();
			String url = String
					.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
							Constants.WXAPP_ID, Constants.WXAPP_SECRET);
			Log.d("GetAccessTokenResult", "get access token, url = " + url);
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
		int expiresIn;
		public int errCode;
		public String errMsg;

		public void parseFrom(String content) {

			if (content == null || content.length() <= 0) {
				Log.e("GetAccessTokenResult", "parseFrom fail, content is null");
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
				Log.e("GetPrepayIdResult", "parseFrom fail, content is null");
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
		progressDialog.cancel();
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
		return trade_no.getTrade_no() + genTimeStamp();
	}

	private String genNonceStr() {
		Random random = new Random();
		return Wx_MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private long timeStamp;
	private boolean isPaySupported;

	@SuppressLint("DefaultLocale")
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
			packageParams.add(new BasicNameValuePair("body", "e袋洗充值"));
			packageParams.add(new BasicNameValuePair("fee_type", "1"));
			packageParams.add(new BasicNameValuePair("input_charset", "UTF-8"));
			packageParams.add(new BasicNameValuePair("notify_url",
					Constants.WECHAT_NOTIFY_URL));
			packageParams.add(new BasicNameValuePair("out_trade_no", trade_no
					.getTrade_no()));
			packageParams.add(new BasicNameValuePair("partner",
					Constants.PARTNER_ID));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					getUserIp()));
			packageParams.add(new BasicNameValuePair("total_fee", (int) (Double
					.valueOf(rechage_cash) * 100) + ""));
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
		case "DepositWXPaySucess":
			geticard();
			break;
		}
	}
	// -----------微信支付-----------------------------------------

}
