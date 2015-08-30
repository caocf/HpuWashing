package com.edaixi.modle;

import java.io.Serializable;

/**
 * a model for recharge setting...
 */
public class RechargeSettingInfo implements Serializable {

	private static final long serialVersionUID = 3015105593764970468L;
	private String id;
	private String weid;
	private String money_give;
	private String min;
	private String max;
	private String dateline;
	private String money_giveCopy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWeid() {
		return weid;
	}

	public void setWeid(String weid) {
		this.weid = weid;
	}

	public String getMoney_give() {
		return money_give;
	}

	public void setMoney_give(String money_give) {
		this.money_give = money_give;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getDateline() {
		return dateline;
	}

	public void setDateline(String dateline) {
		this.dateline = dateline;
	}

	public String getMoney_giveCopy() {
		return money_giveCopy;
	}

	public void setMoney_giveCopy(String money_giveCopy) {
		this.money_giveCopy = money_giveCopy;
	}

	@Override
	public String toString() {
		return "RechargeSettingInfo [id=" + id + ", weid=" + weid
				+ ", money_give=" + money_give + ", min=" + min + ", max="
				+ max + ", dateline=" + dateline + ", money_giveCopy="
				+ money_giveCopy + "]";
	}

}
