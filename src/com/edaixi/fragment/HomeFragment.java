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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edaixi.activity.CityListActivity;
import com.edaixi.activity.GuideHomeTipsActivity;
import com.edaixi.activity.LoginActivity;
import com.edaixi.activity.MainActivity;
import com.edaixi.activity.R;
import com.edaixi.activity.WebActivity;
import com.edaixi.adapter.Myfuncadpter;
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.BannerlistBean;
import com.edaixi.modle.HttpCommonBean;
import com.edaixi.modle.InappUrlbean;
import com.edaixi.util.Constants;
import com.edaixi.util.DensityUtil;
import com.edaixi.util.GetClassUtil;
import com.edaixi.util.LogUtil;
import com.edaixi.util.NetUtil;
import com.edaixi.util.SaveUtils;
import com.edaixi.view.MyViewPager.OnSingleTouchListener;
import com.edaixi.view.TipsDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tendcloud.tenddata.TCAgent;

@SuppressLint("HandlerLeak")
public class HomeFragment extends BaseFragment {
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
	private com.edaixi.view.MyViewPager myview_pager;
	private ArrayList<BannerlistBean> bannerbtnlist;
	private ArrayList<BannerlistBean> funditonlist;
	private JSONObject str;
	private TitleAdapter title;
	private ArrayList<BannerlistBean> banerlistbak;
	private LinearLayout pager_bottom;
	private ImageView point;
	private static TextView loction_text;
	private static TextView home_servesarea_text;
	private int currentPosition = 0;
	private boolean is_Loop_Flag = true;;
	private boolean is_Get_Binner = false;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			currentPosition++;
			if ((banerlistbak.size() > 0)
					&& (currentPosition > banerlistbak.size() - 1)) {
				currentPosition = (currentPosition % banerlistbak.size());
			}
			myview_pager.setCurrentItem(currentPosition);
			if (is_Loop_Flag) {
				mHandler.sendEmptyMessageDelayed(0, 3000);
			}
		};
	};

	private Runnable rightRunnable = new Runnable() {
		@Override
		public void run() {
			startActivity(new Intent(getActivity(), GuideHomeTipsActivity.class));
		}
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
					LogUtil.e("-111--"+msg.obj);
					if (jsonCommonBean.isRet()) {
						String data = jsonCommonBean.getData();
						paserbaner(data, banerlistbak);
						currentPosition = Integer.MAX_VALUE / 2
								- (Integer.MAX_VALUE / 2 % banerlistbak.size());
						removeAllPoint();
						addBannerPoint();
						currentPosition = 0;
						title = null;
						title = new TitleAdapter(banerlistbak);
						myview_pager.setAdapter(title);
					}
					break;
				case GETBANNERFAILD:
					RequstFlag = false;
					break;
				case GETFUNCTIONBTNSUCCED:
					RequstFlag = true;
					LogUtil.e("-12221--"+msg.obj);
					HttpCommonBean CommonBean = gson.fromJson((String) msg.obj,
							HttpCommonBean.class);
					if (CommonBean.isRet()) {
						String data = CommonBean.getData();
						paserbaner(data, funditonlist);
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
						paserbaner(data, bannerbtnlist);
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
		view = View.inflate(getActivity(), R.layout.fragment_home, null);
		saveUtils = new SaveUtils(this.getActivity());
		home_grid_top = (GridView) view.findViewById(R.id.home_grid_top);
		home_grid_bottom = (com.edaixi.view.ExpandableHeightGridView) view
				.findViewById(R.id.home_grid_bottom);
		home_grid_bottom.setExpanded(true);
		myview_pager = (com.edaixi.view.MyViewPager) view
				.findViewById(R.id.myview_pager);
		pager_bottom = (LinearLayout) view.findViewById(R.id.pager_bottom);
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
		myview_pager.setCurrentItem(currentPosition);
		myview_pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				currentPosition = position;
				if (pager_bottom.getChildCount() > 0) {
					for (int i = 0; i < banerlistbak.size(); i++) {
						pager_bottom.getChildAt(i).setBackgroundResource(
								getImageResIDs()[0]);
					}
					pager_bottom.getChildAt(position).setBackgroundResource(
							getImageResIDs()[1]);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
		});

		myview_pager.setOnSingleTouchListener(new OnSingleTouchListener() {

			@Override
			public void onSingleTouch() {
				if ((banerlistbak != null) && (banerlistbak.size() > 0)) {
					TCAgent.onEvent(getActivity(), "首页Banner_"
							+ banerlistbak.get(myview_pager.getCurrentItem())
									.getTitle());

					Intent intent = new Intent();
					if (banerlistbak.get(myview_pager.getCurrentItem())
							.getUrl_type().equals("web")) {
						intent.putExtra("bannerlistbean",
								banerlistbak.get(myview_pager.getCurrentItem()));
						intent.setClass(getActivity(), WebActivity.class);
						startActivity(intent);
					} else if (banerlistbak.get(myview_pager.getCurrentItem())
							.getUrl_type().equals("in_app")) {
						if (!saveUtils.getBoolSP(KeepingData.LOGINED)) {
							intent.setClass(getActivity(), LoginActivity.class);
							startActivity(intent);
						} else {
							Intent intentToActivity = new Intent();
							Gson gson = new Gson();
							InappUrlbean inappurlbean = gson.fromJson(
									banerlistbak.get(
											myview_pager.getCurrentItem())
											.getUrl(), InappUrlbean.class);
							intentToActivity.setClass(getActivity(),
									GetClassUtil.getToclsss(inappurlbean));
							intentToActivity.putExtra("orderId",
									inappurlbean.getId());
							startActivity(intentToActivity);
						}
					}
				}
			}
		});
		judgeIsChangeCity();
		initView();
		mHandler.sendEmptyMessageDelayed(0, 100);
		turnGPSOn();
		return view;
	}

	private void initView() {
		banerlistbak = new ArrayList<BannerlistBean>();
		bannerbtnlist = new ArrayList<BannerlistBean>();
		funditonlist = new ArrayList<BannerlistBean>();
		parms = new HashMap<String, String>();
		get_banner_list();
		get_func_button_list();
		get_banner_button_list();
		title = new TitleAdapter(banerlistbak);
		myview_pager.setAdapter(title);
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
				TCAgent.onEvent(getActivity(), "首页_服务范围");
				Intent intent = new Intent(getActivity(), WebActivity.class);
				BannerlistBean bannerlistBean = new BannerlistBean();
				bannerlistBean.setTitle("服务范围");
				bannerlistBean.setUrl("http://edaixi.com/pages/service_area");
				intent.putExtra("bannerlistbean", bannerlistBean);
				startActivity(intent);
			}
		});
		if (!saveUtils.getBoolSP(KeepingData.is_Show_Home_Guide)) {
			mHandler.postDelayed(rightRunnable, 0);
			saveUtils.saveBoolSP(KeepingData.is_Show_Home_Guide, true);
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
		((MainActivity) getActivity())
				.getdate(parms, Constants.GET_BANNER_BUTTON_LIST, handler,
						GETBANNERBUTTONSUCCED, GETBANNERBUTTONFAILD, false,
						true, false);
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
		((MainActivity) getActivity()).getdate(parms,
				Constants.GET_BANNER_LIST, handler, GETBANNERSUCCED,
				GETBANNERFAILD, true, false, false);

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
								}
							});
						}
					}, 200);
				}
			}
			is_Get_Binner = true;
		} else {
		}
	}

	private int[] getImageResIDs() {
		return new int[] { R.drawable.splash_dot_default,
				R.drawable.splash_dot_current };
	}

	private class TitleAdapter extends PagerAdapter {

		private ArrayList<BannerlistBean> mbannerlist;

		public TitleAdapter(ArrayList<BannerlistBean> bannerlist) {
			super();
			mbannerlist = bannerlist;
		}

		@Override
		public int getCount() {
			int size = mbannerlist.size() == 0 ? 2 : mbannerlist.size();
			return size;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View
					.inflate(getActivity(), R.layout.bander_title, null);
			ImageView imageView_title = (ImageView) view
					.findViewById(R.id.iv_bander_title);
			imageView_title.setScaleType(ScaleType.CENTER_CROP);
			imageView_title.setImageResource(R.drawable.banner_default);
			if (mbannerlist.size() == 0) {
				container.addView(view, 0);
				return view;
			} else {
				BitmapUtils utils = new BitmapUtils(getActivity());
				utils.display(imageView_title, mbannerlist.get(position)
						.getImage_url());
				container.addView(view, 0);
				return view;
			}
		}

	}

	/** 动态移除所有的小圆点 **/
	public void removeAllPoint() {
		pager_bottom.removeAllViews();
		pager_bottom.removeAllViewsInLayout();
	}

	/** 动态添加轮播图小圆点 **/
	protected void addBannerPoint() {
		for (int i = 0; i < banerlistbak.size(); i++) {
			point = new ImageView(getActivity());
			point.setBackgroundResource(R.drawable.splash_dot_current);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;
			point.setLayoutParams(params);
			point.setVisibility(View.VISIBLE);
			if (i == 0) {
				point.setEnabled(true);
			} else {
				point.setEnabled(false);
			}
			pager_bottom.addView(point);
		}
	}

	/** 解析banner结果方法 **/
	protected void paserbaner(String json, ArrayList<BannerlistBean> bannerlist) {
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
			topAdapter.notifyDataSetChanged();
			bottomAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void turnGPSOn() {
		if (!isOPen(getActivity())) {
			Toast.makeText(getActivity(), "您还没有开启GPS，请去设置里面打开.",
					Toast.LENGTH_SHORT).show();
			// LogUtil.e("关闭gps------------------");
			// Intent intent = new
			// Intent("android.location.GPS_ENABLED_CHANGE");
			// intent.putExtra("enabled", true);
			// getActivity().sendBroadcast(intent);
			//
			// String provider = Settings.Secure.getString(getActivity()
			// .getContentResolver(),
			// Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			// if (!provider.contains("gps")) { // if gps is disabled
			// final Intent poke = new Intent();
			// poke.setClassName("com.android.settings",
			// "com.android.settings.widget.SettingsAppWidgetProvider");
			// poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			// poke.setData(Uri.parse("3"));
			// getActivity().sendBroadcast(poke);
			// }
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
	}
}
