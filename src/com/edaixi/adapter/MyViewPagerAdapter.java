package com.edaixi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

	Fragment[] fms = null;
	String isLogin = null;

	public MyViewPagerAdapter(FragmentManager fm, Fragment[] fms, String isLogin) {
		super(fm);
		this.fms = fms;
		this.isLogin = isLogin;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	}

	@Override
	public Fragment getItem(int arg0) {
		return fms[arg0];
	}

	@Override
	public int getCount() {
		return fms.length;
	}

	public Fragment getMineFragment() {
		if (isLogin.equals("true")) {
			return fms[2];
		} else {
			return null;
		}
	}

	public Fragment getHomeFragment() {
		return fms[0];
	}

	public Fragment getOrderFragment() {
		if (isLogin.equals("true")) {
			return fms[1];
		} else {
			return null;
		}
	}
}
