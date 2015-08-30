package com.edaixi.modle;

import java.io.Serializable;

public class MapItemBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mapItemCity;
	private String mapItemArea;
	private String mapItemTitle;
	private String mapItemName;
	private String mapItemLat;
	private String mapItemLog;
	private boolean isMapItemSelect;

	public String getMapItemArea() {
		return mapItemArea;
	}

	public void setMapItemArea(String mapItemArea) {
		this.mapItemArea = mapItemArea;
	}

	public String getMapItemCity() {
		return mapItemCity;
	}

	public void setMapItemCity(String mapItemCity) {
		this.mapItemCity = mapItemCity;
	}

	public String getMapItemTitle() {
		return mapItemTitle;
	}

	public void setMapItemTitle(String mapItemTitle) {
		this.mapItemTitle = mapItemTitle;
	}

	public String getMapItemName() {
		return mapItemName;
	}

	public void setMapItemName(String mapItemName) {
		this.mapItemName = mapItemName;
	}

	public String getMapItemLat() {
		return mapItemLat;
	}

	public void setMapItemLat(String mapItemLat) {
		this.mapItemLat = mapItemLat;
	}

	public String getMapItemLog() {
		return mapItemLog;
	}

	public void setMapItemLog(String mapItemLog) {
		this.mapItemLog = mapItemLog;
	}

	public boolean isMapItemSelect() {
		return isMapItemSelect;
	}

	public void setMapItemSelect(boolean isMapItemSelect) {
		this.isMapItemSelect = isMapItemSelect;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MapItemBean [mapItemCity=" + mapItemCity + ", mapItemArea="
				+ mapItemArea + ", mapItemTitle=" + mapItemTitle
				+ ", mapItemName=" + mapItemName + ", mapItemLat=" + mapItemLat
				+ ", mapItemLog=" + mapItemLog + ", isMapItemSelect="
				+ isMapItemSelect + "]";
	}
}
