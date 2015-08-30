package com.edaixi.modle;

import java.io.Serializable;

/**
 * 开通城市Bean
 */
public class OpenCityBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private boolean is_show;
	private String created_at;
	private String updated_at;
	private String fuwufanwei;
	private String initials;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIs_show() {
		return is_show;
	}

	public void setIs_show(boolean is_show) {
		this.is_show = is_show;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getFuwufanwei() {
		return fuwufanwei;
	}

	public void setFuwufanwei(String fuwufanwei) {
		this.fuwufanwei = fuwufanwei;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
