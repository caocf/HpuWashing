package com.edaixi.activity;

import java.util.ArrayList;
import java.util.List;
import com.edaixi.adapter.SplashViewPagerAdapter;
import com.edaixi.fragment.LastSplashFragment;
import com.edaixi.fragment.NormalSplashFragment;
import com.edaixi.util.SaveUtils;
import com.networkbench.agent.impl.NBSAppAgent;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import com.zxinsight.MagicWindowSDK;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class SplashActivity extends BaseActivity implements OnClickListener {
	// SP文件判断是否为此版本第一次启动的键值
	private static final String KEY_FIRST_START = "FirstStart_V1.0";

	private ViewPager mViewPager = null;
	private ImageView mDotOne = null;
	private ImageView mDotTwo = null;
	private ImageView mDotThree = null;

	private List<Fragment> mViewPagerDataSet = null;
	private SplashViewPagerAdapter mViewPagerAdapter = null;

	private SaveUtils mSaveUtils = null;

	private CommonCallbackListener mCommonListener = null;

	// 标志位，为0时，表示由系统启动；不为0时表明由设置页面进入
	private int mFlag;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		init(this);
		TCAgent.init(this);
		TCAgent.setReportUncaughtExceptions(false);
		// 初始化听云监测
		// 注释听云注册
		NBSAppAgent.setLicenseKey("a795bf585e584e05bd435982c27625f7")
				.withLocationServiceEnabled(true).start(this);

		// 初始化网页打开应用的schema
		Intent intent = getIntent();
		String scheme = intent.getScheme();
		Uri uri = intent.getData();
		System.out.println("scheme:" + scheme);
		if (uri != null) {
			String host = uri.getHost();
			String dataString = intent.getDataString();
			String id = uri.getQueryParameter("id");
			String path = uri.getPath();
			String path1 = uri.getEncodedPath();
			String queryString = uri.getQuery();
		}
		// 初始化魔窗
		MagicWindowSDK.initSDK(this);
		mCommonListener = new CommonCallbackListener();
		mFlag = getIntent().getExtras() == null ? 0 : getIntent().getExtras()
				.getInt("FLAG", 0);
		mSaveUtils = new SaveUtils(this);
		if (mFlag == 0) {
			if (mSaveUtils.getBoolSP(KEY_FIRST_START)) {
				skipDestActivity();
				return;
			}
		}
		mViewPager = (ViewPager) findViewById(R.id.activity_splash_viewpager);
		mDotOne = (ImageView) findViewById(R.id.activity_splash_dot_one);
		mDotTwo = (ImageView) findViewById(R.id.activity_splash_dot_two);
		mDotThree = (ImageView) findViewById(R.id.activity_splash_dot_three);
		mViewPagerDataSet = new ArrayList<Fragment>();
		mViewPagerDataSet.add(NormalSplashFragment.newInstance(
				R.drawable.welcome_f_1, R.drawable.welcome_b_1));
		mViewPagerDataSet.add(NormalSplashFragment.newInstance(
				R.drawable.welcome_f_2, R.drawable.welcome_b_2));
		mViewPagerDataSet.add(LastSplashFragment.newInstance(
				R.drawable.welcome_f_3, R.drawable.welcome_b_3));
		mViewPagerAdapter = new SplashViewPagerAdapter(
				getSupportFragmentManager(), mViewPagerDataSet);
		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPager.setOnPageChangeListener(mCommonListener);
	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("SplashActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("SplashActivity"); // 保证 onPageEnd 在onPause
		// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	/**
	 * 跳转到目标Activity
	 */
	public void skipDestActivity() {
		if (mFlag == 0) {
			if (mSaveUtils != null) {
				mSaveUtils.saveBoolSP(KEY_FIRST_START, true);
			}
			startActivity(new Intent(SplashActivity.this, AdsActivity.class));
			SplashActivity.this.finish(0, 0);
		} else {
			// 表明此Activity是由更多页面打开进入的，无需跳转，直接结束即可
			finish();
		}
	}

	private class CommonCallbackListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				mDotOne.setImageResource(R.drawable.splash_dot_current);
				mDotTwo.setImageResource(R.drawable.splash_dot_default);
				mDotThree.setImageResource(R.drawable.splash_dot_default);
				break;
			case 1:
				mDotOne.setImageResource(R.drawable.splash_dot_default);
				mDotTwo.setImageResource(R.drawable.splash_dot_current);
				mDotThree.setImageResource(R.drawable.splash_dot_default);
				break;
			case 2:
				mDotOne.setImageResource(R.drawable.splash_dot_default);
				mDotTwo.setImageResource(R.drawable.splash_dot_default);
				mDotThree.setImageResource(R.drawable.splash_dot_current);
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
