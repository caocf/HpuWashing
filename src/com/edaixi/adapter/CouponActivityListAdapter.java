package com.edaixi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.edaixi.Enum.CouponEntity;
import com.edaixi.activity.R;
import com.edaixi.data.AppConfig;
import com.edaixi.dataset.CouponsDataSet;
import com.edaixi.util.DateUtil;

/**
 * 
 * @author wei-spring
 * 
 */
@SuppressLint("ResourceAsColor")
public class CouponActivityListAdapter extends BasicAdapter {

	private AppConfig mAppConfig = null;

	private CouponsDataSet mDataSet = null;

	private CouponEntity mBean = null;

	private Context mContext;

	public CouponActivityListAdapter(Context mContext, CouponsDataSet mDataSet) {
		super(mContext);
		this.mDataSet = mDataSet;
		mAppConfig = AppConfig.getInstance();
		this.mContext = mContext;
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

			mViewHolder.mCatagory = (TextView) convertView
					.findViewById(R.id.tv_coupon_title);
			mViewHolder.mMoney = (TextView) convertView
					.findViewById(R.id.item_activity_coupon_list_money);
			mViewHolder.mDes = (TextView) convertView
					.findViewById(R.id.item_activity_coupon_list_des);
			mViewHolder.mRmbIcon = (TextView) convertView
					.findViewById(R.id.item_activity_coupon_list_rmb);
			mViewHolder.mUseLimit = (TextView) convertView
					.findViewById(R.id.item_activity_coupon_list_uselimit);
			mViewHolder.mLifeTime = (TextView) convertView
					.findViewById(R.id.item_activity_coupon_list_timeout);
			mViewHolder.mSelected = (ImageView) convertView
					.findViewById(R.id.item_activity_coupon_list_selected);
			mViewHolder.iv_coupon_right_bg = (ImageView) convertView
					.findViewById(R.id.iv_coupon_right_bg);
			mViewHolder.mRlLeft = (RelativeLayout) convertView
					.findViewById(R.id.rl_coupon_list_left);
			mViewHolder.mFlRight = (FrameLayout) convertView
					.findViewById(R.id.fl_coupon_list_right);

			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		mBean = mDataSet.getIndexBean(position);
		// 设置公用的字段填充数据
		mViewHolder.mCatagory.setText(mBean.getCategory_display());
		mViewHolder.mMoney.setText(mBean.getCouponPrice() + "");
		mViewHolder.mDes.setText(mBean.getCoupon_title());
		if (mBean.getCoupon_time_display() != null) {
			if (DateUtil.getCouponDate(mBean.getCoupon_endtime()).contains("(")) {
				mViewHolder.mLifeTime.setTextColor(R.color.red);
				mViewHolder.mLifeTime.setText(DateUtil.getCouponDate(mBean
						.getCoupon_endtime()));
			} else {
				mViewHolder.mLifeTime.setTextColor(R.color.text_1);
				mViewHolder.mLifeTime.setText(mBean.getCoupon_time_display());
			}
		}

		if (mBean.getDesStr() == "无限制"
				&& mBean.getExclusive_channels_display().contains("无支付限制")) {
			mViewHolder.mUseLimit.setText("无支付限制");
		} else {
			if (mBean.getDesStr() != "无限制") {
				mViewHolder.mUseLimit.setText(Html
						.fromHtml("<font size=\"3\" color=\"#19a3f1\">"
								+ mBean.getDesStr()
								+ "</font><font size=\"3\" color=\"#3e3e3e\">"
								+ "  " + mBean.getExclusive_channels_display()
								+ "</font>"));
			} else {
				mViewHolder.mUseLimit.setText(mBean
						.getExclusive_channels_display());
			}
		}

		if (mBean.isValid()) {
			// 优惠券可以使用背景以及其他条目的数据填充
			switch (mBean.getCategory_id()) {
			case "1":
				// 洗衣
				mViewHolder.mRlLeft
						.setBackgroundResource(R.drawable.coupon_bg_available_left_1);
				mViewHolder.mFlRight
						.setBackgroundResource(R.drawable.coupon_bg_available_right);
				mViewHolder.iv_coupon_right_bg
						.setBackgroundResource(R.drawable.coupon_right_bg_1);
				mViewHolder.mCatagory.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_washing));
				mViewHolder.mRmbIcon.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_washing));
				mViewHolder.mMoney.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_washing));
				break;
			case "2":
				// 洗鞋
				mViewHolder.mRlLeft
						.setBackgroundResource(R.drawable.coupon_bg_available_left_2);
				mViewHolder.mFlRight
						.setBackgroundResource(R.drawable.coupon_bg_available_right);
				mViewHolder.iv_coupon_right_bg
						.setBackgroundResource(R.drawable.coupon_right_bg_2);
				mViewHolder.mCatagory.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_shoes));
				mViewHolder.mRmbIcon.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_shoes));
				mViewHolder.mMoney.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_shoes));
				break;
			case "3":
				// 窗帘
				mViewHolder.mRlLeft
						.setBackgroundResource(R.drawable.coupon_bg_available_left_3);
				mViewHolder.mFlRight
						.setBackgroundResource(R.drawable.coupon_bg_available_right);
				mViewHolder.iv_coupon_right_bg
						.setBackgroundResource(R.drawable.coupon_right_bg_3);
				mViewHolder.mCatagory.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_window));
				mViewHolder.mRmbIcon.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_window));
				mViewHolder.mMoney.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_window));
				break;
			case "4":
				// 高端衣物
				mViewHolder.mRlLeft
						.setBackgroundResource(R.drawable.coupon_bg_available_left_4);
				mViewHolder.mFlRight
						.setBackgroundResource(R.drawable.coupon_bg_available_right);
				mViewHolder.iv_coupon_right_bg
						.setBackgroundResource(R.drawable.coupon_right_bg_4);
				mViewHolder.mCatagory.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_gaoduanyiwu));
				mViewHolder.mRmbIcon.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_gaoduanyiwu));
				mViewHolder.mMoney.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_gaoduanyiwu));
				break;
			case "5":
				// 奢侈品
				mViewHolder.mRlLeft
						.setBackgroundResource(R.drawable.coupon_bg_available_left_5);
				mViewHolder.mFlRight
						.setBackgroundResource(R.drawable.coupon_bg_available_right);
				mViewHolder.iv_coupon_right_bg
						.setBackgroundResource(R.drawable.coupon_right_bg_5);
				mViewHolder.mCatagory.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_shechipin));
				mViewHolder.mRmbIcon.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_shechipin));
				mViewHolder.mMoney.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_shechipin));
				break;

			default:
				// 通用
				mViewHolder.mRlLeft
						.setBackgroundResource(R.drawable.coupon_bg_available_left_0);
				mViewHolder.mFlRight
						.setBackgroundResource(R.drawable.coupon_bg_available_right);
				mViewHolder.iv_coupon_right_bg
						.setBackgroundResource(R.drawable.coupon_right_bg_0);
				mViewHolder.mCatagory.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_tongyong));
				mViewHolder.mRmbIcon.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_tongyong));
				mViewHolder.mMoney.setTextColor(mContext.getResources()
						.getColor(R.color.coupon_item_tongyong));
				break;
			}
		} else {
			// 优惠券不可以使用背景已经其他数据条目的填充
			mViewHolder.mRlLeft
					.setBackgroundResource(R.drawable.coupon_bg_available_left_00);
			mViewHolder.mFlRight
					.setBackgroundResource(R.drawable.coupon_bg_vailable_right);
			mViewHolder.iv_coupon_right_bg
					.setBackgroundResource(R.drawable.coupon_right_bg_00);
			mViewHolder.mCatagory.setTextColor(mContext.getResources()
					.getColor(R.color.coupon_item_bukeyong));
			mViewHolder.mRmbIcon.setTextColor(mContext.getResources().getColor(
					R.color.coupon_item_bukeyong));
			mViewHolder.mMoney.setTextColor(mContext.getResources().getColor(
					R.color.coupon_item_bukeyong));
		}

		if (mAppConfig.getCouponId() == mBean.getId()) {
			mViewHolder.mSelected.setVisibility(View.VISIBLE);
		} else {
			mViewHolder.mSelected.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView mMoney;
		TextView mDes;
		TextView mUseLimit;
		TextView mLifeTime;
		TextView mCatagory;
		TextView mRmbIcon;
		ImageView mSelected;
		ImageView iv_coupon_right_bg;
		RelativeLayout mRlLeft;
		FrameLayout mFlRight;
	}
}