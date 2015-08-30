package com.edaixi.activity.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;
import com.edaixi.activity.BaseActivity;
import com.edaixi.activity.R;
import com.edaixi.util.Constants;
import com.edaixi.util.OrderListAdapterEvent;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import de.greenrobot.event.EventBus;

/**
 * wechat callback activity,wechat share callback here
 * 
 * @author wei-spring
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wxpaysucess);

		api = WXAPIFactory.createWXAPI(this, Constants.WXAPP_ID, false);
		int wxSdkVersion = api.getWXAppSupportAPI();
		if (wxSdkVersion < TIMELINE_SUPPORTED_VERSION) {
			Toast.makeText(WXEntryActivity.this, "您的微信版本太低，请升级...",
					Toast.LENGTH_LONG).show();
		}

		api.handleIntent(getIntent(), this);
		new Thread() {
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				killActivity();
			};
		}.start();
	}

	public void killActivity() {
		this.finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {

	}

	@Override
	public void onResp(BaseResp resp) {
		String result = "";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "分享到微信成功";
			EventBus.getDefault().post(
					new OrderListAdapterEvent("WxHuiDiaoSucess"));
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "分享到微信取消";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "没有授权";
			break;
		default:
			result = "未知错误";
			break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();

	}

	@Override
	protected boolean onBackKeyDown() {
		return false;
	}

}