package com.edaixi.modle;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Icard implements Serializable {
	private String cardno;
	private String rcard_sn;

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getRecard_sn() {
		return rcard_sn;
	}

	public void setRecard_sn(String rcard_sn) {
		this.rcard_sn = rcard_sn;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	private String coin;
}
