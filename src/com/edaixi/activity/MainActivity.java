/**
 * @MainActivity：
 * @data：
 * @date：2014-12-26 20:22:29
 * @author wei-spring
 */
package com.edaixi.activity;

import java.io.File;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.edaixi.activity.OpenCityModleDao.Properties;
import com.edaixi.adapter.MyViewPagerAdapter;
import com.edaixi.data.AppConfig;
import com.edaixi.data.EdaixiApplication;
import com.edaixi.data.KeepingData;
import com.edaixi.fragment.HomeFragment;
import com.edaixi.fragment.HomeFragmentBak;
import com.edaixi.fragment.MineFragment;
import com.edaixi.fragment.OrderFragment;
import com.edaixi.util.LogUtil;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.util.SaveUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.event.EventBus;

@SuppressWarnings("deprecation")
public class MainActivity extends BaseActivity implements OnClickListener,
		OnPageChangeListener {
	public static File mFilePath;
	private SaveUtils saveUtils;
	@ViewInject(R.id.mviewpager)
	private ViewPager vp;
	@ViewInject(R.id.rl_main_washing)
	private RelativeLayout rl_main_washing;
	@ViewInject(R.id.rl_main_order)
	private RelativeLayout rl_main_order;
	@ViewInject(R.id.rl_main_mine)
	private RelativeLayout rl_main_mine;
	@ViewInject(R.id.iv_main_washing)
	private ImageView iv_main_washing;
	@ViewInject(R.id.iv_main_order)
	private ImageView iv_main_order;
	@ViewInject(R.id.iv_main_mine)
	private ImageView iv_main_mine;
	private MyViewPagerAdapter mAdapter;
	private long mExitTime;
	private Fragment[] fms_1s;
	private OpenCityModleDao openCityModleDao;
	private DaoSession newSession;

	@Override
	public void addactivity() {
		super.addactivity();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 订阅频道，当该频道消息到来的时候，打开对应的 Activity
		// PushService.setDefaultPushCallback(this, MainActivity.class);
		// PushService.subscribe(this, "public", DepositActivity.class);
		// PushService.subscribe(this, "private", PayActivity.class);

		// greendao数据缓存
		newSession = EdaixiApplication.getDaoSession(this);
		openCityModleDao = newSession.getOpenCityModleDao();

		saveUtils = new SaveUtils(MainActivity.this);
		judgeCityIsAviable();

		// 友盟更新逻辑
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		prepare4UmengUpdate();
		// 友盟更新逻辑
		ViewUtils.inject(this);
		init(this);

		EventBus.getDefault().register(this);
		vp.setOffscreenPageLimit(3);
		mAdapter = null;
		fms_1s = new Fragment[3];
		//fms_1s[0] = new HomeFragment();
		fms_1s[0] = new HomeFragmentBak();
		fms_1s[1] = new OrderFragment();
		fms_1s[2] = new MineFragment();
		mAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), fms_1s,
				"true");
		rl_main_washing.setOnClickListener(this);
		rl_main_order.setOnClickListener(this);
		rl_main_mine.setOnClickListener(this);
		iv_main_washing.setImageResource(R.drawable.wash_press_icon);
		vp.setAdapter(mAdapter);
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {

				switch (position) {
				case 0:
					iv_main_washing
							.setImageResource(R.drawable.wash_press_icon);
					iv_main_order
							.setImageResource(R.drawable.order_default_icon);
					iv_main_mine.setImageResource(R.drawable.my_default_icon);
					break;
				case 1:
					iv_main_washing
							.setImageResource(R.drawable.wash_default_icon);
					iv_main_order.setImageResource(R.drawable.order_press_icon);
					iv_main_mine.setImageResource(R.drawable.my_default_icon);
					break;
				case 2:
					iv_main_washing
							.setImageResource(R.drawable.wash_default_icon);
					iv_main_order
							.setImageResource(R.drawable.order_default_icon);
					iv_main_mine.setImageResource(R.drawable.my_press_icon);
					break;
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			};
		});
	}

	
	private void prepare4UmengUpdate() {
		MobclickAgent.updateOnlineConfig(this);
		// 获取友盟在线参数
		String UpdateParas = MobclickAgent.getConfigParams(this, "UpdateParas");
		LogUtil.e("获取友盟在线参数"+UpdateParas);
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.forceUpdate(MainActivity.this);
		if (TextUtils.isEmpty(UpdateParas)) {
			return;
		}
		// 转换为数组
		String[] UpdateParasArray = UpdateParas.split(";");
		UmengUpdateAgent.update(this); // 调用umeng更新接口
		String curr_version_name = null;
		try {
			curr_version_name = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String aimString = null;
		for (String string : UpdateParasArray) {
			if (string.contains(curr_version_name)) {
				aimString = string;
			}
		}
		final String aimStrings = aimString;
		// 对话框按键的监听，对于强制更新的版本，如果用户未选择更新的行为，关闭app
		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {

			@Override
			public void onClick(int status) {
				switch (status) {
				case UpdateStatus.Update:
					break;
				case UpdateStatus.NotNow:
					if (aimStrings != null && aimStrings.contains("F")) {
						finish();
					}
					break;
				}
			}
		});

	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("MainActivity");
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("MainActivity");
		MobclickAgent.onPause(this);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_main_washing:
			TCAgent.onEvent(MainActivity.this, "底部导航栏_首页");
			vp.setCurrentItem(0);
			iv_main_washing.setImageResource(R.drawable.wash_press_icon);
			iv_main_order.setImageResource(R.drawable.order_default_icon);
			iv_main_mine.setImageResource(R.drawable.my_default_icon);
			break;
		case R.id.rl_main_order:
			TCAgent.onEvent(MainActivity.this, "底部导航栏_订单");
			vp.setCurrentItem(1);
			iv_main_washing.setImageResource(R.drawable.wash_default_icon);
			iv_main_order.setImageResource(R.drawable.order_press_icon);
			iv_main_mine.setImageResource(R.drawable.my_default_icon);
			EventBus.getDefault().post(
					new OrderListAdapterEvent("TopServingOrderList"));
			break;
		case R.id.rl_main_mine:
			TCAgent.onEvent(MainActivity.this, "底部导航栏_我的");
			vp.setCurrentItem(2);
			iv_main_washing.setImageResource(R.drawable.wash_default_icon);
			iv_main_order.setImageResource(R.drawable.order_default_icon);
			iv_main_mine.setImageResource(R.drawable.my_press_icon);
			break;
		default:
			break;
		}
	}

	@Override
	protected boolean onBackKeyDown() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			finish(0, 0);
		}
		return true;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	public void onEvent(OrderListAdapterEvent event) {
		switch (event.getText()) {
		case "OrderSucess":
			vp.setCurrentItem(1);
			EventBus.getDefault().post(
					new OrderListAdapterEvent("RefeshServingOrderList"));
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
	}

	// 与数据库匹配，定位城市是否可用
	public void judgeCityIsAviable() {
		QueryBuilder<OpenCityModle> qb = openCityModleDao.queryBuilder();

		if (saveUtils.isContainsStrSP(KeepingData.User_City)
				&& saveUtils.getStrSP(KeepingData.User_City) != "") {
			qb.where(Properties.Name.eq(saveUtils
					.getStrSP(KeepingData.User_City)));
			if (qb.list().size() <= 0) {
				saveUtils.saveStrSP(KeepingData.User_City, "北京");
				saveUtils.saveStrSP(KeepingData.User_City_Id, "1");
			} else {
				saveUtils.saveStrSP(KeepingData.User_City_Id, qb.list().get(0)
						.getId()
						+ "");
			}
		}

		QueryBuilder<OpenCityModle> qbtwo = openCityModleDao.queryBuilder();
		if (saveUtils.isContainsStrSP(KeepingData.User_City_New)
				&& saveUtils.getStrSP(KeepingData.User_City_New) != "") {
			qbtwo.where(Properties.Name.eq(saveUtils
					.getStrSP(KeepingData.User_City_New)));
			if (qbtwo.list().size() <= 0) {
				saveUtils.saveStrSP(KeepingData.User_City_New, "");
			} else {
				saveUtils.saveStrSP(KeepingData.USER_City_Id_New, qbtwo.list()
						.get(0).getId()
						+ "");
			}
		}

		QueryBuilder<OpenCityModle> qbthree = openCityModleDao.queryBuilder();
		if (saveUtils.isContainsStrSP(KeepingData.Current_City)
				&& saveUtils.getStrSP(KeepingData.Current_City) != "") {
			qbthree.where(Properties.Name.eq(saveUtils
					.getStrSP(KeepingData.Current_City)));
			if (qbthree.list().size() <= 0) {
				AppConfig.getInstance().setIsopendefaultcity(true);
				saveUtils.saveBoolSP(KeepingData.IS_CITY_AVAILABLE, false);
			} else {
				saveUtils.saveBoolSP(KeepingData.IS_CITY_AVAILABLE, true);
				AppConfig.getInstance().setIsopendefaultcity(false);
			}
		}
	}
}
