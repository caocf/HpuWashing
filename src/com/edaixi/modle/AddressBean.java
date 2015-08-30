package com.edaixi.modle;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AddressBean implements Serializable {
	private String address_id;
	private String username;
	private String address;
	private String address_line_1;
	private String address_line_2;
	private String gender;
	private String tel;
	private String city;
	private String area;
	private String province;
	private boolean frequently_address;
	private boolean is_service_place;
	private boolean can_Order_Select;

	public boolean isCan_Order_Select() {
		return can_Order_Select;
	}

	public void setCan_Order_Select(boolean can_Order_Select) {
		this.can_Order_Select = can_Order_Select;
	}

	public String getAddress_line_1() {
		return address_line_1;
	}

	public void setAddress_line_1(String address_line_1) {
		this.address_line_1 = address_line_1;
	}

	public String getAddress_line_2() {
		return address_line_2;
	}

	public void setAddress_line_2(String address_line_2) {
		this.address_line_2 = address_line_2;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean isIs_service_place() {
		return is_service_place;
	}

	public void setIs_service_place(boolean is_service_place) {
		this.is_service_place = is_service_place;
	}

	public String getUsername() {
		return username;
	}

	public boolean isFrequently_address() {
		return frequently_address;
	}

	public void setFrequently_address(boolean frequently_address) {
		this.frequently_address = frequently_address;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	private boolean checked;

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	private String order_place;

	public String getOrder_place() {
		return order_place;
	}

	public void setOrder_place(String order_place) {
		this.order_place = order_place;
	}

	public String getAddress() {
		return address;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public String toString() {
		return "AddressBean [address_id=" + address_id + ", username="
				+ username + ", address=" + address + ", address_line_1="
				+ address_line_1 + ", address_line_2=" + address_line_2
				+ ", gender=" + gender + ", tel=" + tel + ", city=" + city
				+ ", area=" + area + ", province=" + province
				+ ", frequently_address=" + frequently_address
				+ ", is_service_place=" + is_service_place + ", checked="
				+ checked + ", order_place=" + order_place + "]";
	}

}
