package com.edaixi.modle;

public class AddressInfo {

	public final String mOrderName;
	public final String mOrderTel;
	public final String mOrderProvince;
	public final String mOrderCity;
	public final String mOrderArea;
	public final String mOrderAddress;

	public AddressInfo(String name, String tel, String province, String city,
			String area, String address) {
		mOrderArea = area;
		mOrderAddress = address;
		mOrderName = name;
		mOrderTel = tel;
		mOrderProvince = province;
		mOrderCity = city;
	}

	public String getmOrderName() {
		return mOrderName;
	}

	public String getmOrderTel() {
		return mOrderTel;
	}

	public String getmOrderProvince() {
		return mOrderProvince;
	}

	public String getmOrderCity() {
		return mOrderCity;
	}

	public String getmOrderArea() {
		return mOrderArea;
	}

	public String getmOrderAddress() {
		return mOrderAddress;
	}

	@Override
	public String toString() {
		return "AddressInfo [mOrderName=" + mOrderName + ", mOrderTel="
				+ mOrderTel + ", mOrderProvince=" + mOrderProvince
				+ ", mOrderCity=" + mOrderCity + ", mOrderArea=" + mOrderArea
				+ ", mOrderAddress=" + mOrderAddress + "]";
	}

}
