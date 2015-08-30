package com.edaixi.modle;

import com.edaixi.citylist.widget.ContactItemInterface;

public class CityItem implements ContactItemInterface {
	private String nickName;
	private String fullName;
	private String cityId;
	private boolean isSelect;

	public CityItem(String nickName, String fullName, String cityId,
			boolean isSelect) {
		super();
		this.nickName = nickName;
		this.cityId = cityId;
		this.setFullName(fullName);
		this.isSelect = isSelect;
	}

	@Override
	public String getCityIdInfo() {
		return cityId;
	}

	@Override
	public String getItemForIndex() {
		return fullName;
	}

	@Override
	public String getDisplayInfo() {
		return nickName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	@Override
	public boolean getCitySelectInfo() {
		return isSelect;
	}
}
