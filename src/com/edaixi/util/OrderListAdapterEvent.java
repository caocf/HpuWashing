package com.edaixi.util;

/**
 * a util class for use eventbus....
 * 
 * @author wei-spring
 */
public class OrderListAdapterEvent {
	private String text;

	public OrderListAdapterEvent(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
