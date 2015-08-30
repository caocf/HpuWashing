package com.edaixi.modle;

import java.io.Serializable;

public class OrderInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String order_id;
	private String order_sn;
	private String order_created_at;
	private String order_price;
	private String order_status;
	private String order_status_text;
	private String pay_status;
	private String pay_status_text;
	private String delivery_status_text;
	private String delivery_status;
	private String order_username;
	private String order_tel;
	private String city;
	private String yuyue_qujian_time;
	private String address_qu;
	private String area_qu;
	private String can_be_paid;
	private String can_be_canceled;
	private String coupon_paid;
	private String coupon_sn;
	private String yingfu;
	private String has_clothes_detail;
	private String good;
	private String applogo;
	private String total_score;
	private String washing_score;
	private String washing_time;
	private String washing_date;
	private String category_id;
	private String courier_qu;
	private String date_song;
	private String time_song;
	private String area_song;
	private String address_song;
	private String courier_name_qu;
	private String courier_phone_qu;
	private String courier_song;
	private String courier_name_song;
	private String courier_phone_song;
	private String user_type;
	private String small_amount;
	private String middle_amount;
	private String large_amount;
	private String package_amount;
	private String amount_text;
	private String pay_type;
	private String delivery_fee;
	private String remark;
	private String kehu_qianshou_time;
	private String logistics_score;
	private String service_score;
	private String order_comments;
	private String can_be_commented;
	private String exclusive_channels;
	private String order_can_share;
	private String share_url;
	private String share_title;
	private String share_content;
	private String share_image_url;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getOrder_created_at() {
		return order_created_at;
	}

	public void setOrder_created_at(String order_created_at) {
		this.order_created_at = order_created_at;
	}

	public String getOrder_price() {
		return order_price;
	}

	public void setOrder_price(String order_price) {
		this.order_price = order_price;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getOrder_status_text() {
		return order_status_text;
	}

	public void setOrder_status_text(String order_status_text) {
		this.order_status_text = order_status_text;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}

	public String getPay_status_text() {
		return pay_status_text;
	}

	public void setPay_status_text(String pay_status_text) {
		this.pay_status_text = pay_status_text;
	}

	public String getDelivery_status_text() {
		return delivery_status_text;
	}

	public void setDelivery_status_text(String delivery_status_text) {
		this.delivery_status_text = delivery_status_text;
	}

	public String getDelivery_status() {
		return delivery_status;
	}

	public void setDelivery_status(String delivery_status) {
		this.delivery_status = delivery_status;
	}

	public String getOrder_username() {
		return order_username;
	}

	public void setOrder_username(String order_username) {
		this.order_username = order_username;
	}

	public String getOrder_tel() {
		return order_tel;
	}

	public void setOrder_tel(String order_tel) {
		this.order_tel = order_tel;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getYuyue_qujian_time() {
		return yuyue_qujian_time;
	}

	public void setYuyue_qujian_time(String yuyue_qujian_time) {
		this.yuyue_qujian_time = yuyue_qujian_time;
	}

	public String getAddress_qu() {
		return address_qu;
	}

	public void setAddress_qu(String address_qu) {
		this.address_qu = address_qu;
	}

	public String getArea_qu() {
		return area_qu;
	}

	public void setArea_qu(String area_qu) {
		this.area_qu = area_qu;
	}

	public String getCan_be_paid() {
		return can_be_paid;
	}

	public void setCan_be_paid(String can_be_paid) {
		this.can_be_paid = can_be_paid;
	}

	public String getCan_be_canceled() {
		return can_be_canceled;
	}

	public void setCan_be_canceled(String can_be_canceled) {
		this.can_be_canceled = can_be_canceled;
	}

	public String getCoupon_paid() {
		return coupon_paid;
	}

	public void setCoupon_paid(String coupon_paid) {
		this.coupon_paid = coupon_paid;
	}

	public String getCoupon_sn() {
		return coupon_sn;
	}

	public void setCoupon_sn(String coupon_sn) {
		this.coupon_sn = coupon_sn;
	}

	public String getYingfu() {
		return yingfu;
	}

	public void setYingfu(String yingfu) {
		this.yingfu = yingfu;
	}

	public String getHas_clothes_detail() {
		return has_clothes_detail;
	}

	public void setHas_clothes_detail(String has_clothes_detail) {
		this.has_clothes_detail = has_clothes_detail;
	}

	public String getGood() {
		return good;
	}

	public void setGood(String good) {
		this.good = good;
	}

	public String getApplogo() {
		return applogo;
	}

	public void setApplogo(String applogo) {
		this.applogo = applogo;
	}

	public String getTotal_score() {
		return total_score;
	}

	public void setTotal_score(String total_score) {
		this.total_score = total_score;
	}

	public String getWashing_score() {
		return washing_score;
	}

	public void setWashing_score(String washing_score) {
		this.washing_score = washing_score;
	}

	public String getWashing_time() {
		return washing_time;
	}

	public void setWashing_time(String washing_time) {
		this.washing_time = washing_time;
	}

	public String getWashing_date() {
		return washing_date;
	}

	public void setWashing_date(String washing_date) {
		this.washing_date = washing_date;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCourier_qu() {
		return courier_qu;
	}

	public void setCourier_qu(String courier_qu) {
		this.courier_qu = courier_qu;
	}

	public String getDate_song() {
		return date_song;
	}

	public void setDate_song(String date_song) {
		this.date_song = date_song;
	}

	public String getTime_song() {
		return time_song;
	}

	public void setTime_song(String time_song) {
		this.time_song = time_song;
	}

	public String getArea_song() {
		return area_song;
	}

	public void setArea_song(String area_song) {
		this.area_song = area_song;
	}

	public String getAddress_song() {
		return address_song;
	}

	public void setAddress_song(String address_song) {
		this.address_song = address_song;
	}

	public String getCourier_name_qu() {
		return courier_name_qu;
	}

	public void setCourier_name_qu(String courier_name_qu) {
		this.courier_name_qu = courier_name_qu;
	}

	public String getCourier_phone_qu() {
		return courier_phone_qu;
	}

	public void setCourier_phone_qu(String courier_phone_qu) {
		this.courier_phone_qu = courier_phone_qu;
	}

	public String getCourier_song() {
		return courier_song;
	}

	public void setCourier_song(String courier_song) {
		this.courier_song = courier_song;
	}

	public String getCourier_name_song() {
		return courier_name_song;
	}

	public void setCourier_name_song(String courier_name_song) {
		this.courier_name_song = courier_name_song;
	}

	public String getCourier_phone_song() {
		return courier_phone_song;
	}

	public void setCourier_phone_song(String courier_phone_song) {
		this.courier_phone_song = courier_phone_song;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getSmall_amount() {
		return small_amount;
	}

	public void setSmall_amount(String small_amount) {
		this.small_amount = small_amount;
	}

	public String getMiddle_amount() {
		return middle_amount;
	}

	public void setMiddle_amount(String middle_amount) {
		this.middle_amount = middle_amount;
	}

	public String getLarge_amount() {
		return large_amount;
	}

	public void setLarge_amount(String large_amount) {
		this.large_amount = large_amount;
	}

	public String getPackage_amount() {
		return package_amount;
	}

	public void setPackage_amount(String package_amount) {
		this.package_amount = package_amount;
	}

	public String getAmount_text() {
		return amount_text;
	}

	public void setAmount_text(String amount_text) {
		this.amount_text = amount_text;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getDelivery_fee() {
		return delivery_fee;
	}

	public void setDelivery_fee(String delivery_fee) {
		this.delivery_fee = delivery_fee;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getKehu_qianshou_time() {
		return kehu_qianshou_time;
	}

	public void setKehu_qianshou_time(String kehu_qianshou_time) {
		this.kehu_qianshou_time = kehu_qianshou_time;
	}

	public String getLogistics_score() {
		return logistics_score;
	}

	public void setLogistics_score(String logistics_score) {
		this.logistics_score = logistics_score;
	}

	public String getService_score() {
		return service_score;
	}

	public void setService_score(String service_score) {
		this.service_score = service_score;
	}

	public String getOrder_comments() {
		return order_comments;
	}

	public void setOrder_comments(String order_comments) {
		this.order_comments = order_comments;
	}

	public String getCan_be_commented() {
		return can_be_commented;
	}

	public void setCan_be_commented(String can_be_commented) {
		this.can_be_commented = can_be_commented;
	}

	public String getExclusive_channels() {
		return exclusive_channels;
	}

	public void setExclusive_channels(String exclusive_channels) {
		this.exclusive_channels = exclusive_channels;
	}

	public String getOrder_can_share() {
		return order_can_share;
	}

	public void setOrder_can_share(String order_can_share) {
		this.order_can_share = order_can_share;
	}

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public String getShare_title() {
		return share_title;
	}

	public void setShare_title(String share_title) {
		this.share_title = share_title;
	}

	public String getShare_content() {
		return share_content;
	}

	public void setShare_content(String share_content) {
		this.share_content = share_content;
	}

	public String getShare_image_url() {
		return share_image_url;
	}

	public void setShare_image_url(String share_image_url) {
		this.share_image_url = share_image_url;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "OrderInfo [order_id=" + order_id + ", order_sn=" + order_sn
				+ ", order_created_at=" + order_created_at + ", order_price="
				+ order_price + ", order_status=" + order_status
				+ ", order_status_text=" + order_status_text + ", pay_status="
				+ pay_status + ", pay_status_text=" + pay_status_text
				+ ", delivery_status_text=" + delivery_status_text
				+ ", delivery_status=" + delivery_status + ", order_username="
				+ order_username + ", order_tel=" + order_tel + ", city="
				+ city + ", yuyue_qujian_time=" + yuyue_qujian_time
				+ ", address_qu=" + address_qu + ", area_qu=" + area_qu
				+ ", can_be_paid=" + can_be_paid + ", can_be_canceled="
				+ can_be_canceled + ", coupon_paid=" + coupon_paid
				+ ", coupon_sn=" + coupon_sn + ", yingfu=" + yingfu
				+ ", has_clothes_detail=" + has_clothes_detail + ", good="
				+ good + ", applogo=" + applogo + ", total_score="
				+ total_score + ", washing_score=" + washing_score
				+ ", washing_time=" + washing_time + ", washing_date="
				+ washing_date + ", category_id=" + category_id
				+ ", courier_qu=" + courier_qu + ", date_song=" + date_song
				+ ", time_song=" + time_song + ", area_song=" + area_song
				+ ", address_song=" + address_song + ", courier_name_qu="
				+ courier_name_qu + ", courier_phone_qu=" + courier_phone_qu
				+ ", courier_song=" + courier_song + ", courier_name_song="
				+ courier_name_song + ", courier_phone_song="
				+ courier_phone_song + ", user_type=" + user_type
				+ ", small_amount=" + small_amount + ", middle_amount="
				+ middle_amount + ", large_amount=" + large_amount
				+ ", package_amount=" + package_amount + ", amount_text="
				+ amount_text + ", pay_type=" + pay_type + ", delivery_fee="
				+ delivery_fee + ", remark=" + remark + ", kehu_qianshou_time="
				+ kehu_qianshou_time + ", logistics_score=" + logistics_score
				+ ", service_score=" + service_score + ", order_comments="
				+ order_comments + ", can_be_commented=" + can_be_commented
				+ ", exclusive_channels=" + exclusive_channels
				+ ", order_can_share=" + order_can_share + ", share_url="
				+ share_url + ", share_title=" + share_title
				+ ", share_content=" + share_content + ", share_image_url="
				+ share_image_url + "]";
	}

}