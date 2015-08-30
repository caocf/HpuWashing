package com.edaixi.modle;

public class Userbean {
	private String user_id;
	private String userpassword;
	private String username;
	private String city;
	private String user_token;
	private String tel;
	private String province;
	String area;
	String address;

	public String getTel() {
		return tel;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getUsertoken() {
		return user_token;
	}

	public void setUsertoken(String usertoken) {
		this.user_token = usertoken;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	private String ts;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUserpassword() {
		return userpassword;
	}

	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}

	public String getUsername() {
		return username;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
