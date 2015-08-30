package com.edaixi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.support.v4.app.FragmentActivity;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.*;
import com.edaixi.util.*;
import com.edaixi.view.*;
import com.google.gson.Gson;

public abstract class BaseActivity extends FragmentActivity {

	private Activity activity;
	private Context context;
	private ArrayList<Activity> activitylist = new ArrayList<Activity>();
	private MyhttpUtils utils;
	private SaveUtils saveutils = null;
	private Gson gson;
	private HttpCommonBean httpConmonBean;

	/**
	 * 所有Activity的初始化方法
	 * 
	 * @param activity
	 */
	public void init(Activity activity) {
		addactivity();
		this.activity = activity;
		getContext();
		utils = new MyhttpUtils(context);
		saveutils = new SaveUtils(context);
		gson = new Gson();
	}

	public Gson getGson() {
		return gson;
	};

	/**
	 * 获取返回的公共数据集
	 * 
	 * @return
	 */
	protected HttpCommonBean getHttpConmonbean() {
		httpConmonBean = new HttpCommonBean();
		return httpConmonBean;
	}

	/**
	 * 获取SP文件user_id
	 * 
	 * @return "user_id"
	 */
	public String getUerId() {
		return saveutils.getStrSP(KeepingData.USER_ID);

	}

	/**
	 * 创建SharedPreferences对象
	 * 
	 * @return {@link SaveUtils}
	 */
	public SaveUtils getsaveutils_instants() {
		if (saveutils == null) {
			saveutils = new SaveUtils(BaseActivity.this);
			return saveutils;
		} else {
			return saveutils;
		}
	}

