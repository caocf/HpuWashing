package com.edaixi.activity;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.edaixi.data.AppConfig;
import com.edaixi.data.EdaixiApplication;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.BannerlistBean;
import com.edaixi.modle.HttpCommonBean;
import com.edaixi.modle.OpenCityBean;
import com.edaixi.util.Constants;
import com.edaixi.util.DensityUtil;
import com.edaixi.util.LogUtil;
import com.edaixi.util.MyhttpUtils;
import com.edaixi.util.PinYin;
import com.edaixi.util.SaveUtils;
import com.google.gson.Gson;

@SuppressWarnings("deprecation")
@SuppressLint("HandlerLeak")
public class AdsActivity extends BaseActivity implements AMapLocationListener,
		OnGeocodeSearchListener {

	private static final int CITYSUCESS = 4;
	private static final int CITYFAIL = 5;
	private ImageView iv_ads;
	private HashMap<String, String> mparms;
	private SaveUtils saveUtils;
	private final int GetOpenPicSucess = 150;
	private final int GetOpenPicFail = 151;
	private Bitmap bitmap;
	private BannerlistBean bannerlistbean;
	private double longitude;
	private double latitude;
	private LocationManagerProxy locationManagerProxy;
	private static ArrayList<OpenCityBean> citylist;
	private DaoSession newSession;
	private OpenCityModleDao openCityModleDao;
	private GeocodeSearch geocoderSearch;
	@SuppressWarnings("unused")
	private String user_City;

	private Handler picHandler = new Handler() {
	};

	private Handler handler = new Handler() {

		private Gson gson = new Gson();

		@SuppressWarnings("unused")
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case GetOpenPicSucess:
				HttpCommonBean jsonCommonBean = gson.fromJson(
						(String) msg.obj.toString(), HttpCommonBean.class);
				if (jsonCommonBean.isRet()) {
					String data = jsonCommonBean.getData();
					try {
						JSONArray jsonArray = new JSONArray(data);
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArray
									.opt(i);
							// JSONObject jsonObject = new JSONObject(data);
							bannerlistbean = new BannerlistBean();
							bannerlistbean.setImage_url(jsonObject
									.getString("image_url"));
							bannerlistbean.setUrl_type(jsonObject
									.getString("url_type"));
							bannerlistbean.setUrl(jsonObject.getString("url"));
							bannerlistbean.setTitle(jsonObject
									.getString("title"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showdialog(jsonCommonBean.getError());
				}
				break;
			case GetOpenPicFail:
				break;
			case CITYSUCESS:
				HttpCommonBean commonBean = gson.fromJson(
						(String) msg.obj.toString(), HttpCommonBean.class);
				if (commonBean.isRet()) {
					try {
						JSONArray jsonArray = new JSONArray(
								commonBean.getData());
						citylist = new ArrayList<OpenCityBean>();
						openCityModleDao.deleteAll();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jo = (JSONObject) jsonArray.opt(i);
							OpenCityBean openCityBean = gson.fromJson(
									jo.getString("data"), OpenCityBean.class);
							citylist.add(openCityBean);
							OpenCityModle openCityModle = new OpenCityModle();
							openCityModle.setId(openCityBean.getId());
							openCityModle.setName(openCityBean.getName());
							openCityModle.setIs_show(openCityBean.isIs_show());
							openCityModle.setCreated_at(openCityBean
									.getCreated_at());
							openCityModle.setUpdated_at(openCityBean
									.getUpdated_at());
							openCityModle.setFuwufanwei(openCityBean
									.getFuwufanwei());
							openCityModle.setInitials(openCityBean
									.getInitials());
							LogUtil.e("-" + openCityModle.toString());
							openCityModleDao.insert(openCityModle);
						}
						List<OpenCityModle> loadAll = openCityModleDao
								.loadAll();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showdialog(commonBean.getError());
				}
				break;
			case CITYFAIL:
				break;
			}
		};
	};
	private Runnable rightRunnable = new Runnable() {
		@Override
		public void run() {
			startActivity(new Intent(AdsActivity.this, MainActivity.class));
			finish(0, 0);
		}
	};
	private Runnable loadingRunnable = new Runnable() {
		@Override
		public void run() {
			if (bannerlistbean != null) {
				new LoadImage().execute(bannerlistbean.getImage_url());
			} else {
				picHandler.postDelayed(rightRunnable, 0);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ads);
		init(this);
		// greendao数据缓存
		newSession = EdaixiApplication.getDaoSession(this);
		openCityModleDao = newSession.getOpenCityModleDao();
		// 获取城市列表
		getcityList();
		iv_ads = (ImageView) findViewById(R.id.iv_ads);

		// 高德定位
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		locationManagerProxy = LocationManagerProxy.getInstance(this);
		locationManagerProxy.setGpsEnable(true);
		locationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, 15, this);
		saveUtils = new SaveUtils(this);

		getOpenPic();
		picHandler.postDelayed(loadingRunnable, 2000);
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return false;
	}

	private class LoadImage extends AsyncTask<String, String, Bitmap> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected Bitmap doInBackground(String... args) {
			try {
				if (bannerlistbean != null) {
					URL myAdsUrl = new URL(bannerlistbean.getImage_url());
					URLConnection myAdsUrlCon = myAdsUrl.openConnection();
					myAdsUrlCon.setConnectTimeout(1000);
					myAdsUrlCon.setReadTimeout(1000);
					bitmap = BitmapFactory.decodeStream((InputStream) myAdsUrl
							.getContent());
				} else {
					bitmap = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap image) {
			if (image != null) {
				iv_ads.setImageBitmap(image);
				Animation animation = AnimationUtils.loadAnimation(
						AdsActivity.this, R.anim.ads_alpha);
				iv_ads.startAnimation(animation);
				picHandler.postDelayed(rightRunnable, 2000);
			} else {
				picHandler.postDelayed(rightRunnable, 100);
			}
		}
	}

	/**
	 * 获取启动页面图片
	 */
	public void getOpenPic() {
		mparms = new HashMap<String, String>();
		mparms.put("width", String.valueOf(DensityUtil.getWidthInPx(this)));
		mparms.put("height", String.valueOf(DensityUtil.getHeightInPx(this)));
		if (saveUtils.getStrSP(KeepingData.User_City_Id) != null) {
			mparms.put("city_id", saveUtils.getStrSP(KeepingData.User_City_Id));
		} else {
			mparms.put("city_id", "1");
		}
		MyhttpUtils utils = new MyhttpUtils(getApplicationContext());
		String url = utils.getUrl(mparms, Constants.GET_OPEN_PIC,
				getApplicationContext());
		String urlss = utils.getUrl(mparms, url, getApplicationContext());
		utils.getdate(getContext(), handler, GetOpenPicSucess, GetOpenPicFail,
				mparms, urlss, false, false);
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

		if (aMapLocation != null
				&& aMapLocation.getAMapException().getErrorCode() == 0) {
			AppConfig.getInstance().setLocationLatString(
					aMapLocation.getLatitude() + "");
			AppConfig.getInstance().setLocationLogString(
					aMapLocation.getLongitude() + "");
			LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
			// getAddress(latLonPoint);
			String formatAddress = aMapLocation.getAddress();
			saveUtils.saveStrSP(KeepingData.USER_ADDRESS, formatAddress);
			String formatAddressFill = formatAddress
					.replace(aMapLocation.getCity(), "")
					.replace(aMapLocation.getDistrict(), "")
					.replace(aMapLocation.getProvince(), "");
			LogUtil.e("定位地址------" + formatAddressFill);
			saveUtils.saveStrSP(KeepingData.USER_ADDRESS_FILL,
					formatAddressFill);

			String search_user_city = aMapLocation.getCity();
			String search_user_area = aMapLocation.getDistrict();
			LogUtil.e("formatAddress  " + formatAddress);
			LogUtil.e("search_user_city" + search_user_city);
			saveUtils.saveStrSP(KeepingData.City_Code,
					aMapLocation.getCityCode());
			int indexCity = formatAddress.indexOf("市");
			int indexProvince = formatAddress.indexOf("省") + 1;
			String user_city;
			if ((indexProvince != -1) && (indexCity != -1)) {
				user_city = formatAddress.substring(indexProvince, indexCity);
			} else {
				if (indexCity != -1) {
					user_city = formatAddress.substring(0, indexCity);
				} else {
					user_city = "北京";
				}
			}
			if (search_user_city == null || search_user_city == "") {
				search_user_city = user_city;
			}
			saveUtils.saveStrSP(KeepingData.Current_City, search_user_city.replace("市", ""));
			saveUtils.saveStrSP(KeepingData.Current_Area, search_user_area);
			LogUtil.e("user_city-city" + user_city);
			LogUtil.e("search_user_city" + search_user_city);
			LogUtil.e("current-city"
					+ saveUtils.getStrSP(KeepingData.Current_City));
			LogUtil.e("current-area"
					+ saveUtils.getStrSP(KeepingData.Current_Area));
			if (search_user_city != null) {
				if ((saveUtils.getStrSP(KeepingData.User_City) != "")) {
					if (PinYin.getPinYin(
							saveUtils.getStrSP(KeepingData.User_City))
							.equals(PinYin.getPinYin((search_user_city.replace(
									"市", ""))))) {
					} else {
						saveUtils.saveStrSP(KeepingData.User_City_New,
								search_user_city.replace("市", ""));
					}
				} else {
					saveUtils.saveStrSP(KeepingData.User_City,
							search_user_city.replace("市", ""));
				}
			}
			AppConfig.getInstance().setLocationFail(false);
		} else {
			AppConfig.getInstance().setLocationFail(true);
		}
	}

	/** 经纬度转地理位置工具方法 **/
	public void onRegeocodeSearched() {

	}

	/** 获取城市列表 **/
	public void getcityList() {
		mparms = new HashMap<String, String>();
		mparms.clear();
		MyhttpUtils utils = new MyhttpUtils(getApplicationContext());
		String url = utils.getUrl(mparms, Constants.getcitylist,
				getApplicationContext());
		utils.getdate(getContext(), handler, CITYSUCESS, CITYFAIL, mparms, url,
				false, false);
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locationManagerProxy != null) {
			locationManagerProxy.removeUpdates(this);
			locationManagerProxy.destroy();
		}
		locationManagerProxy = null;
	}

	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {

	}

	/**
	 * 响应逆地理编码
	 */
	public void getAddress(LatLonPoint latLonPoint) {
		// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 1000,
				GeocodeSearch.AMAP);
		// 设置同步逆地理编码请求
		geocoderSearch.getFromLocationAsyn(query);
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				String formatAddress = result.getRegeocodeAddress()
						.getFormatAddress().toString();
				saveUtils.saveStrSP(KeepingData.USER_ADDRESS, formatAddress);
				String formatAddressFill = formatAddress
						.replace(result.getRegeocodeAddress().getCity(), "")
						.replace(result.getRegeocodeAddress().getDistrict(), "")
						.replace(result.getRegeocodeAddress().getProvince(), "");
				saveUtils.saveStrSP(KeepingData.USER_ADDRESS_FILL,
						formatAddressFill);

				String search_user_city = result.getRegeocodeAddress()
						.getCity();
				LogUtil.e("formatAddress  " + formatAddress);
				LogUtil.e("search_user_city00" + search_user_city);
				saveUtils.saveStrSP(KeepingData.City_Code, result
						.getRegeocodeAddress().getCityCode());
				int indexCity = formatAddress.indexOf("市");
				int indexProvince = formatAddress.indexOf("省") + 1;
				String user_city;
				if ((indexProvince != -1) && (indexCity != -1)) {
					user_city = formatAddress.substring(indexProvince,
							indexCity);
				} else {
					if (indexCity != -1) {
						user_city = formatAddress.substring(0, indexCity);
					} else {
						user_city = "北京";
					}
				}
				if (search_user_city == null || search_user_city == "") {
					search_user_city = user_city;
				}
				saveUtils.saveStrSP(KeepingData.Current_City,
						search_user_city.replace("市", ""));
				LogUtil.e("user_city-city" + user_city);
				LogUtil.e("search_user_city" + search_user_city);
				LogUtil.e("current-city"
						+ saveUtils.getStrSP(KeepingData.Current_City));
				if (search_user_city != null) {
					if ((saveUtils.getStrSP(KeepingData.User_City) != "")) {
						if (PinYin.getPinYin(
								saveUtils.getStrSP(KeepingData.User_City))
								.equals(PinYin.getPinYin((search_user_city
										.replace("市", ""))))) {
						} else {
							saveUtils.saveStrSP(KeepingData.User_City_New,
									search_user_city.replace("市", ""));
						}
					} else {
						saveUtils.saveStrSP(KeepingData.User_City,
								search_user_city.replace("市", ""));
					}
				}
				AppConfig.getInstance().setLocationFail(false);
			} else {
				AppConfig.getInstance().setLocationFail(true);
			}
		}
	}

}
