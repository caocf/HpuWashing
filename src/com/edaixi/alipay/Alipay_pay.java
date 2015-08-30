package com.edaixi.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.edaixi.data.AppConfig;
import com.edaixi.util.Constants;
import com.edaixi.util.OrderListAdapterEvent;

import de.greenrobot.event.EventBus;

public class Alipay_pay {
	private static Activity mcontext;
	public static final String TAG = "alipay-sdk";
	private static final int SDK_PAY_FLAG = 1;
	private static final int RQF_LOGIN = 2;
	private String mprice;
	private String mouttradeno;
	private String mpaytitle;
	private Handler mhandler;
	private boolean mispay;

	public Alipay_pay(Activity activity, String price, String outtradeno,
			boolean ispay) {
		mcontext = activity;
		mprice = price;
		mouttradeno = outtradeno;
		mispay = ispay;
	}

	private String outtradeno;
	private TextView mUserId;
	private String result;

	public void pay() {
		// 订单
		String orderInfo = getNewOrderInfo(mprice, mouttradeno);

		// 对订单做RSA 签名
		String sign = Rsa.sign(orderInfo, Keys.PRIVATE);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(mcontext);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();

	}

	private String getNewOrderInfo(String price, String outtradeno) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + Keys.DEFAULT_PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + Keys.DEFAULT_SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + outtradeno + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + "e袋洗安卓客户端" + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + "e袋洗安卓客户端" + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\""
				+ URLEncoder.encode(Constants.ALIPAY_NOTIFY_URL) + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	private String getUserInfo() {
		String userId = mUserId.getText().toString();
		return trustLogin(Keys.DEFAULT_PARTNER, userId);
	}

	private String trustLogin(String partnerId, String appUserId) {
		StringBuilder sb = new StringBuilder();
		sb.append("app_name=\"mc\"&biz_type=\"trust_login\"&partner=\"");
		sb.append(partnerId);
		Log.d("TAG", "UserID = " + appUserId);
		if (!TextUtils.isEmpty(appUserId)) {
			appUserId = appUserId.replace("\"", "");
			sb.append("\"&app_id=\"");
			sb.append(appUserId);
		}
		sb.append("\"");

		String info = sb.toString();
		// 请求信息签名
		String sign = Rsa.sign(info, Keys.PRIVATE);
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		info += "&sign=\"" + sign + "\"&" + getSignType();

		return info;
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG:

				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					if (AppConfig.getInstance().isOrderZhifubaoPay()) {
						EventBus.getDefault().post(
								new OrderListAdapterEvent("ZhiFuBaoPaySucess"));
					} else {
						EventBus.getDefault()
								.post(new OrderListAdapterEvent(
										"DepositALiPaySucess"));
					}
					Toast.makeText(mcontext, "支付成功", Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(mcontext, "支付结果确认中", Toast.LENGTH_SHORT)
								.show();
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(mcontext, "支付失败", Toast.LENGTH_SHORT)
								.show();

					}
				}
				AppConfig.getInstance().setCanPay(true);
				AppConfig.getInstance().setCanRecharge(true);
				break;

			case RQF_LOGIN: {
			}
				break;
			default:
				break;
			}
		};
	};
}
