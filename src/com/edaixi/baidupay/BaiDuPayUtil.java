package com.edaixi.baidupay;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.baidu.android.pay.PayCallBack;
import com.baidu.paysdk.PayCallBackManager.PayStateModle;
import com.baidu.paysdk.api.BaiduPay;
import com.edaixi.modle.BaiDuPayOrderInfo;
import com.edaixi.util.Constants;
import com.edaixi.util.LogUtil;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.util.SaveUtils;
import de.greenrobot.event.EventBus;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

public class BaiDuPayUtil {

	public static final String TAG = "EbPayDemo";
	ProgressDialog progressDialog;
	private Context mContext;
	private SaveUtils saveUtils;

	private static final int CREATE_ORDER = 1;
	public BaiDuPayOrderInfo baiDuPayOrderInfo;

	public BaiDuPayUtil(Context mContext, BaiDuPayOrderInfo baiDuPayOrderInfo) {
		super();
		this.mContext = mContext;
		this.baiDuPayOrderInfo = baiDuPayOrderInfo;
		saveUtils = new SaveUtils(mContext);
	}

	@SuppressLint("HandlerLeak")
	private Handler mDopayHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog != null) {
				if (progressDialog.isShowing()) {
					progressDialog.cancel();
				}
			}
			switch (msg.what) {
			case CREATE_ORDER:
				if (msg.obj != null) {
					if (msg.obj instanceof String) {
						String str = (String) msg.obj;
						if (!TextUtils.isEmpty(str)
								&& str.contains("service_code")) {
							realPay(str);
						} else {
							Toast.makeText(mContext, "订单创建失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 发起支付
	 * 
	 * @param name
	 *            商品名称
	 * @param price
	 *            商品价格
	 * @param bigPrice
	 */
	public void pay() {
		String price = baiDuPayOrderInfo.getUnit_amount();
		if (TextUtils.isEmpty(price)) {
			return;
		}
		BigDecimal bigPrice = new BigDecimal(price);
		if (bigPrice.compareTo(new BigDecimal(0)) != 1) {
			return;
		}
		String orderinfo = createOrderInfo("e袋洗订单", price, "1");
		realPay(orderinfo);
	}

	/**
	 * 支付结果处理
	 * 
	 * @param stateCode
	 * @param payDesc
	 */
	private void handlepayResult(int stateCode, String payDesc) {
		LogUtil.e("payDesc=" + payDesc + "#len=" + payDesc.length());
		switch (stateCode) {
		case PayStateModle.PAY_STATUS_SUCCESS:
			// 需要到服务端验证支付结果
			EventBus.getDefault().post(
					new OrderListAdapterEvent("WXPaySucess"));
			break;
		case PayStateModle.PAY_STATUS_PAYING:
			// 需要到服务端验证支付结果
			Toast.makeText(mContext, "支付处理中", Toast.LENGTH_SHORT).show();
			break;
		case PayStateModle.PAY_STATUS_CANCEL:
			Toast.makeText(mContext, "取消", Toast.LENGTH_SHORT).show();
			break;
		case PayStateModle.PAY_STATUS_NOSUPPORT:
			Toast.makeText(mContext, "不支持该种支付方式", Toast.LENGTH_SHORT).show();
			break;
		case PayStateModle.PAY_STATUS_TOKEN_INVALID:
			Toast.makeText(mContext, "无效的登陆状态", Toast.LENGTH_SHORT).show();
			break;
		case PayStateModle.PAY_STATUS_LOGIN_ERROR:
			Toast.makeText(mContext, "登陆失败", Toast.LENGTH_SHORT).show();
			break;
		case PayStateModle.PAY_STATUS_ERROR:
			Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
			break;
		case PayStateModle.PAY_STATUS_LOGIN_OUT:
			Toast.makeText(mContext, "退出登录", Toast.LENGTH_SHORT).show();
			break;
		default:
			Toast.makeText(mContext, "支付失败" + stateCode, Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}

	/**
	 * 组装订单信息
	 * 
	 * @param position
	 * @return
	 */
	private String createOrderInfo(String name, String price, String num) {
		BigDecimal bigPrice = new BigDecimal(price); // 创建BigDecimal对象
		
		BigDecimal bigNum = new BigDecimal(num);
		BigDecimal bigInterest = bigPrice.multiply(bigNum);
		/** 合约用户 */
		String spUno = getUUID() + saveUtils.getStrSP("user_id");
		String spUnoParam = "";
		if (!TextUtils.isEmpty(spUno)) {
			spUnoParam = "&sp_uno=" + spUno;
		}
		if (TextUtils.isEmpty(PartnerConfig.PARTNER_ID)
				|| TextUtils.isEmpty(PartnerConfig.MD5_PRIVATE)) {
		}
		StringBuffer orderInfo = new StringBuffer("currency=1&extra=");
		String orderNo = baiDuPayOrderInfo.getOrder_no();
		try {
			orderInfo
					.append("&goods_desc=")
					.append(new String(getUTF8toGBKString(name)))
					.append("&goods_name=")
					.append(new String(getUTF8toGBKString(name)))
					.append("&goods_url=http://www.edaixi.com")
					.append("&input_charset=1&order_create_time="
							+ getOrderCreateTime() + "&order_no=" + orderNo
							+ "&pay_type=2&return_url="
							+ Constants.BAIDU_NOTIFY_URL)
					.append("&service_code=1&sign_method=1&sp_no="
							+ PartnerConfig.PARTNER_ID + "&sp_request_type=0"
							+ spUnoParam + "&total_amount="+bigInterest
							+ "&transport_amount=0&unit_amount="+bigPrice.toString()
							+ "&unit_count=" + bigNum.toString() + "&version=2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer orderInfo1 = new StringBuffer("currency=1&extra=");
		try {
			orderInfo1
					.append("&goods_desc=")
					.append(URLEncoder.encode(name, "GBK"))
					.append("&goods_name=")
					.append(URLEncoder.encode(name, "GBK"))
					.append("&goods_url=http://www.edaixi.com")
					.append("&input_charset=1&order_create_time="
							+ getOrderCreateTime() + "&order_no=" + orderNo
							+ "&pay_type=2&return_url="
							+ Constants.BAIDU_NOTIFY_URL)
					.append("&service_code=1&sign_method=1&sp_no="
							+ PartnerConfig.PARTNER_ID + "&sp_request_type=0"
							+ spUnoParam + "&total_amount="+bigInterest
							+ "&transport_amount=0&unit_amount="+bigPrice.toString()
							+ "&unit_count=" + bigNum.toString() + "&version=2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.e("orderInfo.toString()---"+orderInfo.toString());
		String signed = MD5.toMD5(orderInfo.toString() + "&key="
				+ PartnerConfig.MD5_PRIVATE);
		return orderInfo1.toString() + "&sign=" + signed;
	}

	private void realPay(String orderInfo) {
		Map<String, String> map = new HashMap<String, String>();
		BaiduPay.getInstance().doPay(mContext, orderInfo, new PayCallBack() {
			public void onPayResult(int stateCode, String payDesc) {
				handlepayResult(stateCode, payDesc);
			}

			@Override
			public boolean isHideLoadingDialog() {
				return false;
			}

		}, map);

	}

	/**
	 * 字符转换从UTF-8到GBK
	 * 
	 * @param gbkStr
	 * @return
	 */
	public static byte[] getUTF8toGBKString(String gbkStr) {
		int n = gbkStr.length();
		byte[] utfBytes = new byte[3 * n];
		int k = 0;
		for (int i = 0; i < n; i++) {
			int m = gbkStr.charAt(i);
			if (m < 128 && m >= 0) {
				utfBytes[k++] = (byte) m;
				continue;
			}
			utfBytes[k++] = (byte) (0xe0 | (m >> 12));
			utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
			utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
		}
		if (k < utfBytes.length) {
			byte[] tmp = new byte[k];
			System.arraycopy(utfBytes, 0, tmp, 0, k);
			return tmp;
		}
		return utfBytes;
	}

	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}

	public String getOrderCreateTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = format.format(new Date());
		return s;
	}
}
