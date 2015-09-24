package com.edaixi.modle;

public class BaiDuPayOrderInfo {
	public String service_code;
	public String sp_no;
	public String sp_uno;
	public String sp_request_type;
	public String order_create_time;
	public String order_no;
	public String goods_category;
	public String goods_channel_sp;
	public String goods_channel;
	public String goods_name;
	public String goods_desc;
	public String goods_url;
	public String unit_amount;
	public String unit_count;
	public String transport_amount;
	public String total_amount;
	public String Currency;
	public String buyer_sp_username;
	public String return_url;
	public String page_url;
	public String pay_type;
	public String expire_time;
	public String input_charset;
	public String Version;
	public String Sign;
	public String sign_method;
	public String extra;

	public String getService_code() {
		return service_code;
	}

	public void setService_code(String service_code) {
		this.service_code = service_code;
	}

	public String getSp_no() {
		return sp_no;
	}

	public void setSp_no(String sp_no) {
		this.sp_no = sp_no;
	}

	public String getSp_uno() {
		return sp_uno;
	}

	public void setSp_uno(String sp_uno) {
		this.sp_uno = sp_uno;
	}

	public String getSp_request_type() {
		return sp_request_type;
	}

	public void setSp_request_type(String sp_request_type) {
		this.sp_request_type = sp_request_type;
	}

	public String getOrder_create_time() {
		return order_create_time;
	}

	public void setOrder_create_time(String order_create_time) {
		this.order_create_time = order_create_time;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getGoods_category() {
		return goods_category;
	}

	public void setGoods_category(String goods_category) {
		this.goods_category = goods_category;
	}

	public String getGoods_channel_sp() {
		return goods_channel_sp;
	}

	public void setGoods_channel_sp(String goods_channel_sp) {
		this.goods_channel_sp = goods_channel_sp;
	}

	public String getGoods_channel() {
		return goods_channel;
	}

	public void setGoods_channel(String goods_channel) {
		this.goods_channel = goods_channel;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_desc() {
		return goods_desc;
	}

	public void setGoods_desc(String goods_desc) {
		this.goods_desc = goods_desc;
	}

	public String getGoods_url() {
		return goods_url;
	}

	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}

	public String getUnit_amount() {
		return unit_amount;
	}

	public void setUnit_amount(String unit_amount) {
		this.unit_amount = unit_amount;
	}

	public String getUnit_count() {
		return unit_count;
	}

	public void setUnit_count(String unit_count) {
		this.unit_count = unit_count;
	}

	public String getTransport_amount() {
		return transport_amount;
	}

	public void setTransport_amount(String transport_amount) {
		this.transport_amount = transport_amount;
	}

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public String getBuyer_sp_username() {
		return buyer_sp_username;
	}

	public void setBuyer_sp_username(String buyer_sp_username) {
		this.buyer_sp_username = buyer_sp_username;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getPage_url() {
		return page_url;
	}

	public void setPage_url(String page_url) {
		this.page_url = page_url;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}

	public String getInput_charset() {
		return input_charset;
	}

	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public String getSign() {
		return Sign;
	}

	public void setSign(String sign) {
		Sign = sign;
	}

	public String getSign_method() {
		return sign_method;
	}

	public void setSign_method(String sign_method) {
		this.sign_method = sign_method;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@Override
	public String toString() {
		return "BaiDuPayOrderInfo [service_code=" + service_code + ", sp_no="
				+ sp_no + ", sp_uno=" + sp_uno + ", sp_request_type="
				+ sp_request_type + ", order_create_time=" + order_create_time
				+ ", order_no=" + order_no + ", goods_category="
				+ goods_category + ", goods_channel_sp=" + goods_channel_sp
				+ ", goods_channel=" + goods_channel + ", goods_name="
				+ goods_name + ", goods_desc=" + goods_desc + ", goods_url="
				+ goods_url + ", unit_amount=" + unit_amount + ", unit_count="
				+ unit_count + ", transport_amount=" + transport_amount
				+ ", total_amount=" + total_amount + ", Currency=" + Currency
				+ ", buyer_sp_username=" + buyer_sp_username + ", return_url="
				+ return_url + ", page_url=" + page_url + ", pay_type="
				+ pay_type + ", expire_time=" + expire_time
				+ ", input_charset=" + input_charset + ", Version=" + Version
				+ ", Sign=" + Sign + ", sign_method=" + sign_method
				+ ", extra=" + extra + "]";
	}

}
