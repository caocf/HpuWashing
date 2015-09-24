package com.edaixi.modle;

public class CouponBean {

	private int id;
	private String sncode;
	private String order_sn;
	private boolean used;
	private long usetime;
	private int coupon_price;
	private String coupon_starttime;
	private String coupon_endtime;
	private String coupon_title;
	private String exclusive_channels;
	private String exclusive_channels_display;
	private String coupon_time_display;
	private int coupon_least_price;
	private String category_id;
	private String category_display;

	public CouponBean() {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getExclusive_channels_display() {
		return exclusive_channels_display;
	}

	public void setExclusive_channels_display(String exclusive_channels_display) {
		this.exclusive_channels_display = exclusive_channels_display;
	}

	public String getExclusive_channels() {
		return exclusive_channels;
	}

	public void setExclusive_channels(String exclusive_channels) {
		this.exclusive_channels = exclusive_channels;
	}

	public String getSncode() {
		return sncode;
	}

	public void setSncode(String sncode) {
		this.sncode = sncode;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public long getUsetime() {
		return usetime;
	}

	public void setUsetime(long usetime) {
		this.usetime = usetime;
	}

	public int getCoupon_price() {
		return coupon_price;
	}

	public void setCoupon_price(int coupon_price) {
		this.coupon_price = coupon_price;
	}

	public String getCoupon_starttime() {
		return coupon_starttime;
	}

	public void setCoupon_starttime(String coupon_starttime) {
		this.coupon_starttime = coupon_starttime;
	}

	public String getCoupon_endtime() {
		return coupon_endtime;
	}

	public void setCoupon_endtime(String coupon_endtime) {
		this.coupon_endtime = coupon_endtime;
	}

	public String getCoupon_title() {
		return coupon_title;
	}

	public void setCoupon_title(String coupon_title) {
		this.coupon_title = coupon_title;
	}

	public int getCoupon_least_price() {
		return coupon_least_price;
	}

	public void setCoupon_least_price(int coupon_least_price) {
		this.coupon_least_price = coupon_least_price;
	}

}
