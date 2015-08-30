package com.edaixi.modle;

import java.io.Serializable;

import android.text.TextUtils;

/**
 * 我的常用地址列表接口中列表单条数据bean
 */
public class MyAddressBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2263644634557839827L;

	private int address_id;
	private String username;
	private String tel;
	private String province;
	private String city;
	private String area;
	private String address;

	public MyAddressBean() {
	}

	public int getAddress_id() {
		return address_id;
	}

	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 构建详细地址字符串
	 * 
	 * @return
	 */
	public String buildDetailAdd() {
		StringBuilder mSB = new StringBuilder();
		if (!TextUtils.isEmpty(province)) {
			mSB.append(province).append("省");
		}
		if (!TextUtils.isEmpty(city)) {
			mSB.append(city).append("市");
		}
		if (!TextUtils.isEmpty(area)) {
			mSB.append(area).append("区");
		}
		if (!TextUtils.isEmpty(address)) {
			mSB.append(address);
		}

		return mSB.toString();
	}

}
