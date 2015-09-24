package com.edaixi.dataset;

import java.util.ArrayList;
import java.util.List;

import com.edaixi.Enum.CouponEntity;

/**
 * 优惠券页面使用的优惠券数据集
 */
public class CouponsDataSet implements IDataSet<CouponEntity> {

	private List<CouponEntity> mDataSet = null;

	public CouponsDataSet() {
		mDataSet = new ArrayList<CouponEntity>();
	}

	@Override
	public void addBean(CouponEntity mBean) {
		mDataSet.add(mBean);
	}

	@Override
	public CouponEntity removeIndexBean(int mIndex) {

		return mDataSet.remove(mIndex);
	}

	@Override
	public CouponEntity getIndexBean(int mIndex) {
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

	/**
	 * 根据消费额确定优惠券是否有效
	 * 
	 * @param mMoney
	 */
	public void setDataSetValid(double mMoney) {
		for (CouponEntity mEntity : mDataSet) {
			mEntity.setValid(mMoney);
		}
	}

	@Override
	public CouponEntity getitem() {
		// TODO Auto-generated method stub
		return null;
	}

}
