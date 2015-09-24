package com.edaixi.citylist.widget;

public interface ContactItemInterface
{
	// 根据该字段来排序
	public String getItemForIndex();

	// 该字段用来显示出来
	public String getDisplayInfo();
	
	// 该字段用来标识城市ID
	public String getCityIdInfo();
}
