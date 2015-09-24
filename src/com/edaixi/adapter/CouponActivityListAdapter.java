package com.edaixi.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edaixi.Enum.CouponEntity;
import com.edaixi.activity.R;
import com.edaixi.data.AppConfig;
import com.edaixi.dataset.CouponsDataSet;

/**
 * 
 * @author wei-spring
 * 
 */
public class CouponActivityListAdapter extends BasicAdapter {

	private AppConfig mAppConfig = null;

	private CouponsDataSet mDataSet = null;

	private CouponEntity mBean = null;

	public CouponActivityListAdapter(Context mContext, CouponsDataSet mDataSet) {
		super(mContext);
		this.mDataSet = mDataSet;
		mAppConfig = AppConfig.getInstance();
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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_activity_coupon_list,
					parent, false);

			mViewHolder = new ViewHolder();

			mViewHolder.mMoney = (TextView) convertView
					.findViewById(R.id.item_activity_coupon_list_money);
			mViewHolder.mDes = (TextView) convertView
					.findViewById(R.id.item_activity_coupon_list_des);
			mViewHolder.mUseLimit = (TextView) convertView
					.findViewById(R.id.item_activity_coupon_list_uselimit);
			mViewHolder.mType = (TextView) convertView
					.findViewById(R.id.item_activity_coupon_list_type);
			mViewHolder.mLifeTime = (TextView) convertView
					.findViewById(R.id.item_activity_coupon_list_timeout);
			mViewHolder.mSelected = (ImageView) convertView
					.findViewById(R.id.item_activity_coupon_list_selected);
			mViewHolder.mTopBack = (LinearLayout) convertView
					.findViewById(R.id.item_activity_coupon_list_topback);
			mViewHolder.mRightLl = (LinearLayout) convertView
					.findViewById(R.id.ll_coupon_list_right);

			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		mBean = mDataSet.getIndexBean(position);
		if (mBean.isValid()) {
			mViewHolder.mTopBack
					.setBackgroundResource(R.drawable.coupon_bg_available_left);
			mViewHolder.mRightLl
					.setBackgroundResource(R.drawable.coupon_bg_available_right);
		} else {
			mViewHolder.mTopBack
					.setBackgroundResource(R.drawable.coupon_bg_vailable_left);
			mViewHolder.mRightLl
					.setBackgroundResource(R.drawable.coupon_bg_vailable_right);
		}

		if (mAppConfig.getCouponId() == mBean.getId()) {
			mViewHolder.mSelected.setVisibility(View.VISIBLE);
		} else {
			mViewHolder.mSelected.setVisibility(View.INVISIBLE);
		}

		mViewHolder.mMoney.setText(mBean.getCouponPrice() + "");
		TextPaint tpPaint = mViewHolder.mDes.getPaint();
		tpPaint.setFakeBoldText(true);
		mViewHolder.mDes.setText(mBean.getCoupon_title());

		if (mBean.getCouponLeastPrice() > 0) {
			mViewHolder.mUseLimit.setVisibility(View.VISIBLE);
			TextPaint tpPaintLimit = mViewHolder.mUseLimit.getPaint();
			tpPaintLimit.setFakeBoldText(true);
			mViewHolder.mUseLimit.setText(mBean.getDesStr());
		} else {
			mViewHolder.mUseLimit.setVisibility(View.GONE);
		}

		if (mBean.getExclusive_channels_display() != null
				&& !mBean.getExclusive_channels_display().contains("无支付限制")) {
			mViewHolder.mType.setVisibility(View.VISIBLE);
			TextPaint tpPaintChanel = mViewHolder.mType.getPaint();
			tpPaintChanel.setFakeBoldText(true);
			mViewHolder.mType.setText(mBean.getExclusive_channels_display());
		} else {
			mViewHolder.mType.setVisibility(View.GONE);
		}
		if ((mBean.getCouponLeastPrice() <= 0)
				&& mBean.getExclusive_channels_display().contains("无支付限制")) {
			mViewHolder.mType.setVisibility(View.VISIBLE);
			TextPaint tpPaintChanel = mViewHolder.mType.getPaint();
			tpPaintChanel.setFakeBoldText(true);
			mViewHolder.mType.setText("无限制");
		}
		if (mBean.getCoupon_time_display() != null) {
			TextPaint tpPaint6 = mViewHolder.mLifeTime.getPaint();
			tpPaint6.setFakeBoldText(true);
			mViewHolder.mLifeTime.setText(mBean.getCoupon_time_display());
		}
		return convertView;
	}

	private class ViewHolder {
		TextView mMoney;
		TextView mDes;
		TextView mUseLimit;
		TextView mType;
		TextView mLifeTime;
		ImageView mSelected;
		LinearLayout mTopBack;
		LinearLayout mRightLl;
	}

}
