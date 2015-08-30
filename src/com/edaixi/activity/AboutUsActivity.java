package com.edaixi.activity;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;
import com.edaixi.Enum.WebPageType;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

/**
 * 关于我们页面
 */
@SuppressLint("NewApi")
public class AboutUsActivity extends BaseActivity {
	private CommonCallbackListener mCommonListener = null;
	private Bundle mBundle = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_aboutus);
		init(this);
		mCommonListener = new CommonCallbackListener();
		mBundle = new Bundle();
		findViewById(R.id.activity_aboutus_back_btn).setOnClickListener(
				mCommonListener);
		findViewById(R.id.activity_aboutus_weixin).setOnClickListener(
				mCommonListener);
		findViewById(R.id.activity_aboutus_weibo).setOnClickListener(
				mCommonListener);
		findViewById(R.id.activity_aboutus_net).setOnClickListener(
				mCommonListener);
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return false;
	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("AboutUsActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("AboutUsActivity"); // 保证 onPageEnd 在onPause
													// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	private class CommonCallbackListener implements OnClickListener {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_aboutus_back_btn:
				onBackKeyDown();
				break;
			case R.id.activity_aboutus_weixin:
				ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				cmb.setText("e袋洗");
				Toast.makeText(AboutUsActivity.this, "复制微信公众号成功，请在微信中搜索关注.",
						Toast.LENGTH_SHORT).show();
				break;
			case R.id.activity_aboutus_weibo:
				TCAgent.onEvent(getActivity(), "关于_e袋洗微博");
				mBundle.clear();
				mBundle.putInt("TYPE", WebPageType.E_WEIBO.getType());
				startNewActivity(WebActivity.class, 0, 0, false, mBundle);
				break;
			case R.id.activity_aboutus_net:
				mBundle.clear();
				mBundle.putInt("TYPE", WebPageType.WEBSITE.getType());
				startNewActivity(WebActivity.class, 0, 0, false, mBundle);
				break;
			}
		}

	}

}
