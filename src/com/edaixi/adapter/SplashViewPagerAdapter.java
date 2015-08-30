package com.edaixi.adapter;

import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 开屏Splash页面适配器
 */
public class SplashViewPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragDataSet = null;

	public SplashViewPagerAdapter(FragmentManager fm,
			List<Fragment> mFragDataSet) {
		super(fm);
		this.mFragDataSet = mFragDataSet;
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragDataSet.get(arg0);
	}

	@Override
	public int getCount() {
		return mFragDataSet.size();
	}
}
