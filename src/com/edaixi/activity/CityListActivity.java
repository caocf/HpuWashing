package com.edaixi.activity;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.edaixi.adapter.CityAdapter;
import com.edaixi.citylist.widget.ContactItemInterface;
import com.edaixi.citylist.widget.ContactListViewImpl;
import com.edaixi.data.AppConfig;
import com.edaixi.data.EdaixiApplication;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.CityItem;
import com.edaixi.modle.OpenCityBean;
import com.edaixi.util.LogUtil;
import com.edaixi.util.PinYin;
import com.edaixi.util.SaveUtils;

@SuppressWarnings("deprecation")
public class CityListActivity extends BaseActivity implements
		AMapLocationListener {

	private ContactListViewImpl listview;
	boolean inSearchMode = false;
	private List<ContactItemInterface> contactList;
	@SuppressWarnings("unused")
	private List<ContactItemInterface> filterList;
	private LinearLayout ll_select_city;
	private TextView city_text;
	private TextView select_city_titel_text;
	private SaveUtils saveUtils;
	private List<OpenCityModle> openCityNames;
	private LocationManagerProxy locationManagerProxy;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_citylist);
		init(this);
		saveUtils = new SaveUtils(this);
		ll_select_city = (LinearLayout) findViewById(R.id.ll_select_city);
		city_text = (TextView) findViewById(R.id.city_text);
		select_city_titel_text = (TextView) findViewById(R.id.select_city_titel_text);

		// 高德地图定位
		locationManagerProxy = LocationManagerProxy
				.getInstance(CityListActivity.this);
		locationManagerProxy.setGpsEnable(true);
		locationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, 15, this);

		LogUtil.e("user-city" + saveUtils.getStrSP(KeepingData.User_City));
		if (saveUtils.getStrSP(KeepingData.User_City) != "") {
			select_city_titel_text.setText("当前城市-"
					+ saveUtils.getStrSP(KeepingData.User_City));
		} else {
			select_city_titel_text.setText("当前城市-北京");
		}

		if (AppConfig.getInstance().isLocationFail()) {
			city_text.setText("失败重试");
		} else {
			if (AppConfig.getInstance().isIsopendefaultcity()) {
				city_text.setText("超出服务范围");
			} else {
				city_text.setText(saveUtils.getStrSP(KeepingData.Current_City));
				city_text.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						saveUtils.saveStrSP(KeepingData.User_City,
								saveUtils.getStrSP(KeepingData.Current_City));
						finish(0, 0);
					}
				});
			}
		}

		ll_select_city.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish(0, 0);
			}
		});

		filterList = new ArrayList<ContactItemInterface>();
		contactList = getSampleContactList();

		CityAdapter adapter = new CityAdapter(this, R.layout.city_list_item,
				contactList);

		listview = (ContactListViewImpl) this.findViewById(R.id.open_listview);
		listview.setFastScrollEnabled(true);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {
				//city_text.setText(contactList.get(position).getDisplayInfo());
				saveUtils.saveStrSP(KeepingData.User_City,
						contactList.get(position).getDisplayInfo());
				saveUtils.saveStrSP(KeepingData.User_City_Id,
						contactList.get(position).getCityIdInfo());
				finish(0, 0);
			}
		});
	}

	/**
	 * 从数据库读取缓存的城市列表
	 * 
	 * @return
	 */
	public List<ContactItemInterface> getSampleContactList() {
		List<ContactItemInterface> list = new ArrayList<ContactItemInterface>();
		openCityNames = new ArrayList<OpenCityModle>();
		@SuppressWarnings("deprecation")
		DaoSession newSession = EdaixiApplication
				.getDaoSession(CityListActivity.this);
		OpenCityModleDao openCityModleDao = newSession.getOpenCityModleDao();
		openCityNames = openCityModleDao.loadAll();
		for (int i = 0; i < openCityNames.size(); i++) {
			list.add(new CityItem(openCityNames.get(i).getName(), PinYin
					.getPinYin(openCityNames.get(i).getName()), openCityNames
					.get(i).getId() + ""));
		}
		return list;
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
	}
}
