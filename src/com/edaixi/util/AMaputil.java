package com.edaixi.util;

import android.app.Activity;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.edaixi.activity.DaoSession;
import com.edaixi.activity.OpenCityModle;
import com.edaixi.activity.OpenCityModleDao;
import com.edaixi.activity.OpenCityModleDao.Properties;
import com.edaixi.data.EdaixiApplication;

import de.greenrobot.dao.query.QueryBuilder;

public class AMaputil {

	private Activity mActivity;
	private OpenCityModleDao openCityModleDao;
	private DaoSession newSession;
	private SaveUtils saveUtils;

	public AMaputil(Activity mActivity) {
		super();
		this.mActivity = mActivity;
		newSession = EdaixiApplication.getDaoSession(mActivity);
		openCityModleDao = newSession.getOpenCityModleDao();
		saveUtils = new SaveUtils(mActivity);
	}

	public void ReLocation(AMapLocationListener aMapLocationListener) {
		LocationManagerProxy locationManagerProxy = LocationManagerProxy
				.getInstance(mActivity);
		locationManagerProxy.setGpsEnable(true);
		locationManagerProxy
				.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 15,
						aMapLocationListener);
	}

	public boolean judgeCityIsAviable(String cityName) {
		QueryBuilder<OpenCityModle> qb = openCityModleDao.queryBuilder();
		qb.where(Properties.Name.eq(cityName));
		if (qb.list().size() > 0) {
			return true;
		}
		return false;
	}

}
