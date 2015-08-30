package com.edaixi.modle;

/**
 * 查询实体卡数据时实体卡对应的数据bean
 */
public class EntityCardBean {

	private int id;
	private long cardno;
	private String rcard_sn;
	private double coin;

	public EntityCardBean() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getCardno() {
		return cardno;
	}

	public void setCardno(long cardno) {
		this.cardno = cardno;
	}

	public String getRcard_sn() {
		return rcard_sn;
	}

	public void setRcard_sn(String rcard_sn) {
		this.rcard_sn = rcard_sn;
	}

	public double getCoin() {
		return coin;
	}

	public void setCoin(double coin) {
		this.coin = coin;
	}

}
