package com.edaixi.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.Userbean;
import com.google.gson.Gson;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetUtil {
	public static final String TAG_POST = "Post方式";
	private static final String TAG_Get = "Get方式";
	private static final String TAG = "NetUtil";
	private static Context mContext;
	private Resources mRes;
	private Userbean userbean;
	private Gson gson;
	private SaveUtils saveUtils;
	private AppConfig mAppConfig;

	public NetUtil(Context context) {
		mContext = context;
		userbean = new Userbean();
		gson = new Gson();
		saveUtils = new SaveUtils(mContext);
	}

	private String join(List<String> strings, String join) {
		StringBuilder sb = new StringBuilder();
		for (String string : strings) {
			sb.append(string);
			sb.append(join);
		}
		return sb.substring(0, sb.length() - 1);
	}

	private String sign(List<String> strings) {
		Collections.sort(strings);
		String str = join(strings, "&");
		if (strings.contains("user_id")) {
			return MD5util.string2MD5(str + "LSEUa4APd5"
					+ saveUtils.getStrSP("user_token"));
		} else {
			return MD5util.string2MD5(str + "LSEUa4APd5");
		}
	}

	public String sign1(String strings) {
		return MD5util.string2MD5(strings);
	}

	// Post方式请求
	private String requestByPost(String path, Map<String, Object> params)
			throws Exception {
		params.put("app_key", "app_client");
		params.put("version", getVersion());
		params.put("user_type", 3);
		params.put("market", "");
		List<String> pairs = new ArrayList<String>();
		for (String key : params.keySet()) {
			pairs.add(key + "=" + params.get(key));
		}

		String sign = sign(pairs);
		pairs.clear();
		pairs.add("sign=" + sign);
		for (String key : params.keySet()) {
			pairs.add(key
					+ "="
					+ URLEncoder.encode(String.valueOf(params.get(key)),
							Constants.ENCODE));
		}
		String param = join(pairs, "&");

		String result = "-1";
		// 请求的参数转换为byte数组
		// 新建一个URL对象
		URL url = new URL(path + param);
		// 打开一个HttpURLConnection连接
		LogUtil.e("HttpURLConnection连接" + url);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		// 设置连接超时时间
		urlConn.setConnectTimeout(10 * 1000);
		// Post请求必须设置允许输出
		urlConn.setDoOutput(true);
		// Post请求不能使用缓存
		urlConn.setUseCaches(false);
		// 设置为Post请求
		urlConn.setRequestMethod("POST");
		urlConn.setInstanceFollowRedirects(true);
		// 配置请求Content-Type
		urlConn.setRequestProperty("content-type", "text/html;charset=utf-8");
		// 开始连接
		urlConn.connect();
		// 发送请求参数
		// 判断请求是否成功
		if (urlConn.getResponseCode() == 200
				|| urlConn.getResponseCode() == 201) {
			// 获取返回的数据
			byte[] data = readStream(urlConn.getInputStream());
			result = new String(data, Constants.ENCODE);
			LogUtil.e(" 登陆接口返回的数据--" + result);
		} else {
			result = "-1";
		}
		urlConn.disconnect();
		return result;
	}

	private String requestByGet(String path, HashMap<String, Object> params)
			throws Exception {
		params.put("app_key", "app_client");
		params.put("version", getVersion());
		params.put("user_type", 3);
		params.put("maket", "");
		List<String> pairs = new ArrayList<String>();
		for (String key : params.keySet()) {
			pairs.add(key + "=" + params.get(key));
		}
		String sign = sign(pairs);
		pairs.clear();
		pairs.add("sign=" + sign);

		for (String key : params.keySet()) {
			pairs.add(key
					+ "="
					+ URLEncoder.encode(String.valueOf(params.get(key)),
							Constants.ENCODE));
		}
		String param = join(pairs, "&");

		String result = "-1";
		// 请求的参数转换为byte数组
		// 新建一个URL对象
		System.out.println("path+param:" + path + param);
		URL url = new URL(path + param);
		// 打开一个HttpURLConnection连接
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		// 设置连接超时时间
		urlConn.setConnectTimeout(10 * 1000);
		// Post请求必须设置允许输出
		// urlConn.setDoOutput(true);
		// Post请求不能使用缓存
		// urlConn.setUseCaches(false);
		// 设置为Post请求
		urlConn.setRequestMethod("GET");
		urlConn.setInstanceFollowRedirects(true);
		// 配置请求Content-Type
		urlConn.setRequestProperty("content-type", "text/html;charset=utf-8");
		// 开始连接
		urlConn.connect();
		// 发送请求参数
		// 判断请求是否成功
		if (urlConn.getResponseCode() == 200) {
			// 获取返回的数据
			byte[] data = readStream(urlConn.getInputStream());
			result = new String(data, Constants.ENCODE);
		} else {
			System.out.println("" + urlConn.getResponseCode());
			result = "-1";
		}
		urlConn.disconnect();
		return result;
	}

	// 获取连接返回的数据
	private static byte[] readStream(InputStream inputStream) throws Exception {
		byte[] buffer = new byte[4096];
		int len = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int count = 1;
		while ((len = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
			buffer = new byte[4096];
		}
		byte[] data = baos.toByteArray();
		inputStream.close();
		baos.close();
		return data;
	}

	public String getlogin(String phone, String code, String push_token) {
		String path = Constants.getlogin;
		String str = "";
		String result = "";
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("phone", phone);
			params.put("code", code);
			params.put("push_token", "");
			try {
				result = requestByPost(path, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null != result && !"".equals(result) && !"-1".equals(result)) {
				JSONObject json = new JSONObject(result);
				if (json.getBoolean("ret")) {
					str = "true";
					saveUtils.saveStrSP("ts", json.getString("ts"));
					String datajson = json.getString("data");
					Userbean userbean = gson.fromJson(datajson, Userbean.class);
					saveUtils.saveStrSP("user_token", userbean.getUsertoken());
					saveUtils.saveStrSP("user_id", userbean.getUser_id());
					mAppConfig = AppConfig.getInstance();
					mAppConfig.setUserId(userbean.getUser_id());
					mAppConfig.setUserToken(userbean.getUsertoken());
				} else {
					try {
						json = new JSONObject(result);
						if (!json.getBoolean("ret")) {
							str = json.getString("error");
						}
						if (!json.getBoolean("ret")
								&& json.getString("error").contains("401")) {
							saveUtils.saveBoolSP(KeepingData.LOGINED, false);
							saveUtils.saveBoolSP(KeepingData.IS_FIRSTLOGINED,
									false);
							saveUtils.saveStrSP("user_token", "");
							saveUtils.saveStrSP("user_id", "");
							str = mAppConfig.ALREADYLOGIN;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return str;
	}

	public String creataddress(String user_id, String username, String tel,
			String province, String city, String area, String address) {
		String path = Constants.getcreataddress;
		String str = "";
		String result = "";
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("user_id", user_id);
			params.put("username", username);
			params.put("tel", tel);
			params.put("province", province);
			params.put("city", city);
			params.put("area", area);
			params.put("address", address);
			System.out.println(path + params);
			try {
				result = requestByPost(path, params);
			} catch (Exception e) {

				e.printStackTrace();
			}
			if (null != result && !"".equals(result) && !"-1".equals(result)) {
				JSONObject object = new JSONObject(result);
				if (object.get("ret").equals(true)) {
					str = "true";
					userbean.setTs(object.getString("ts"));
				} else {
					str = object.getString("error");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(str);
		return str;

	}

	public String getsms(String phone) {
		String path = Constants.sendsms;
		String str = "";
		String result = "";
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("phone", phone);
			System.out.println(path + params);
			try {
				result = requestByPost(path, params);
			} catch (Exception e) {

				e.printStackTrace();
			}
			if (null != result && !"".equals(result) && !"-1".equals(result)) {
				JSONObject object = new JSONObject(result);
				if (object.get("ret").equals(true)) {
					str = "true";

					userbean.setTs(object.getString("ts"));
				} else {
					str = object.getString("error");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(str);
		return str;
	}

	public String getVersion() {
		PackageManager pm = mContext.getPackageManager();
		try {
			// 代表的就是清单文件的信息。
			PackageInfo packageInfo = pm.getPackageInfo(
					mContext.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// 肯定不会发生。
			return "";
		}
	}

	/**
	 * 检查网络连接状态，是否有可用的网络连接
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean getNetworkState(Context mContext) {

		ConnectivityManager conManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}

}
