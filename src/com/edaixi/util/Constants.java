package com.edaixi.util;

public class Constants {

	/** 微信支付，分享关键key,id **/
	public static final String WXAPP_ID = "wx98a4962c2ef2e5a6";
	public static final String WXAPP_SECRET = "9d46a45c433db35faa15ce02a6d273af";
	public static final String PARTNER_ID = "1229155401";
	public static final String PARTNER_KEY = "93070942c85088fe67bcb06fd2d462ce";
	public static final String PaySignKey = "xsdIJuZ0oB33w3OKJA1brWIDLhzxDhFIs3WfacFkmBlX1hubjKtK36DKVeBzKHiq5qmmVPewe30FZ6ih5FkoQgmMeady3MfJTIuKsz2079kAVNg4eKndyh2K65ozfvhl";
	public static final String TXAPP_ID = "1104311868";
	public static final String TXAPP_KEY = "FbNAt08Yj5fRb6jE";
	public static final String SINAWEIBO_ID = "3983016986";
	public static final String SINAWEIBO_KEY = "67eb4dd0c59d2fcdf7743b8d4a1464";

	/** greenDao 数据缓存一些常量 **/
	public static final String GreenDao_OpenCitys = "green_dao_open_citys";

	/** 全局请求接口URL常量 **/

	// 获取优惠券
	public static final int ADSREQUEST = 0;
	// 获取优惠券成功
	public static final int ADSREQUSESUCCED = 1;
	// 获取优惠券成功
	public static final int ADSREQUSEFAILD = -1;

	public static String ENCODE = "UTF-8";

	public String key = "FO5Z6BIWV9";
	// avoskey
	public static String avoskey = "6iebvdj6vkz13nxmjf3fzniw5h37prbv0bclvrlp67i9dhsx";
	// avosvalue
	public String avosvalue = "doe8qr2p3g0x7r8ldi2judtyvsdro2jfnwkygenh251yla7a";

	// 测试环境图片V2
	// public final static String APP_IMG_URL =
	// "http://open03.edaixi.cn/client/v2/";
	// 测试环境图片V3
	public final static String APP_IMG_URL = "http://open.edaixi.com/client/v3/";
	// public final static String APP_IMG_URL =
	// "http://open10.edaixi.cn:81/client/v3/";
	// public final static String APP_IMG_URL =
	// "http://open05.edaixi.cn:81/client/v3/";
	// 外部测试环境
	public final static String APP_API_URL = "http://open.edaixi.com/client/v1/";
	// public final static String APP_API_URL =
	// "http://open10.edaixi.cn:81/client/v1/";
	// public final static String APP_API_URL =
	// "http://open05.edaixi.cn:81/client/v1/";
	// 测试环境
	// public final static String APP_API_URL =
	// "http://192.168.0.77:3007/client/v1/";
	// 生产环境
	// public final static String APP_API_URL =
	// "http://open03.edaixi.cn/client/v1/";
	// 登陆
	public final static String getlogin = APP_API_URL + "bind_user?";
	// 获取验证码
	public final static String sendsms = APP_API_URL + "send_sms?";
	// 获取用户地址
	public final static String getaddress = APP_API_URL + "get_address_list?";
	// 获取优惠券
	public final static String getcoupons = APP_API_URL + "get_coupons?";
	// 订单是否分享红包回调
	public final static String getorderisshare = APP_API_URL
			+ "order_envelope_is_share?";
	// 获取订单列表
	public final static String getoerderlist = APP_API_URL + "get_order_list?";
	// 获取订单信息
	public final static String getorder = APP_API_URL + "get_order?";
	// 获取会员卡信息
	public final static String geticard = APP_API_URL + "get_icard?";
	// 获取订单衣物明细
	public final static String getorderclothing = APP_API_URL
			+ "order_clothing?";
	// 获取订单物流信息
	public final static String getorderdeliverystatuslist = APP_API_URL
			+ "order_delivery_status_list?";
	// 获取服务范围
	public final static String getcitiesoptions = APP_API_URL
			+ "cities_options?";