	/**
	 * 显示自定义提示框
	 * 
	 * @param 提示内容
	 */
	public void showdialog(final String msgstr) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!isFinishing()) {
					CustomDialog customDialog = CustomDialog
							.createProgressDialog(context);
					customDialog.setMessage(msgstr);
					customDialog.show();
				}
			}
		});
	}

	/**
	 * 隐藏输入法
	 * 
	 * @param 触发的view
	 */
	public void invisibleInputmethod(View v) {
		InputMethodManager imm = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	/**
	 * 显示输入法
	 * 
	 * @param 触发的view
	 */
	public void visibleInputmethod(View v) {
		InputMethodManager imm = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * @return 对应的Activity
	 */
	public Activity getActivity() {
		return activity;
	}

	/**
	 * 返回每个Activity上下文
	 * 
	 * @return 上下文
	 */
	public Context getContext() {
		context = activity;
		return context;
	}

	public void addactivity() {
		activitylist.add(activity);
	};

	/**
	 * Back键按下回调事件
	 * 
	 * @return true 表示事件被消费
	 */
	protected abstract boolean onBackKeyDown();

	/**
	 * Menu键按下的回调事件，各子类Activity可根据需要自行覆写该方法即可
	 * 
	 * @return
	 */
	protected boolean onMenuKeyDown() {
		return false;
	}

	/**
	 * 子类不可覆写此方法 若需监听返回和菜单按钮事件，需覆写onBackKeyDown和onMenuKeyDown方法 若需监听其他按键事件，需覆写
	 * {@link onKeyDown(int keyCode, KeyEvent event, int voidID) }方法
	 */
	@Override
	public final boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (onBackKeyDown()) {
				return true;
			}
			break;
		case KeyEvent.KEYCODE_MENU:
			if (onMenuKeyDown()) {
				return true;
			}
			break;
		default:
			break;
		}
		return onKeyDown(keyCode, event, 0);
	}

	/**
	 * 子类监听特殊按键事件需覆写此方法
	 * 
	 * @param keyCode
	 * @param event
	 * @param voidID
	 *            无实际意义，用于区别原有onKeyDown方法
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event, int voidID) {
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 简单的打开一个新的Activity，并传入必要的参数
	 * 
	 * @param target
	 * @param enterAnim
	 *            Use 0 for no animation.
	 * @param exitAnim
	 *            Use 0 for no animation.
	 * @param isFinish
	 *            是否需要关闭当前Activity
	 * @param mBundle
	 *            不需要时可以为空
	 */
	public final void startNewActivity(Class<? extends Activity> target,
			int enterAnim, int exitAnim, boolean isFinish, Bundle mBundle) {
		Intent mIntent = new Intent(this, target);
		if (mBundle != null) {
			mIntent.putExtras(mBundle);
		}
		startActivity(mIntent);
		// overridePendingTransition(enterAnim, exitAnim);
		if (isFinish) {
			finish();
		}
	}

	/**
	 * 打开一个可以回调数据的Activity，并传入必要的参数
	 * 
	 * @param target
	 * @param enterAnim
	 *            Use 0 for no animation.
	 * @param exitAnim
	 *            Use 0 for no animation.
	 * @param requestCode
	 * @param mBundle
	 *            不需要时可以为空
	 */
	public final void startNewActivityForResult(
			Class<? extends Activity> target, int enterAnim, int exitAnim,
			int requestCode, Bundle mBundle) {
		Intent mIntent = new Intent(this, target);
		if (mBundle != null) {
			mIntent.putExtras(mBundle);
		}
		startActivityForResult(mIntent, requestCode);
		// overridePendingTransition(enterAnim, exitAnim);
	}

	/**
	 * 开启一个普通的后台服务
	 * 
	 * @param target
	 * @param mBundle
	 */
	protected final void startService(Class<? extends Service> target,
			Bundle mBundle) {
		Intent mIntent = new Intent(this, target);
		if (mBundle != null) {
			mIntent.putExtras(mBundle);
		}
		startService(mIntent);
	}

	/**
	 * 关闭一个普通的后台服务
	 * 
	 * @param target
	 * @param mBundle
	 */
	protected final void stopService(Class<? extends Service> target,
			Bundle mBundle) {
		Intent mIntent = new Intent(this, target);
		if (mBundle != null) {
			mIntent.putExtras(mBundle);
		}
		stopService(mIntent);
	}

	/**
	 * 使用指定动画参数关闭当前Activity
	 * 
	 * @param enterAnim
	 *            Use 0 for no animation.
	 * @param exitAnim
	 *            Use 0 for no animation.
	 */
	protected void finish(int enterAnim, int exitAnim) {
		finish();
		// overridePendingTransition(enterAnim, exitAnim);
	}

	/*
	 * 退出
	 */
	public void Exit() {
		if (activitylist.size() > 0) {
			for (Activity activity : activitylist) {
				activity.finish();
			}
		}
		System.exit(0);
	};

	public boolean isHasNet() {

		ConnectivityManager cwjManager = (ConnectivityManager) getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cwjManager.getActiveNetworkInfo() != null) {
			return cwjManager.getActiveNetworkInfo().isAvailable();
		} else {
			return false;
		}
	}

	/*
	 * 获取数据
	 */
	public void getdate(HashMap<String, String> params, String path,
			Handler handler, int TUREMESSAGER, int ERRORMESSAGER,
			boolean isshow, boolean showNonet, boolean iscache) {
		if (!NetUtil.getNetworkState(this) && showNonet) {
			showdialog("网络不可用，请检查您的网络连接");
			return;
		}
		String url = utils.getUrl(params, path, getContext());
		utils.getdate(getContext(), handler, TUREMESSAGER, ERRORMESSAGER,
				params, url, isshow, iscache);
	}

	/*
	 * 获取数据
	 */
	public void postdate(HashMap<String, String> params, String path,
			Handler handler, int TUREMESSAGER, int ERRORMESSAGER,
			boolean isshow, boolean showNonet) {
		if (!NetUtil.getNetworkState(this)) {
			showdialog("网络不可用，请检查您的网络连接");
			return;
		}
		String url = utils.getUrl(params, path, getContext());
		utils.postdate(getContext(), handler, TUREMESSAGER, ERRORMESSAGER,
				params, url, isshow);
	}
}
