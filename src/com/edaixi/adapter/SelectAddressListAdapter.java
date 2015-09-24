package com.edaixi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.edaixi.activity.R;
import com.edaixi.dataset.MyAddressDataSet;

public class SelectAddressListAdapter extends BasicAdapter {
	private MyAddressDataSet mDataSet = null;
	private boolean isAddressService = true;
	private Context context;

	public SelectAddressListAdapter(Context mContext,
			MyAddressDataSet mDataSet, boolean isAddressService) {
		super(mContext);
		this.mDataSet = mDataSet;
		this.isAddressService = isAddressService;
		this.context = mContext;
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
			convertView = mInflater.inflate(R.layout.item_address_select,
					parent, false);
			mViewHolder.mName = (TextView) convertView
					.findViewById(R.id.address_item_name);
			mViewHolder.mPhone = (TextView) convertView
					.findViewById(R.id.address_item_tel);
			mViewHolder.mDetailAdd = (TextView) convertView
					.findViewById(R.id.address_item_text);
			mViewHolder.mAddLayout = (RelativeLayout) convertView
					.findViewById(R.id.rl_address_select);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		if (isAddressService) {
			mViewHolder.mName.setTextColor(context.getResources().getColor(
					R.color.text_1));
			mViewHolder.mName.setText(mDataSet.getIndexBean(_position)
					.getUsername());

			if (mDataSet.getIndexBean(_position).getTel() != null) {
				mViewHolder.mPhone.setTextColor(context.getResources()
						.getColor(R.color.text_1));
				mViewHolder.mPhone.setText(mDataSet.getIndexBean(_position)
						.getTel());
			}
			mViewHolder.mDetailAdd.setTextColor(context.getResources()
					.getColor(R.color.text_1));
			if(mDataSet.getIndexBean(_position).getAddress() != null)
			mViewHolder.mDetailAdd.setText(mDataSet.getIndexBean(_position)
					.getCity()
					+ mDataSet.getIndexBean(_position).getArea()
					+ mDataSet.getIndexBean(_position).getAddress().trim());
		} else {
			mViewHolder.mName.setTextColor(context.getResources().getColor(
					R.color.falseaddress));
			mViewHolder.mName.setText(mDataSet.getIndexBean(_position)
					.getUsername());

			if (mDataSet.getIndexBean(_position).getTel() != null) {
				mViewHolder.mPhone.setTextColor(context.getResources()
						.getColor(R.color.falseaddress));
				mViewHolder.mPhone.setText(mDataSet.getIndexBean(_position)
						.getTel());
			}
			mViewHolder.mDetailAdd.setTextColor(context.getResources()
					.getColor(R.color.falseaddress));
			mViewHolder.mDetailAdd.setText(mDataSet.getIndexBean(_position)
					.getCity()
					+ mDataSet.getIndexBean(_position).getArea()
					+ mDataSet.getIndexBean(_position).getAddress().trim());
		}
		return convertView;
	}

	private static class ViewHolder {
		private TextView mName;
		private TextView mPhone;
		private TextView mDetailAdd;
		private RelativeLayout mAddLayout;
	}
}
