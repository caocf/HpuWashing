package com.edaixi.activity;

import java.util.ArrayList;
import java.util.List;
import android.graphics.drawable.Drawable;
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
import com.edaixi.adapter.CityAdapter;
import com.edaixi.citylist.widget.ContactItemInterface;
import com.edaixi.citylist.widget.ContactListViewImpl;
import com.edaixi.data.AppConfig;
import com.edaixi.data.EdaixiApplication;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.CityItem;
import com.edaixi.util.AMaputil;
import com.edaixi.util.LogUtil;
import com.edaixi.util.PinYin;
import com.edaixi.util.SaveUtils;

@SuppressWarnings("deprecation")
public class CityListActivity extends BaseActivity implements
		AMapLocationListener, OnClickListener {

	private ContactListViewImpl listview;
	boolean inSearchMode = false;
	private List<ContactItemInterface> contactList;
	@SuppressWarnings("unused")
	private List<ContactItemInterface> filterList;
	private LinearLayout ll_select_city;
	private TextView city_text;
	private TextView select_city_titel_text;
	private TextView location_city_text_tips;
	private TextView tv_hot_city_beijing;
	private TextView tv_hot_city_shanghai;
	private TextView tv_hot_city_guangzhou;
	private TextView tv_hot_city_shenzhen;
	private SaveUtils saveUtils;
	private List<OpenCityModle> openCityNames;
	private LocationManagerProxy locationManagerProxy;
	private Drawable nav_up;
	private AMaputil aMaputil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_citylist);
		init(this);
		saveUtils = new SaveUtils(this);
		aMaputil = new AMaputil(getActivity());
		nav_up = getResources().getDrawable(R.drawable.city_select);
		nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
				nav_up.getMinimumHeight());
		ll_select_city = (LinearLayout) findViewById(R.id.ll_select_city);
		city_text = (TextView) findViewById(R.id.city_text);
		tv_hot_city_beijing = (TextView) findViewById(R.id.tv_hot_city_beijing);
		tv_hot_city_beijing.setOnClickListener(this);
		tv_hot_city_shanghai = (TextView) findViewById(R.id.tv_hot_city_shanghai);
		tv_hot_city_shanghai.setOnClickListener(this);
		tv_hot_city_guangzhou = (TextView) findViewById(R.id.tv_hot_city_guangzhou);
		tv_hot_city_guangzhou.setOnClickListener(this);
		tv_hot_city_shenzhen = (TextView) findViewById(R.id.tv_hot_city_shenzhen);
		tv_hot_city_shenzhen.setOnClickListener(this);
		location_city_text_tips = (TextView) findViewById(R.id.location_city_text_tips);
		select_city_titel_text = (TextView) findViewById(R.id.select_city_titel_text);

		// 高德地图定位
		locationManagerProxy = LocationManagerProxy
				.getInstance(CityListActivity.this);
		locationManagerProxy.setGpsEnable(true);
		locationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, 15, this);

		if (saveUtils.getStrSP(KeepingData.User_City) != "") {
			select_city_titel_text.setText("当前城市-"
					+ saveUtils.getStrSP(KeepingData.User_City));
		} else {
			select_city_titel_text.setText("当前城市-北京");
		}

		if (AppConfig.getInstance().isLocationFail()) {
			city_text.setText("定位失败,点击重试");
			location_city_text_tips.setVisibility(View.GONE);
			city_text.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					city_text.setText("定位中...");
					aMaputil.ReLocation(new AMapLocationListener() {

						@Override
						public void onStatusChanged(String provider,
								int status, Bundle extras) {

						}

						@Override
						public void onProviderEnabled(String provider) {

						}

						@Override
						public void onProviderDisabled(String provider) {

						}

						@Override
						public void onLocationChanged(Location location) {

						}

						@Override
						public void onLocationChanged(AMapLocation aMapLocation) {
							if (aMapLocation != null) {
								String search_user_city = aMapLocation
										.getCity();
								if (search_user_city != null
										&& search_user_city != "") {
									AppConfig.getInstance().setLocationFail(
											false);
									city_text.setText(search_user_city.replace(
											"市", ""));
									if (!aMaputil
											.judgeCityIsAviable(search_user_city
													.replace("市", ""))) {
										location_city_text_tips
												.setVisibility(View.VISIBLE);
										finish();
									} else {
										location_city_text_tips
												.setVisibility(View.GONE);
										saveUtils.saveStrSP(
												KeepingData.User_City,
												search_user_city.replace("市",
														""));
										saveUtils.saveStrSP(
												KeepingData.User_City_Id,
												getCityId(search_user_city
														.replace("市", "")));
										saveUtils
												.saveBoolSP(
														KeepingData.is_Select_City_Item,
														false);
										saveUtils.saveBoolSP(
												KeepingData.is_Select_Hot_City,
												false);
										saveUtils.saveStrSP(
												KeepingData.select_City_Item,
												"");
										finish();
									}
								} else {
									city_text.setText("定位失败,点击重试");
								}
							}
						}
					});

				}
			});
		} else {
			if (AppConfig.getInstance().isIsopendefaultcity()) {
				city_text.setText(saveUtils.getStrSP(KeepingData.Current_City));
				city_text.setClickable(false);
				city_text.setFocusable(false);
				location_city_text_tips.setVisibility(View.VISIBLE);
			} else {
				location_city_text_tips.setVisibility(View.GONE);
				city_text.setText(saveUtils.getStrSP(KeepingData.Current_City)
						+ " ");
				if (!saveUtils.getBoolSP(KeepingData.is_Select_City_Item)) {
					if (saveUtils.getBoolSP(KeepingData.is_Select_Hot_City)) {
						if (saveUtils.getStrSP(KeepingData.Select_Hot_City)
								.equals(saveUtils
										.getStrSP(KeepingData.Current_City))) {
							city_text.setCompoundDrawables(null, null, nav_up,
									null);
						}
					} else {
						city_text
								.setCompoundDrawables(null, null, nav_up, null);
					}
				}

				city_text.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						saveUtils.saveStrSP(KeepingData.User_City,
								saveUtils.getStrSP(KeepingData.Current_City));
						saveUtils.saveStrSP(KeepingData.User_City_Id,
								getCityId(saveUtils
										.getStrSP(KeepingData.Current_City)));
						saveUtils.saveBoolSP(KeepingData.is_Select_City_Item,
								false);
						saveUtils.saveBoolSP(KeepingData.is_Select_Hot_City,
								false);
						saveUtils.saveStrSP(KeepingData.select_City_Item, "");
						finish();
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

		if (city_text.getCompoundDrawables()[2] == null) {
			if (!saveUtils.getBoolSP(KeepingData.is_Select_City_Item)) {
				if (saveUtils.getStrSP(KeepingData.User_City).equals("北京")) {
					tv_hot_city_beijing.setCompoundDrawables(null, null,
							nav_up, null);
					tv_hot_city_shanghai.setCompoundDrawables(null, null, null,
							null);
					tv_hot_city_guangzhou.setCompoundDrawables(null, null,
							null, null);
					tv_hot_city_shenzhen.setCompoundDrawables(null, null, null,
							null);
				} else if (saveUtils.getStrSP(KeepingData.User_City).equals(
						"上海")) {
					tv_hot_city_beijing.setCompoundDrawables(null, null, null,
							null);
					tv_hot_city_shanghai.setCompoundDrawables(null, null,
							nav_up, null);
					tv_hot_city_guangzhou.setCompoundDrawables(null, null,
							null, null);
					tv_hot_city_shenzhen.setCompoundDrawables(null, null, null,
							null);
				} else if (saveUtils.getStrSP(KeepingData.User_City).equals(
						"深圳")) {
					tv_hot_city_beijing.setCompoundDrawables(null, null, null,
							null);
					tv_hot_city_shanghai.setCompoundDrawables(null, null, null,
							null);
					tv_hot_city_guangzhou.setCompoundDrawables(null, null,
							null, null);
					tv_hot_city_shenzhen.setCompoundDrawables(null, null,
							nav_up, null);
				} else if (saveUtils.getStrSP(KeepingData.User_City).equals(
						"广州")) {
					tv_hot_city_beijing.setCompoundDrawables(null, null, null,
							null);
					tv_hot_city_shanghai.setCompoundDrawables(null, null, null,
							null);
					tv_hot_city_guangzhou.setCompoundDrawables(null, null,
							nav_up, null);
					tv_hot_city_shenzhen.setCompoundDrawables(null, null, null,
							null);
				}
			}
		}

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
				saveUtils.saveStrSP(KeepingData.User_City,
						contactList.get(position).getDisplayInfo());
				saveUtils.saveStrSP(KeepingData.User_City_Id,
						contactList.get(position).getCityIdInfo());
				saveUtils.saveStrSP(KeepingData.select_City_Item, contactList
						.get(position).getDisplayInfo());
				saveUtils.saveBoolSP(KeepingData.is_Select_City_Item, true);
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
		DaoSession newSession = EdaixiApplication
				.getDaoSession(CityListActivity.this);
		OpenCityModleDao openCityModleDao = newSession.getOpenCityModleDao();
		openCityNames = openCityModleDao.loadAll();
		for (int i = 0; i < openCityNames.size(); i++) {
			if (!openCityNames.get(i).getName().equals("北京")
					&& !openCityNames.get(i).getName().equals("上海")
					&& !openCityNames.get(i).getName().equals("深圳")
					&& !openCityNames.get(i).getName().equals("广州")) {
				if (saveUtils.getStrSP(KeepingData.select_City_Item).equals(
						openCityNames.get(i).getName())) {
					list.add(new CityItem(openCityNames.get(i).getName(),
							PinYin.getPinYin(openCityNames.get(i).getName()),
							openCityNames.get(i).getId() + "", true));
				} else {
					list.add(new CityItem(openCityNames.get(i).getName(),
							PinYin.getPinYin(openCityNames.get(i).getName()),
							openCityNames.get(i).getId() + "", false));
				}
			}
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_hot_city_beijing:
			tv_hot_city_beijing.setCompoundDrawables(null, null, nav_up, null);
			tv_hot_city_shanghai.setCompoundDrawables(null, null, null, null);
			tv_hot_city_guangzhou.setCompoundDrawables(null, null, null, null);
			tv_hot_city_shenzhen.setCompoundDrawables(null, null, null, null);
			saveUtils.saveStrSP(KeepingData.User_City, "北京");
			saveUtils.saveStrSP(KeepingData.Select_Hot_City, "北京");
			saveUtils.saveStrSP(KeepingData.User_City_Id, getCityId("北京"));
			saveUtils.saveStrSP(KeepingData.select_City_Item, "");
			saveUtils.saveBoolSP(KeepingData.is_Select_City_Item, false);
			saveUtils.saveBoolSP(KeepingData.is_Select_Hot_City, true);
			finish();
			break;
		case R.id.tv_hot_city_shanghai:
			tv_hot_city_beijing.setCompoundDrawables(null, null, null, null);
			tv_hot_city_shanghai.setCompoundDrawables(null, null, nav_up, null);
			tv_hot_city_guangzhou.setCompoundDrawables(null, null, null, null);
			tv_hot_city_shenzhen.setCompoundDrawables(null, null, null, null);
			saveUtils.saveStrSP(KeepingData.User_City, "上海");
			saveUtils.saveStrSP(KeepingData.Select_Hot_City, "上海");
			saveUtils.saveStrSP(KeepingData.User_City_Id, getCityId("上海"));
			saveUtils.saveStrSP(KeepingData.select_City_Item, "");
			saveUtils.saveBoolSP(KeepingData.is_Select_City_Item, false);
			saveUtils.saveBoolSP(KeepingData.is_Select_Hot_City, true);
			finish();
			break;
		case R.id.tv_hot_city_guangzhou:
			tv_hot_city_beijing.setCompoundDrawables(null, null, null, null);
			tv_hot_city_shanghai.setCompoundDrawables(null, null, null, null);
			tv_hot_city_guangzhou
					.setCompoundDrawables(null, null, nav_up, null);
			tv_hot_city_shenzhen.setCompoundDrawables(null, null, null, null);
			saveUtils.saveStrSP(KeepingData.User_City, "广州");
			saveUtils.saveStrSP(KeepingData.Select_Hot_City, "广州");
			saveUtils.saveStrSP(KeepingData.User_City_Id, getCityId("广州"));
			saveUtils.saveStrSP(KeepingData.select_City_Item, "");
			saveUtils.saveBoolSP(KeepingData.is_Select_City_Item, false);
			saveUtils.saveBoolSP(KeepingData.is_Select_Hot_City, true);
			finish();
			break;
		case R.id.tv_hot_city_shenzhen:
			tv_hot_city_beijing.setCompoundDrawables(null, null, null, null);
			tv_hot_city_shanghai.setCompoundDrawables(null, null, null, null);
			tv_hot_city_guangzhou.setCompoundDrawables(null, null, null, null);
			tv_hot_city_shenzhen.setCompoundDrawables(null, null, nav_up, null);
			saveUtils.saveStrSP(KeepingData.User_City, "深圳");
			saveUtils.saveStrSP(KeepingData.Select_Hot_City, "深圳");
			saveUtils.saveStrSP(KeepingData.User_City_Id, getCityId("深圳"));
			saveUtils.saveStrSP(KeepingData.select_City_Item, "");
			saveUtils.saveBoolSP(KeepingData.is_Select_City_Item, false);
			saveUtils.saveBoolSP(KeepingData.is_Select_Hot_City, true);
			finish();
			break;
		}
	}

	public String getCityId(String cityName) {
		String cityIdString = null;
		DaoSession newSession = EdaixiApplication
				.getDaoSession(CityListActivity.this);
		OpenCityModleDao openCityModleDao = newSession.getOpenCityModleDao();
		List<OpenCityModle> loadAll = openCityModleDao.loadAll();
		for (OpenCityModle openCityModle : loadAll) {
			if (openCityModle.getName().equals(cityName)) {
				cityIdString = openCityModle.getId() + "";
			}
		}
		return cityIdString;
	}
}
