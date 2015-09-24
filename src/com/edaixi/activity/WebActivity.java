package com.edaixi.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.edaixi.Enum.WebPageType;
import com.edaixi.modle.BannerlistBean;
import com.edaixi.util.LogUtil;
import com.edaixi.util.SaveUtils;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

public class WebActivity extends BaseActivity {
	private TextView mTitleTextView = null;
	private ProgressBar mProgressBar = null;
	private WebView mWebView = null;

	private WebPageType mType;

	private CommonCallBackListener mCommonListener = null;

	private TextView web_add_btn;
	private SaveUtils saveUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web);
		init(this);
		mCommonListener = new CommonCallBackListener();
		findViewById(R.id.activity_web_back_btn).setOnClickListener(
				mCommonListener);
		mTitleTextView = (TextView) findViewById(R.id.activity_web_title);
		mProgressBar = (ProgressBar) findViewById(R.id.activity_web_progressbar);
		web_add_btn = (TextView) findViewById(R.id.web_add_btn);
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.setWebChromeClient(new MyWebChromeClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		Bundle mBundle = getIntent().getExtras();
		saveUtils = new SaveUtils(getContext());
		if (getIntent().getSerializableExtra("bannerlistbean") != null) {
			final BannerlistBean bannerlistbean = (BannerlistBean) getIntent()
					.getSerializableExtra("bannerlistbean");
			mTitleTextView.setText(bannerlistbean.getTitle());
			mWebView.loadUrl(bannerlistbean.getUrl());
		} else {
			try {
				mType = WebPageType.ofWebPageType(mBundle.getInt("TYPE", 0));
				if (mType != null) {
					mWebView.loadUrl(mType.getUrl());
					mTitleTextView.setText(mType.getTitle());
				} else {
					if (getIntent().getExtras() != null
							&& getIntent().getExtras().getBoolean("couponinfo")) {
						mWebView.loadUrl("http://www.edaixi.cn/pages/coupon_des");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("WebActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("WebActivity"); // 保证 onPageEnd 在onPause 之前调用,因为
												// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return true;
	}

	/**
	 * 处理Url中链接的响应模式
	 */
	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	/**
	 * 处理打开link过程中的进度回调
	 */
	private class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				mProgressBar.setVisibility(View.INVISIBLE);
			} else {
				mProgressBar.setVisibility(View.VISIBLE);
				mProgressBar.setProgress(newProgress);
			}

		}

	}

	/**
	 * 通用回调监听器
	 */
	private class CommonCallBackListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_web_back_btn:
				finish(0, 0);
				break;
			default:
				break;
			}
		}
	}

}
