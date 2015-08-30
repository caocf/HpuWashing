package com.edaixi.Enum;

import java.io.Serializable;

/**
 * 进入优惠券页面的条件枚举
 */
public enum UseConponType implements Serializable {

	/**
	 * 查看优惠券
	 */
	CHECK(0),

	/**
	 * 使用优惠券
	 */
	USE(1);

	private int mType;

	private UseConponType(int mType) {
		this.mType = mType;
	}

	public int getType() {
		return mType;
	}

}
