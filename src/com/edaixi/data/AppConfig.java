package com.edaixi.data;

/**
 * 应用程序全局变量值
 */
public class AppConfig {

	private static AppConfig mAppConfig = null;

	public static AppConfig getInstance() {
		if (mAppConfig == null) {
			synchronized (AppConfig.class) {
				if (mAppConfig == null) {
					mAppConfig = new AppConfig();
				}
			}
		}
		return mAppConfig;
	}

	private String mUserId;
	private String mShareOrderId;
	private String mUserToken;
	private boolean islogin;
	private boolean isNewOrder;
	private boolean isopenlogin;
	private boolean isopendefaultcity;
	private boolean isOrderWxPay;
	private boolean isOrderZhifubaoPay;
	private boolean refresh_Address;
	private boolean locationFail;
	private String cityareastr;
	private String mCurrentCity;
	private String loctioncity;
	private String loctionaddress;
	private String loctionadcode;
	private String loctioncitycode;
	private String cancleOrderString;
	private String locationLatString;
	private String locationLogString;

	public String getLocationLatString() {
		return locationLatString;
	}

	public void setLocationLatString(String locationLatString) {
		this.locationLatString = locationLatString;
	}

	public String getLocationLogString() {
		return locationLogString;
	}

	public void setLocationLogString(String locationLogString) {
		this.locationLogString = locationLogString;
	}

	public boolean isNewOrder() {
		return isNewOrder;
	}

	public void setNewOrder(boolean isNewOrder) {
		this.isNewOrder = isNewOrder;
	}

	public boolean isOrderZhifubaoPay() {
		return isOrderZhifubaoPay;
	}

	public void setOrderZhifubaoPay(boolean isOrderZhifubaoPay) {
		this.isOrderZhifubaoPay = isOrderZhifubaoPay;
	}

	public boolean isLocationFail() {
		return locationFail;
	}

	public void setLocationFail(boolean locationFail) {
		this.locationFail = locationFail;
	}

	public String getmCurrentCity() {
		return mCurrentCity;
	}

	public void setmCurrentCity(String mCurrentCity) {
		this.mCurrentCity = mCurrentCity;
	}

	public String getCancleOrderString() {
		return cancleOrderString;
	}

	public void setCancleOrderString(String cancleOrderString) {
		this.cancleOrderString = cancleOrderString;
	}

	public boolean isIsopendefaultcity() {
		return isopendefaultcity;
	}

	public void setIsopendefaultcity(boolean isopendefaultcity) {
		this.isopendefaultcity = isopendefaultcity;
	}

	public static AppConfig getmAppConfig() {
		return mAppConfig;
	}

	public static void setmAppConfig(AppConfig mAppConfig) {
		AppConfig.mAppConfig = mAppConfig;
	}

	public String getmUserId() {
		return mUserId;
	}

	public void setmUserId(String mUserId) {
		this.mUserId = mUserId;
	}

	public String getmUserToken() {
		return mUserToken;
	}

	public void setmUserToken(String mUserToken) {
		this.mUserToken = mUserToken;
	}

	public boolean isRefresh_Address() {
		return refresh_Address;
	}

	public void setRefresh_Address(boolean refresh_Address) {
		this.refresh_Address = refresh_Address;
	}

	public int getmCouponId() {
		return mCouponId;
	}

	public void setmCouponId(int mCouponId) {
		this.mCouponId = mCouponId;
	}

	public String getLoctionadcode() {
		return loctionadcode;
	}

	public void setLoctionadcode(String loctionadcode) {
		this.loctionadcode = loctionadcode;
	}

	public String getLoctioncitycode() {
		return loctioncitycode;
	}

	public void setLoctioncitycode(String loctioncitycode) {
		this.loctioncitycode = loctioncitycode;
	}

	public String getLoctioncity() {
		return loctioncity;
	}

	public String getLoctionaddress() {
		return loctionaddress;
	}

	public void setLoctionaddress(String loctionaddress) {
		this.loctionaddress = loctionaddress;
	}

	public void setLoctioncity(String loctioncity) {
		this.loctioncity = loctioncity;
	}

	public static final String ALREADYLOGIN = "用户已经登陆";

	public String getmShareOrderId() {
		return mShareOrderId;
	}

	public void setmShareOrderId(String mShareOrderId) {
		this.mShareOrderId = mShareOrderId;
	}

	public boolean isIsopenlogin() {
		return isopenlogin;
	}

	public void setIsopenlogin(boolean isopenlogin) {
		this.isopenlogin = isopenlogin;
	}

	public boolean isOrderWxPay() {
		return isOrderWxPay;
	}

	public void setOrderWxPay(boolean isOrderWxPay) {
		this.isOrderWxPay = isOrderWxPay;
	}

	public String getCityareastr() {
		return cityareastr;
	}

	public void setCityareastr(String cityareastr) {
		this.cityareastr = cityareastr;
	}

	public boolean isIslogin() {
		return islogin;
	}

	public void setIslogin(boolean islogin) {
		this.islogin = islogin;
	}

	/* 当前使用的优惠券ID */
	private int mCouponId;

	private AppConfig() {
		mCouponId = -1;
	}

	public String getUserId() {
		return mUserId;
	}

	public void setUserId(String mUserId) {
		this.mUserId = mUserId;
	}

	public String getUserToken() {
		return mUserToken;
	}

	public void setUserToken(String mUserToken) {
		this.mUserToken = mUserToken;
	}

	public int getCouponId() {
		return mCouponId;
	}

	public void setCouponId(int mCouponId) {
		this.mCouponId = mCouponId;
	}

}
