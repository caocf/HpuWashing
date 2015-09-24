package com.edaixi.Enum;

import java.io.Serializable;
import java.util.Locale;

import com.edaixi.modle.CouponBean;
import com.edaixi.util.Changetime;

/**
 * 优惠券实体类
 */
public class CouponEntity implements Serializable {

	private static final long serialVersionUID = -6281049328459271460L;
	private static final String UNLIMITED = "无限制";
	private static final String NORMAL_DES_FORMAT = "满%d可用";
	private int mId;
	private String mSnCode;
	private boolean mUsed;
	private long mUseTime;
	private int mCouponPrice;
	private String mCouponStartTime;
	private String exclusive_channels;
	private String exclusive_channels_display;
	private String mCouponEndTime;
	private String coupon_title;
	private String coupon_time_display;
	private String category_id;
	private String category_display;
	private String coupon_endtime;
	private int mCouponLeastPrice;
	private boolean mValid;

	public CouponEntity(CouponBean mBean) {
		mId = mBean.getId();
		mSnCode = mBean.getSncode();
		mUsed = mBean.isUsed();
		mUseTime = mBean.getUsetime();
		coupon_title = mBean.getCoupon_title();
		mCouponPrice = mBean.getCoupon_price();
		mCouponStartTime = mBean.getCoupon_starttime();
		mCouponEndTime = mBean.getCoupon_endtime();
		mCouponLeastPrice = mBean.getCoupon_least_price();
		exclusive_channels = mBean.getExclusive_channels();
		exclusive_channels_display = mBean.getExclusive_channels_display();
		coupon_time_display = mBean.getCoupon_time_display();
		category_id = mBean.getCategory_id();
		category_display = mBean.getCategory_display();
		coupon_endtime = mBean.getCoupon_endtime();
		mValid = true;
	}

	public String getCoupon_endtime() {
		return coupon_endtime;
	}

	public void setCoupon_endtime(String coupon_endtime) {
		this.coupon_endtime = coupon_endtime;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCategory_display() {
		return category_display;
	}

	public void setCategory_display(String category_display) {
		this.category_display = category_display;
	}

	public String getCoupon_time_display() {
		return coupon_time_display;
	}

	public void setCoupon_time_display(String coupon_time_display) {
		this.coupon_time_display = coupon_time_display;
	}

	public String getExclusive_channels_display() {
		return exclusive_channels_display;
	}

	public void setExclusive_channels_display(String exclusive_channels_display) {
		this.exclusive_channels_display = exclusive_channels_display;
	}

	public String getCoupon_title() {
		return coupon_title;
	}

	public void setCoupon_title(String coupon_title) {
		this.coupon_title = coupon_title;
	}

	public String getExclusive_channels() {
		return exclusive_channels;
	}

	public void setExclusive_channels(String exclusive_channels) {
		this.exclusive_channels = exclusive_channels;
	}

	public int getId() {
		return mId;
	}

	public void setId(int mId) {
		this.mId = mId;
	}

	public String getSnCode() {
		return mSnCode;
	}

	public void setSnCode(String mSnCode) {
		this.mSnCode = mSnCode;
	}

	public boolean isUsed() {
		return mUsed;
	}

	public void setUsed(boolean mUsed) {
		this.mUsed = mUsed;
	}

	public long getUseTime() {
		return mUseTime;
	}

	public void setUseTime(long mUseTime) {
		this.mUseTime = mUseTime;
	}

	public int getCouponPrice() {
		return mCouponPrice;
	}

	public void setCouponPrice(int mCouponPrice) {
		this.mCouponPrice = mCouponPrice;
	}

	public String getCouponStartTime() {
		return mCouponStartTime;
	}

	public void setCouponStartTime(String mCouponStartTime) {
		this.mCouponStartTime = mCouponStartTime;
	}

	public String getCouponEndTime() {
		return mCouponEndTime;
	}

	public void setCouponEndTime(String mCouponEndTime) {
		this.mCouponEndTime = mCouponEndTime;
	}

	public int getCouponLeastPrice() {
		return mCouponLeastPrice;
	}

	public void setCouponLeastPrice(int mCouponLeastPrice) {
		this.mCouponLeastPrice = mCouponLeastPrice;
	}

	public boolean isValid() {
		return mValid;
	}

	/**
	 * 根据当前的消费额确定此优惠券是否有效
	 * 
	 * @param mMoney
	 */
	public void setValid(double mMoney) {
		boolean istimeout = Changetime.changetime(getCouponStartTime());
		mValid = (mMoney >= mCouponLeastPrice) && istimeout;
	}

	/**
	 * 获取此优惠券的描述字符串
	 */
	public String getDesStr() {
		if (mCouponLeastPrice == 0) {
			return UNLIMITED;
		}
		return String.format(Locale.getDefault(), NORMAL_DES_FORMAT,
				mCouponLeastPrice);
	}

	@Override
	public String toString() {
		return "CouponEntity [mId=" + mId + ", mSnCode=" + mSnCode + ", mUsed="
				+ mUsed + ", mUseTime=" + mUseTime + ", mCouponPrice="
				+ mCouponPrice + ", mCouponStartTime=" + mCouponStartTime
				+ ", exclusive_channels=" + exclusive_channels
				+ ", exclusive_channels_display=" + exclusive_channels_display
				+ ", mCouponEndTime=" + mCouponEndTime + ", coupon_title="
				+ coupon_title + ", coupon_time_display=" + coupon_time_display
				+ ", category_id=" + category_id + ", category_display="
				+ category_display + ", coupon_endtime=" + coupon_endtime
				+ ", mCouponLeastPrice=" + mCouponLeastPrice + ", mValid="
				+ mValid +getDesStr()+"]";
	}

	
}
