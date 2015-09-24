package com.edaixi.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.edaixi.activity.R;
import com.edaixi.dataset.MyAddressDataSet;

/**
 * 我的常用地址页面列表适配器
 */
public class MyAddressListAdapter extends BasicAdapter {
	private MyAddressDataSet mDataSet = null;
	private boolean misorder = true;
	private boolean firstcome = true;
	private Handler handler;

	public MyAddressListAdapter(Context mContext, MyAddressDataSet mDataSet,
			boolean isorder, boolean firstcome, Handler handler, ListView list) {
		super(mContext);
		this.mDataSet = mDataSet;
		misorder = isorder;
		this.handler = handler;

	}

	public MyAddressListAdapter(Context mContext,
			MyAddressDataSet mListDataSet, boolean isorder, Handler handler) {
		super(mContext);
		this.mDataSet = mListDataSet;
		misorder = isorder;
	}

	@Override
	public int getCount() {
		return mDataSet.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataSet.getIndexBean(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder = null;
		final int _position = position;
		if (convertView == null) {// convertView 存放item的
			mViewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_mine_address, parent,
					false);
			mViewHolder.mName = (TextView) convertView
					.findViewById(R.id.address_item_name);
			mViewHolder.mPhone = (TextView) convertView
					.findViewById(R.id.address_item_tel);
			mViewHolder.mDetailAdd = (TextView) convertView
					.findViewById(R.id.address_item_text);
			convertView.setTag(mViewHolder);

		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		if (mDataSet.getIndexBean(_position).getTel() != null) {
			mViewHolder.mPhone.setText(mDataSet.getIndexBean(_position)
					.getTel());
		}
		if (mDataSet.getIndexBean(_position).getUsername() != null) {
			mViewHolder.mName.setText(mDataSet.getIndexBean(_position)
					.getUsername());
		}
		mViewHolder.mDetailAdd.setText(mDataSet.getIndexBean(_position)
				.getCity()
				+ mDataSet.getIndexBean(_position).getArea()
				+ mDataSet.getIndexBean(_position).getAddress().trim());

		return convertView;
	}

	protected void checkchange(int position) {
		mDataSet.getIndexBean(position).setChecked(true);
		firstcome = false;
	}

	private static class ViewHolder {
		TextView mName;
		TextView mPhone;
		TextView mDetailAdd;
	}

}
