package com.edaixi.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.edaixi.activity.CityListActivity;
import com.edaixi.activity.MainActivity;
import com.edaixi.activity.R;
import com.edaixi.activity.WebActivity;
import com.edaixi.adapter.Myfuncadpter;
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.BannerlistBean;
import com.edaixi.modle.HttpCommonBean;
import com.edaixi.util.Constants;
import com.edaixi.util.DensityUtil;
import com.edaixi.util.NetUtil;
import com.edaixi.util.SaveUtils;
import com.edaixi.view.TipsDialog;
import com.google.gson.Gson;
import com.tendcloud.tenddata.TCAgent;
import com.viewpagerindicator.AutoScrollViewPager;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.IconPagerAdapter;
import com.zxinsight.MarketingHelper;
import com.zxinsight.common.util.Settings;

@SuppressLint("HandlerLeak")
public class HomeFragmentBak extends BaseFragment {
	private static final int GETBANNERFAILD = 2;
	private static final int GETBANNERSUCCED = 3;
	private static final int GETFUNCTIONBTNSUCCED = 4;
	private static final int GETFUNCTIONBTNFAILD = 5;
	private static final int GETBANNERBUTTONFAILD = 6;
	private static final int GETBANNERBUTTONSUCCED = 7;
	boolean RequstFlag = false;
	private View view;
	private HashMap<String, String> parms;
	private SaveUtils saveUtils;
	private GridView home_grid_top;
	private com.edaixi.view.ExpandableHeightGridView home_grid_bottom;
	private Myfuncadpter topAdapter;
	private Myfuncadpter bottomAdapter;
	private AutoScrollViewPager pager;
	private ArrayList<BannerlistBean> bannerbtnlist;
	private ArrayList<BannerlistBean> funditonlist;
	private JSONObject str;
	private ArrayList<BannerlistBean> banerlistbak;
	private static TextView loction_text;
	private static TextView home_servesarea_text;
	private static TextView mTextViewShowTips;
	private boolean is_Loop_Flag = true;;
	private boolean is_Get_Binner = false;
	private MarketingHelper marketingHelper;
	private CirclePageIndicator indicator;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (is_Loop_Flag) {
				mHandler.sendEmptyMessageDelayed(0, 3000);
			}
		};
	};

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		private Gson gson = new Gson();

		@Override
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case GETBANNERSUCCED:
					RequstFlag = true;
					HttpCommonBean jsonCommonBean = gson.fromJson(
							(String) msg.obj, HttpCommonBean.class);
					if (jsonCommonBean.isRet()) {
						String data = jsonCommonBean.getData();
						paserbaner(data, banerlistbak, "Top");
					}
					break;
				case GETBANNERFAILD:
					RequstFlag = false;
					break;
				case GETFUNCTIONBTNSUCCED:
					RequstFlag = true;
					HttpCommonBean CommonBean = gson.fromJson((String) msg.obj,
							HttpCommonBean.class);
					if (CommonBean.isRet()) {
						String data = CommonBean.getData();
						paserbaner(data, funditonlist, "Bottom");
					}
					break;
				case GETFUNCTIONBTNFAILD:
					RequstFlag = false;
					break;
				case GETBANNERBUTTONSUCCED:
					RequstFlag = true;
					HttpCommonBean CommonBeanBtn = gson.fromJson(
							(String) msg.obj, HttpCommonBean.class);
					if (CommonBeanBtn.isRet()) {
						String data = CommonBeanBtn.getData();
						paserbaner(data, bannerbtnlist, "Middle");
					}
					break;
				case GETBANNERBUTTONFAILD:
					RequstFlag = false;
					break;
				}
				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@SuppressLint("HandlerLeak")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.fragment_homebak, null);
		saveUtils = new SaveUtils(this.getActivity());
		banerlistbak = new ArrayList<BannerlistBean>();
		bannerbtnlist = new ArrayList<BannerlistBean>();
		funditonlist = new ArrayList<BannerlistBean>();
		parms = new HashMap<String, String>();
		home_grid_top = (GridView) view.findViewById(R.id.home_grid_top);
		home_grid_bottom = (com.edaixi.view.ExpandableHeightGridView) view
				.findViewById(R.id.home_grid_bottom);
		home_grid_bottom.setExpanded(true);
		pager = (AutoScrollViewPager) view.findViewById(R.id.pager);

		// 魔窗初始化轮播图
		marketingHelper = MarketingHelper.currentMarketing(getActivity());
		Settings.setDebugMode(true);
		pager.setAdapter(new FragmentAdapter(getFragmentManager(),
				GetMagicWindowCount(), banerlistbak));
		indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		mTextViewShowTips = (TextView) view
				.findViewById(R.id.tv_show_newuser_tips);
		loction_text = (TextView) view.findViewById(R.id.loction_text);
		home_servesarea_text = (TextView) view
				.findViewById(R.id.home_servesarea_text);
		if (saveUtils.getStrSP(KeepingData.User_City) != "") {
			loction_text.setText(saveUtils.getStrSP(KeepingData.User_City));
		} else {
			loction_text.setText("北京");
			saveUtils.saveStrSP(KeepingData.User_City, "北京");
			saveUtils.saveStrSP(KeepingData.User_City_Id, "1");
		}

		judgeIsChangeCity();
		initView();
		mHandler.sendEmptyMessageDelayed(0, 100);
		turnGPSOn();
		return view;
	}

	// 获取魔窗
	public ArrayList<BannerlistBean> GetMagicWindowCount() {
		ArrayList<BannerlistBean> MagicLists = new ArrayList<BannerlistBean>();
		if (marketingHelper != null) {
			if (marketingHelper.isActive("6BL9VZ2Q")) {
				BannerlistBean magicWindowBeanOne = new BannerlistBean();
				magicWindowBeanOne.setMagicIndex(1);
				magicWindowBeanOne.setMagicIndexKey("6BL9VZ2Q");
				MagicLists.add(magicWindowBeanOne);
			}
			if (marketingHelper.isActive("28LWZJGD")) {
				BannerlistBean magicWindowBeanTwo = new BannerlistBean();
				magicWindowBeanTwo.setMagicIndex(2);
				magicWindowBeanTwo.setMagicIndexKey("28LWZJGD");
				MagicLists.add(magicWindowBeanTwo);
			}
			if (marketingHelper.isActive("VC1GNXXD")) {
				BannerlistBean magicWindowBeanThree = new BannerlistBean();
				magicWindowBeanThree.setMagicIndex(3);
				magicWindowBeanThree.setMagicIndexKey("VC1GNXXD");
				MagicLists.add(magicWindowBeanThree);
			}
		}
		return MagicLists;
	}

	public class FragmentAdapter extends FragmentPagerAdapter implements
			IconPagerAdapter {

		private int mCount = 2;
		ArrayList<BannerlistBean> magicWindowBeans;
		ArrayList<BannerlistBean> bannerlist;

		public FragmentAdapter(FragmentManager fm, int count) {
			super(fm);
			mCount = count;
		}

		public FragmentAdapter(FragmentManager fm,
				ArrayList<BannerlistBean> magicWindowBeans,
				ArrayList<BannerlistBean> bannerlist) {
			super(fm);
			mCount = magicWindowBeans.size() + bannerlist.size();
			this.magicWindowBeans = magicWindowBeans;
			this.bannerlist = bannerlist;
		}

		@Override
		public Fragment getItem(int position) {
			if (magicWindowBeans != null) {
				return BannerFragment.newInstance(position, magicWindowBeans,
						bannerlist);
			} else {
				return BannerFragment.newInstance(position);
			}
		}

		@Override
		public int getIconResId(int index) {
			return index;
		}

		@Override
		public int getCount() {
			return mCount;
		}
	}

	private void initView() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				get_banner_list();
				get_func_button_list();
				get_banner_button_list();
			}
		}).start();
		topAdapter = new Myfuncadpter(getActivity(), bannerbtnlist, true);
		home_grid_top.setAdapter(topAdapter);
		bottomAdapter = new Myfuncadpter(getActivity(), funditonlist, false);
		home_grid_bottom.setAdapter(bottomAdapter);
		loction_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						CityListActivity.class);
				startActivityForResult(intent, Activity.RESULT_FIRST_USER);
			}
		});
		home_servesarea_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TCAgent.onEvent(getActivity(), "首页_服务介绍");
				Intent intent = new Intent(getActivity(), WebActivity.class);
				BannerlistBean bannerlistBean = new BannerlistBean();
				bannerlistBean.setTitle("服务介绍");
				bannerlistBean.setUrl("http://edaixi.com/service?city="
						+ saveUtils.getStrSP(KeepingData.User_City_Id));
				intent.putExtra("bannerlistbean", bannerlistBean);
				startActivity(intent);
			}
		});

		if ((AppConfig.getInstance().isLocationFail() && !saveUtils
				.getBoolSP(KeepingData.is_Show_Location_Guide))
				|| (AppConfig.getInstance().isIsopendefaultcity() && !saveUtils
						.getBoolSP(KeepingData.is_Show_Location_Guide))) {
			if (saveUtils.getBoolSP(KeepingData.is_Open_App)) {
				Intent intent = new Intent(getActivity(),
						CityListActivity.class);
				startActivityForResult(intent, Activity.RESULT_FIRST_USER);
				saveUtils.saveBoolSP(KeepingData.is_Open_App, false);
				saveUtils.saveBoolSP(KeepingData.is_Show_Location_Guide, true);
			}
		} else {
			if (!saveUtils.getBoolSP(KeepingData.is_Show_Home_Guide)) {
				Animation translateAnimation = new TranslateAnimation(0f, 0f,
						0.1f, 15.0f);
				translateAnimation.setDuration(1000);
				translateAnimation.setRepeatCount(-1);
				translateAnimation.setRepeatMode(Animation.REVERSE);
				mTextViewShowTips.setVisibility(View.VISIBLE);
				mTextViewShowTips.setAnimation(translateAnimation);
				saveUtils.saveBoolSP(KeepingData.is_Show_Home_Guide, true);
			}
		}
	}

	public static TextView getloctionview() {
		return loction_text;
	}

	private void get_func_button_list() {
		parms.clear();
		parms.put("width",
				String.valueOf(DensityUtil.getWidthInPx(getActivity())));
		parms.put("height",
				String.valueOf(DensityUtil.getHeightInPx(getActivity())));
		if (saveUtils.getStrSP(KeepingData.User_City_Id) != null) {
			parms.put("city_id", saveUtils.getStrSP(KeepingData.User_City_Id));
		} else {
			parms.put("city_id", "1");
		}
		if (saveUtils.getStrSP("user_id") != null
				&& saveUtils.getStrSP("user_id") != "") {
			parms.put("user_id", saveUtils.getStrSP("user_id"));
		}
		((MainActivity) getActivity()).getdate(parms,
				Constants.GET_FUNC_BUTTON_LIST, handler, GETFUNCTIONBTNSUCCED,
				GETFUNCTIONBTNFAILD, false, false, false);
	}

	private void get_banner_button_list() {
		parms.clear();
		parms.put("width",
				String.valueOf(DensityUtil.getWidthInPx(getActivity())));
		parms.put("height",
				String.valueOf(DensityUtil.getHeightInPx(getActivity())));
		if (saveUtils.getStrSP(KeepingData.User_City_Id) != null) {
			parms.put("city_id", saveUtils.getStrSP(KeepingData.User_City_Id));
		} else {
			parms.put("city_id", "1");
		}
		if (saveUtils.getStrSP("user_id") != null
				&& saveUtils.getStrSP("user_id") != "") {
			parms.put("user_id", saveUtils.getStrSP("user_id"));
		}
		((MainActivity) getActivity()).getdate(parms,
				Constants.GET_BANNER_BUTTON_LIST, handler,
				GETBANNERBUTTONSUCCED, GETBANNERBUTTONFAILD, false, false,
				false);
	}

	private void get_banner_list() {
		parms.clear();
		parms.put("width",
				String.valueOf(DensityUtil.getWidthInPx(getActivity())));
		parms.put("height",
				String.valueOf(DensityUtil.getHeightInPx(getActivity())));
		if (saveUtils.getStrSP(KeepingData.User_City_Id) != null) {
			parms.put("city_id", saveUtils.getStrSP(KeepingData.User_City_Id));
		} else {
			parms.put("city_id", "1");
		}
		if (saveUtils.getStrSP("user_id") != null
				&& saveUtils.getStrSP("user_id") != "") {
			parms.put("user_id", saveUtils.getStrSP("user_id"));
		}
		((MainActivity) getActivity()).getdate(parms,
				Constants.GET_BANNER_LIST, handler, GETBANNERSUCCED,
				GETBANNERFAILD, false, false, false);

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (NetUtil.getNetworkState(getActivity())) {
				if (!RequstFlag && is_Get_Binner) {
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									get_banner_list();
									get_func_button_list();
									get_banner_button_list();
									is_Get_Binner = true;
								}
							});
						}
					}, 200);
				}
			}
		}
	}

	/** 解析banner结果方法 **/
	protected void paserbaner(String json,
			ArrayList<BannerlistBean> bannerlist, final String locationStr) {
		JSONArray jsonArray;
		bannerlist.clear();
		try {
			jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				str = (JSONObject) jsonArray.opt(i);
				BannerlistBean bannerlistbean = new BannerlistBean();
				bannerlistbean.setImage_url(str.getString("image_url"));
				bannerlistbean.setUrl_type(str.getString("url_type"));
				bannerlistbean.setUrl(str.getString("url"));
				bannerlistbean.setTitle(str.getString("title"));
				if (str.has("inner_url")) {
					bannerlistbean.setInner_url(str.getString("inner_url"));
					bannerlistbean.setInner_type(str.getString("inner_type"));
					bannerlistbean.setInner_title(str.getString("inner_title"));
				}
				bannerlist.add(bannerlistbean);
			}

			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					switch (locationStr) {
					case "Top":
						pager.setAdapter(new FragmentAdapter(
								getFragmentManager(), GetMagicWindowCount(),
								banerlistbak));
						pager.startAutoScroll(3000);
						indicator.setViewPager(pager);
						break;
					case "Middle":
						topAdapter.mfunditonlist = bannerbtnlist;
						topAdapter.notifyDataSetChanged();
						break;
					case "Bottom":
						bottomAdapter.mfunditonlist = funditonlist;
						bottomAdapter.notifyDataSetChanged();
						break;
					}
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void turnGPSOn() {
		if (!isOPen(getActivity())) {
			Toast.makeText(getActivity(), "您还没有开启GPS，请去设置里面打开.",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * 
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean isOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}
		return false;
	}

	/** 判断用户是否需要切换城市，弹框提示 **/
	public void judgeIsChangeCity() {
		if ((saveUtils.getStrSP(KeepingData.User_City) != "")
				&& (saveUtils.getStrSP(KeepingData.User_City_New) != "")) {
			if (saveUtils.getStrSP(KeepingData.User_City) != saveUtils
					.getStrSP(KeepingData.User_City_New)) {
				AlertDialog.Builder eixtdialog = new Builder(getActivity());
				eixtdialog.setMessage("您当前位置为"
						+ saveUtils.getStrSP(KeepingData.User_City_New)
						+ ",是否切换城市?");
				eixtdialog.setPositiveButton("是",
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 执行切换城市逻辑
								loction_text.setText(saveUtils
										.getStrSP(KeepingData.User_City_New));
								saveUtils
										.saveStrSP(
												KeepingData.User_City,
												saveUtils
														.getStrSP(KeepingData.User_City_New));
								saveUtils
										.saveStrSP(
												KeepingData.User_City_Id,
												saveUtils
														.getStrSP(KeepingData.USER_City_Id_New));
								saveUtils.saveStrSP(KeepingData.User_City_New,
										"");
								saveUtils.saveStrSP(
										KeepingData.USER_City_Id_New, "");
								saveUtils.saveStrSP(
										KeepingData.select_City_Item, "");
								saveUtils.saveBoolSP(
										KeepingData.is_Select_City_Item, false);
								get_banner_list();
								get_func_button_list();
								get_banner_button_list();
							}

						}).setNegativeButton("否", null);
				eixtdialog.show();

			}
		}

		if (!saveUtils.getBoolSP(KeepingData.is_First_In)
				&& saveUtils.getBoolSP(KeepingData.is_Show_Home_Guide)
				&& AppConfig.getInstance().isIsopendefaultcity()) {
			TipsDialog tips = new TipsDialog(getActivity(),
					R.style.customdialog_style, R.layout.tips_dialog);
			tips.show();
		}
		saveUtils.saveBoolSP(KeepingData.is_First_In, true);
	}

	/** 回调用户选中城市结果方法 **/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (saveUtils.getStrSP(KeepingData.User_City) != "") {
			loction_text.setText(saveUtils.getStrSP(KeepingData.User_City));
			get_banner_list();
			get_func_button_list();
			get_banner_button_list();
		}
		if (saveUtils.getBoolSP(KeepingData.is_Show_Location_Guide)
				&& !saveUtils.getBoolSP(KeepingData.is_Show_Home_Guide)) {
			Animation translateAnimation = new TranslateAnimation(0f, 0f, 0.1f,
					15.0f);
			translateAnimation.setDuration(1000);
			translateAnimation.setRepeatCount(-1);
			translateAnimation.setRepeatMode(Animation.REVERSE);
			mTextViewShowTips.setVisibility(View.VISIBLE);
			mTextViewShowTips.setAnimation(translateAnimation);
			saveUtils.saveBoolSP(KeepingData.is_Show_Home_Guide, true);
		}
	}
}