	// 获取必要的zip包的接口
	public static final String getAppStatusList = APP_API_URL
			+ "app_status_list?";

	// 兑换优惠券
	public final static String getbindcoupon = APP_API_URL + "bind_coupon?";
	// 优惠券过期天数
	public final static String getcouponoutdated = APP_API_URL
			+ "get_coupon_outdated_time";
	// 付款
	public final static String getpayorder2 = APP_API_URL + "pay_order2?";
	// 会员卡充值
	public final static String geticardrecharge = APP_API_URL
			+ "icard_recharge?";
	// 取消订单
	public final static String getcancelorder = APP_API_URL + "cancel_order?";
	// 取消订单原因
	public final static String getcancelorderreasons = APP_API_URL
			+ "order_cancel_reasons?";
	// 删除地址
	public final static String getdeleteaddress = APP_API_URL
			+ "delete_address?";
	// 创建订单
	public final static String getcreateorder = APP_API_URL + "create_order?";
	// 创建订单评论
	public final static String getcreateordercomment = APP_API_URL
			+ "create_order_comment?";
	// 获取城市列表
	public final static String getcitylist = APP_API_URL + "cities?";
	// 创建地址
	public final static String getcreataddress = APP_API_URL
			+ "create_address?";
	// 更新地址
	public final static String getupdateaddress = APP_API_URL
			+ "update_address?";
	// 判断地址是否在服务范围内
	public final static String getverifyaddress = APP_API_URL
			+ "verify_address?";
	// 绑定会员卡?
	public final static String getbindmembercard = APP_API_URL
			+ "bind_member_card?";
	// 充值卡充值?
	public final static String getbindrecharge = APP_API_URL + "bind_recharge?";
	// 应用程序名，用于创建缓存根目录
	public static final String APP_NAME = "eWashing";
	// 下载更新Apk地址
	public static final String UPDATE = "com.edaixi.predownfile";
	// 预下载文件完成广播
	public static final String PREDOWN_FILE_BROADCAST = "com.edaixi.predownfile";
	// 支付宝测试回调地址
	// public static final String ALIPAY_NOTIFY_URL =
	// "http://payment03.edaixi.cn/payment/alipay/notify_url.php";
	// 支付宝线上回调地址
	// public static final String ALIPAY_NOTIFY_URL =
	// "http://wx.rongchain.com/payment/alipay/notify_url.php";
	// 支付宝新线上回调地址
	public static final String ALIPAY_NOTIFY_URL = "http://payment.edaixi.com/payment/ali_app_notify";
	// public static final String ALIPAY_NOTIFY_URL =
	// "http://wx01.edaixi.cn/payment/alipay/notify_url.php";
	// 微信回调地址
	// public static final String WECHAT_NOTIFY_URL =
	// "http://wx.rongchain.com/payment/wechat/open_notify_android.php";
	// 微信新回调地址
	public static final String WECHAT_NOTIFY_URL = "http://payment.edaixi.com/payment/wechat_android_notify";
	// 微信测试回调地址
	// public static final String WECHAT_NOTIFY_URL =
	// "http://payment03.edaixi.cn/payment/wechat_android_notify";
	// 获取banner地址
	public static final String GET_BANNER_LIST = APP_IMG_URL
			+ "get_banner_list?";
	// 获取func_button_list地址
	public static final String GET_BANNER_BUTTON_LIST = APP_IMG_URL
			+ "get_banner_button_list?";
	// 获取func_button_list地址
	public static final String GET_FUNC_BUTTON_LIST = APP_IMG_URL
			+ "get_func_button_list?";
	// 充值页面充值信息
	public final static String getrecharge_settings = APP_API_URL
			+ "recharge_settings?";
	// 充值页面图片
	public final static String getrecharge_settings_pic = APP_API_URL
			+ "recharge_settings_pic?";

	// 获取各城市运费
	public static final String GET_CITY_DELIVERY_FEE = APP_API_URL
			+ "get_city_delivery_fee?";
	// 获取启动页面图片
	public static final String GET_OPEN_PIC = APP_IMG_URL + "get_ads_list?";
}
