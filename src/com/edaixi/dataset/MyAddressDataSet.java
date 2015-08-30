package com.edaixi.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.edaixi.modle.AddressBean;

/**
 * 我的常用地址页面所用的数据集
 */
@SuppressWarnings("serial")
public class MyAddressDataSet implements IDataDao<AddressBean>, Serializable {

	private List<AddressBean> mDataSet = null;
	private AddressBean mitem;

	public MyAddressDataSet() {
		mDataSet = new ArrayList<AddressBean>();
	}

	@Override
	public void addBean(AddressBean mBean) {
		mDataSet.add(mBean);
	}

	@Override
	public AddressBean removeIndexBean(int mIndex) {
		if (mIndex < mDataSet.size()) {
			return mDataSet.remove(mIndex);
		}
		return null;
	}

	@Override
	public AddressBean getIndexBean(int mIndex) {
		if (mIndex < mDataSet.size()) {
			return mDataSet.get(mIndex);
		}
		return null;
	}

	@Override
	public int size() {
		return mDataSet.size();
	}

	@Override
	public void clear() {
		mDataSet.clear();
	}

	@Override
	public AddressBean alwaysuseads() {
		for (AddressBean item : mDataSet) {
			if (item.isFrequently_address()) {
				mitem = item;
			}
		}
		return mitem;
	}

	@Override
	public boolean canused(int mIndex) {
		return mDataSet.get(mIndex).isIs_service_place();
	}
}
