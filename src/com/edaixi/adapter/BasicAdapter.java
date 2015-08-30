package com.edaixi.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * 基础适配器
 */
public abstract class BasicAdapter extends BaseAdapter {

	protected Context mContext;
	protected LayoutInflater mInflater = null;

	protected Resources mResources = null;

	public BasicAdapter(Context mContext) {
		this.mContext = mContext;
		mInflater = LayoutInflater.from(this.mContext);

		mResources = this.mContext.getResources();
	}

}
