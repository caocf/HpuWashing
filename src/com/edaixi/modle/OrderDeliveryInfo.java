package com.edaixi.modle;

import java.io.Serializable;

public class OrderDeliveryInfo implements Serializable {

	private static final long serialVersionUID = -4460744281482579481L;

	private String text;
	private String time;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "OrderDeliveryInfo [text=" + text + ", time=" + time + "]";
	}
}
