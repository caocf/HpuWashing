package com.edaixi.util;

import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class TecentAdsUtil {

	private static Context mContext;

	public TecentAdsUtil(Context mContext) {
		super();
		TecentAdsUtil.mContext = mContext;
	}

	@SuppressWarnings("deprecation")
	public void getToTencentAds() throws SocketException {
		String encrypt_key = "ec779a4e4d798890";
		String IMEIString = getDeviceIMEI();
		String TimeString = getSystemTime();
		String query_string;
		try {
			query_string = "muid=" + URLEncoder.encode(IMEIString, "UTF-8")
					+ "&conv_time=" + URLEncoder.encode(TimeString, "UTF-8");

			String page = "http://t.gdt.qq.com/conv/app/1104311868" + "/conv?"
					+ query_string;
			String encode_page = URLEncoder.encode(page, "UTF-8");
			String property = "d808c91eef4098e9&GET&" + encode_page;
			String signature = string2MD5(property);

			String base_data = query_string + "&sign="
					+ URLEncoder.encode(signature, "UTF-8");
			String base64 = ToBase64(base_data, encrypt_key);
			String data = new String(Base64Util.encode(base64.getBytes()));
			String attachment = "conv_type="
					+ URLEncoder.encode("MOBILEAPP_ACTIVITE", "UTF-8")
					+ "&app_type=" + URLEncoder.encode("ANDROID", "UTF-8")
					+ "&advertiser_id=" + URLEncoder.encode("673411", "UTF-8");
			final String finalUrl = "http://t.gdt.qq.com/conv/app/1104311868/conv?v="
					+ data + "&" + attachment;

			HttpUtils http = new HttpUtils();
			http.configTimeout(10 * 1000);
			http.send(HttpMethod.GET, finalUrl, new RequestCallBack<String>() {

				@Override
				public void onStart() {
				}

				@Override
				public void onFailure(HttpException arg0, String result) {
				}

				@Override
				public void onSuccess(ResponseInfo<String> result) {
					LogUtil.e("===广点通返回的结果==="+result.result);
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("DefaultLocale")
	public static String getDeviceIMEI() {
		String imei = ((TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		return string2MD5(imei.toLowerCase());
	}

	// 获取当前时间戳
	public static String getSystemTime() {
		long time = System.currentTimeMillis() / 1000;
		return time + "";
	}

	// MD5工具类
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	// Base64工具类
	public static String ToBase64(String base_data, String encrypt_key) {
		char[] base_datachar = base_data.toCharArray();
		char[] base_datacharBak = new char[base_datachar.length];
		char[] encrypt_keychar = encrypt_key.toCharArray();
		int j = 0;
		for (int i = 0; i < base_datachar.length; i++) {
			base_datacharBak[i] = (char) (base_datachar[i] ^ encrypt_keychar[j]);
			j = (++j) % (encrypt_keychar.length);
		}
		return new String(base_datacharBak);
	}

}
