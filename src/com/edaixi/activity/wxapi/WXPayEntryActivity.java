package com.edaixi.activity.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;
import com.edaixi.activity.R;
import com.edaixi.data.AppConfig;
import com.edaixi.util.Constants;
import com.edaixi.util.OrderListAdapterEvent;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import de.greenrobot.event.EventBus;

/**
 * wechat pay callback activity, may fail or sucess
 * 
 * @author wei-spring
 * 
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wxpaysucess);

		api = WXAPIFactory.createWXAPI(this, Constants.WXAPP_ID);
		api.handleIntent(getIntent(), this);

		new Thread() {
			public void run() {
				try {
					Thread.sleep(300);
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
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (String.valueOf(resp.errCode).equals("-1")) {
				Toast.makeText(this, "支付错误,请稍后重试.", Toast.LENGTH_SHORT).show();
			} else if (String.valueOf(resp.errCode).equals("-2")) {
				Toast.makeText(this, "取消支付", Toast.LENGTH_SHORT).show();
			} else if (String.valueOf(resp.errCode).equals("-3")) {
				Toast.makeText(this, "发送失败,请稍后重试", Toast.LENGTH_SHORT).show();
			} else if (String.valueOf(resp.errCode).equals("0")) {
				Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
				AppConfig appConfig = AppConfig.getInstance();
				if (appConfig.isOrderWxPay()) {
					EventBus.getDefault().post(
							new OrderListAdapterEvent("WXPaySucess"));
				} else {
					EventBus.getDefault().post(
							new OrderListAdapterEvent("DepositWXPaySucess"));
				}
			}
		}
	}
}