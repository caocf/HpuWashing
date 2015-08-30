package com.edaixi.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SaveUtils {
	private Context mContext;
	private String NAME = "edaixi";

	public SaveUtils(Context _context) {
		mContext = _context;
	}

	/**
	 * 保存String系统配置
	 * 
	 * @param context
	 * @param SPKey
	 * @param SPValue
	 */
	public void saveStrSP(String SPKey, String SPValue) {
		SharedPreferences sp = mContext.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(SPKey, SPValue);
		editor.commit();
	}

	/**
	 * 读取String系统配置
	 * 
	 * @param context
	 * @param SPKey
	 * @return
	 */
	public String getStrSP(String SPKey) {

		SharedPreferences sp = mContext.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		return sp.getString(SPKey, "");
	}

	/**
	 * 判断是否包含某个键值对
	 * 
	 * @param context
	 * @param SPKey
	 * @return
	 */
	public boolean isContainsStrSP(String SPKey) {

		SharedPreferences sp = mContext.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		return sp.contains(SPKey);
	}

	/**
	 * 保存bool系统配置
	 * 
	 * @param context
	 * @param SPKey
	 * @param SPValue
	 */
	public void saveBoolSP(String SPKey, Boolean SPValue) {
		SharedPreferences sp = mContext.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(SPKey, SPValue);
		editor.commit();
	}

	/**
	 * 读取bool系统配置
	 * 
	 * @param context
	 * @param SPKey
	 * @return true/false
	 */
	public Boolean getBoolSP(String SPKey) {
		SharedPreferences sp = mContext.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(SPKey, false);
	}

	/**
	 * 保存int系统配置
	 * 
	 * @param context
	 * @param SPKey
	 * @param SPValue
	 */
	public void saveIntSP(String SPKey, int SPValue) {
		SharedPreferences sp = mContext.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt(SPKey, SPValue);
		editor.commit();
	}

	/**
	 * 读取int系统配置
	 * 
	 * @param context
	 * @param SPKey
	 * @return int
	 */
	public int getIntSP(String SPKey) {
		SharedPreferences sp = mContext.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		return sp.getInt(SPKey, 0);
	}

	public void ExitSys() {
		saveStrSP("currorder", "");
		saveStrSP("currlatlng", "");
		saveStrSP("curradd", "");
		saveStrSP("myadd", "");
		saveStrSP("mylocation", "");
		saveStrSP("city", "");
		// MobclickAgent.onKillProcess(mContext) ;
		((Activity) mContext).finish();
		System.exit(0);
	}
}
