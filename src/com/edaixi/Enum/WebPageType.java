package com.edaixi.Enum;

/**
 * 打开网页页面的操作类型枚举
 */
public enum WebPageType {

	/**
	 * 意见反馈
	 */
	FEEDBACK(0, "意见反馈", "http://t.cn/RZARXfh"),

	/**
	 * 常见问题
	 */
	COMMON_ISSUE(1, "常见问题", "http://www.edaixi.cn/pages/normal_question"),

	/**
	 * e袋洗微博
	 */
	E_WEIBO(2, "e袋洗微博", "http://m.weibo.cn/d/ewashing"),

	/**
	 * 价格表
	 */
	PRICE_LIST(
			3,
			"价格表",
			"http://wx.edaixi.cn/mobile.php?act=module&from_user=LSEUa4APd5&name=washing&do=price&weid=5"),

	/**
	 * 服务范围
	 */
	SERVER_RANGE(4, "服务范围", "http://www.edaixi.cn/pages/service_area"),

	/**
	 * 官网
	 */
	WEBSITE(5, "官方网站", "http://www.edaixi.com/"),

	USERPRTOCOL(6, "用户协议", "http://www.edaixi.cn/pages/user_agreement"), COUPON_INFO(
			7, "优惠券使用说明", "http://www.edaixi.cn/pages/coupon_des");
	private int mType;
	private String mTitle;
	private String mUrl;

	private WebPageType(int mType, String mTitle, String mUrl) {
		this.mType = mType;
		this.mTitle = mTitle;
		this.mUrl = mUrl;
	}

	public int getType() {
		return mType;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getUrl() {
		return mUrl;
	}

	/**
	 * 将指定索引值转换为对应的枚举值
	 */
	public static WebPageType ofWebPageType(int mIndex) {
		WebPageType mResult = FEEDBACK;

		for (WebPageType mode : values()) {
			if (mode.getType() == mIndex) {
				mResult = mode;
				break;
			}
		}
		return mResult;
	}

}
