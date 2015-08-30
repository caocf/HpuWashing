package com.edaixi.modle;

import java.io.Serializable;

public class ExtraCardBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String account_no;
	private String coin;
	private String created_at;
	private String available_at;
	private String account_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getAvailable_at() {
		return available_at;
	}

	public void setAvailable_at(String available_at) {
		this.available_at = available_at;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	@Override
	public String toString() {
		return "ExtraCardBean [id=" + id + ", account_no=" + account_no
				+ ", coin=" + coin + ", created_at=" + created_at
				+ ", available_at=" + available_at + ", account_name="
				+ account_name + "]";
	}

}
