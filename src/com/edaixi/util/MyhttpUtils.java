package com.edaixi.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.edaixi.activity.LoginActivity;
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.view.RongchainProgressDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MyhttpUtils {
	private Context mContext;
	private String url = "";
	public NetUtil netUtil;
	private SaveUtils saveutils;
	private RongchainProgressDialog rongchainProgress;

	public MyhttpUtils(Context context) {
		super();
		mContext = context;
		netUtil = new NetUtil(context);
		saveutils = new SaveUtils(context);
	}

	private String join(List<String> strings, String join) {
		StringBuilder sb = new StringBuilder();
		for (String string : strings) {
			sb.append(string);
			sb.append(join);
		}
		return sb.substring(0, sb.length() - 1);
	}

	private String sign(List<String> strings, boolean isAddUserToken) {
		Collections.sort(strings);
		String str = join(strings, "&");
		return MD5util.string2MD5(str + "LSEUa4APd5"
				+ (isAddUserToken ? saveutils.getStrSP("user_token") : ""));
	}

	public String getUrl(HashMap<String, String> params, String path,
			Context context) {
		params.put("app_key", "app_client");
		params.put("version", getVersion(context));
		params.put("user_type", "3");
		params.put("market", "");
		List<String> pairs = new ArrayList<String>();

		pairs.clear();
		for (String key : params.keySet()) {
			pairs.add(key + "=" + params.get(key));
		}
		String sign = sign(pairs, params.containsKey("user_id"));
		pairs.add("sign=" + sign);
		String param = join(pairs, "&");
		url = path + param;
		return url;
	}

	public String getVersion(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			// 代表的就是清单文件的信息
			PackageInfo packageInfo = pm.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();

			return "";
		}
	}

	/***
	 * get方法获取服务端数据
	 * 
	 * @param context
	 * @param handler
	 * @param TUREMESSAGER
	 * @param ERRORMESSAGER
	 * @param parm
	 * @param url
	 */
	public void getdate(final Context context, final Handler handler,
			final int TUREMESSAGER, final int ERRORMESSAGER,
			final HashMap<String, String> parm, final String url,
			final boolean isshow, boolean iscache) {
		final RequestParams params = new RequestParams();
		Set<String> keySet = parm.keySet();
		Iterator iterator = keySet.iterator();
		if (iterator.hasNext()) {
			String string = (String) iterator.next();
			params.addQueryStringParameter(string, parm.get(string));
		}
		HttpUtils http = new HttpUtils();
		http.configTimeout(10 * 1000);
		if (iscache) {
			http.configCurrentHttpCacheExpiry(1000 * 10);
			http.configRequestRetryCount(2);
			http.configHttpCacheSize(0);
		}
		http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				if (isshow) {
					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (!((Activity) context).isFinishing()) {
								rongchainProgress = RongchainProgressDialog
										.createProgressDialog(mContext);
								rongchainProgress.setMessage("正在拼命加载...");
								rongchainProgress.show();
							}
						}
					});

				}
				super.onStart();
			}

			@Override
			public void onFailure(HttpException arg0, String result) {
				((Activity) context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (!((Activity) context).isFinishing()) {
							if (rongchainProgress != null) {
								rongchainProgress.cancel();
								rongchainProgress = null;
							}
						}
					}
				});
				if (result == null) {
					return;
				}
				try {
					Message msg = Message.obtain();
					msg.what = ERRORMESSAGER;
					msg.obj = result;
					handler.sendMessage(msg);
				} catch (Exception e) {
					handler.sendEmptyMessage(ERRORMESSAGER);
					e.printStackTrace();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> result) {
				((Activity) context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (!((Activity) context).isFinishing()) {
							if (rongchainProgress != null) {
								rongchainProgress.cancel();
								rongchainProgress = null;
							}
						}
					}
				});
				LogUtil.e("---url---"+url+"請求返回結果-get"+result.result);
				AppConfig appconfig = AppConfig.getInstance();
				JSONObject json;
				try {
					json = new JSONObject(result.result);
					if (!json.getBoolean("ret")
							&& json.getString("error").contains("401")) {
						saveutils.saveBoolSP(KeepingData.LOGINED, false);
						saveutils.saveStrSP("user_token", "");
						saveutils.saveStrSP("user_id", "");
						saveutils
								.saveBoolSP(KeepingData.IS_FIRSTLOGINED, false);
						if (AppConfig.getInstance().isIslogin()) {
							Toast.makeText(mContext, "请重新登录", 0).show();
							Intent intent = new Intent(context,
									LoginActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("iscanback", true);
							context.startActivity(intent);
							((Activity) context).finish();
							return;
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Message msg = Message.obtain();
				msg.what = TUREMESSAGER;
				msg.obj = result.result;
				handler.sendMessage(msg);
			}
		});
	}

	public void postdate(final Context context, final Handler handler,
			final int TUREMESSAGER, final int ERRORMESSAGER,
			HashMap<String, String> parm, final String url, final boolean isshow) {
		final RequestParams params = new RequestParams();
		Set<String> keySet = parm.keySet();
		Iterator iterator = keySet.iterator();
		if (iterator.hasNext()) {
			String string = URLEncoder.encode((String) iterator.next());
			params.addQueryStringParameter(string, parm.get(string));
		}

		HttpUtils http = new HttpUtils();
		http.configTimeout(10 * 1000);
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			private String resultstr;

			@Override
			public void onStart() {
				LogUtil.e("url-------------" + url + "paras--"
						+ params.toString());
				if (isshow) {
					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (!((Activity) context).isFinishing()) {
								rongchainProgress = RongchainProgressDialog
										.createProgressDialog(mContext);
								rongchainProgress.setMessage("正在拼命加载...");
								rongchainProgress.show();
							}
						}
					});
				}
				super.onStart();
			}

			@Override
			public void onFailure(HttpException arg0, String result) {
				((Activity) context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (!((Activity) context).isFinishing()) {
							if (rongchainProgress != null) {
								rongchainProgress.cancel();
								rongchainProgress = null;
							}
						}
					}
				});

				if (result == null) {
					return;
				}
				try {
					Message msg = Message.obtain();
					msg.what = ERRORMESSAGER;
					msg.obj = result;
					handler.sendMessage(msg);
				} catch (Exception e) {
					handler.sendEmptyMessage(ERRORMESSAGER);
					e.printStackTrace();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> result) {
				((Activity) context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (!((Activity) context).isFinishing()) {
							if (rongchainProgress != null) {
								rongchainProgress.cancel();
								rongchainProgress = null;
							}
						}
					}
				});
				LogUtil.e("---url---"+url+"請求返回結果-post"+result.result);
				JSONObject json;
				try {
					json = new JSONObject(result.result);
					if (!json.getBoolean("ret")
							&& json.getString("error").contains("401")) {
						saveutils.saveBoolSP(KeepingData.LOGINED, false);
						saveutils.saveStrSP("user_token", "");
						saveutils
								.saveBoolSP(KeepingData.IS_FIRSTLOGINED, false);
						Toast.makeText(mContext, "请重新登录.", 0).show();
						Intent intent = new Intent();
						intent.setClass(context, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(intent);
						((Activity) context).finish();
						return;
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				Message msg = Message.obtain();
				msg.what = TUREMESSAGER;
				msg.obj = result.result;
				if (msg.obj == null) {
					return;
				} else {
					handler.sendMessage(msg);
				}
			}
		});
	}
}
