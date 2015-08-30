package com.edaixi.modle;

import java.io.Serializable;

public class ClothingOrderInfo implements Serializable {
	private static final long serialVersionUID = -8420078894358023118L;

	private String ordersn;
	private String color;
	private String cloth_title;
	private String original_price;
	private String current_price;
	private String cloth_condition;
	private String wash_result;

	public String getOrdersn() {
		return ordersn;
	}

	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCloth_title() {
		return cloth_title;
	}

	public void setCloth_title(String cloth_title) {
		this.cloth_title = cloth_title;
	}

	public String getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(String original_price) {
		this.original_price = original_price;
	}

	public String getCurrent_price() {
		return current_price;
	}

	public void setCurrent_price(String current_price) {
		this.current_price = current_price;
	}

	public String getCloth_condition() {
		return cloth_condition;
	}

	public void setCloth_condition(String cloth_condition) {
		this.cloth_condition = cloth_condition;
	}

	public String getWash_result() {
		return wash_result;
	}

	public void setWash_result(String wash_result) {
		this.wash_result = wash_result;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ClothingOrderInfo [ordersn=" + ordersn + ", color=" + color
				+ ", cloth_title=" + cloth_title + ", original_price="
				+ original_price + ", current_price=" + current_price
				+ ", cloth_condition=" + cloth_condition + ", wash_result="
				+ wash_result + "]";
	}

}
