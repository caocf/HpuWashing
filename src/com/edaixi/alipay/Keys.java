/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.edaixi.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	// 合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088512143837202";

	// 收款支付宝账号
	public static final String DEFAULT_SELLER = "2088512143837202";

	// 商户私钥，自助生成
	public static final String PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMGTquCePpScZw0J+UNFmhT2jsz05IkxOgfwsRZgBs/HwUhtWYIa3Y/JSMhpni8tIOD0fa6QsxAWclZ+JjpRRx6yZvOG3/15am3DXKm8uAB4TpmrarOMUGtX6cL+Mjgno0p8rbU0+C8NqaHxJGDRFxIXNGHfOK8CTkldNL6EfFHdAgMBAAECgYBcaxzk+ogUOYu1nPJlnMBQi9pnne/SVC9JhS926Ee4Qb1Uz+gxBpSLRmU6UbqU+W2+GNw8UJc88gqKotWreAyEk7/AvOxgPTEVXL/FI+baDbi9xF3/acodv77yjOOBGNIuAY6oxVyW3a6vRiMexCkLpYTJCtCPB9gIjLkMigGW4QJBAP+G4sY0Um7Ydqd8Na9TzDE7H+mK5Yb4/jCihGnYjTwvZck7h6hYoDV9r4DN/UAEVldX8w6O/6QuqzF3K4HObnkCQQDB72suLtIlRdymfdbRP0WgY+HadEooHjLTmwo3pnAAAMsaL/4k035+tzWpb1gcIeVVN1Hc7ju7LKoidzVvVRWFAkEA65poQA6ALzohdV4+dbFTJdV5IDH0XlX4sck3RAzqdKLTPA9KjrtgxNFlX+MObddR8Ojj7/mD1tM8/7f8goxfKQJAPl+YYzQf1mkPvGSAsK/e3uUBANeh+iEsS65zrM5U+0tGB1gkLwfuiSa3lZUAC5xDPBMcuyfMAjktRwR8JSCCWQJBAIT4094BpLf+0Qma0/jaswq574RIj7HP/CFqfYjqzlR0zX08zMpw85IuAAPyFkq/vBUyR6HE/OnrRMBTWDV2IlM=";

	// 支付宝公钥
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

}
