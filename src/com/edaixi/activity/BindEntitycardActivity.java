package com.edaixi.activity;

import java.lang.ref.SoftReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.edaixi.modle.BindEntityCardBean;
import com.edaixi.modle.EntityCardBean;
import com.edaixi.modle.HttpCommonBean;
import com.edaixi.util.Constants;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.util.SaveUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 绑定实体卡页面
 */
@SuppressLint("HandlerLeak")
public class BindEntitycardActivity extends BaseActivity {

	private static final int BIND_CORRECT_CODE = 10;
	private static final int BIND_ERROR_CODE = 11;
	private static final int CHECK_CORRECT_CODE = 12;
	private static final int CHECK_ERROR_CODE = 13;

	private LinearLayout mCheckFrame = null;
	private LinearLayout mBindFrame = null;

	private EditText mCardEdit = null;
	private EditText mPassEdit = null;

	private CommonCallbackListener mCommonListener = null;

	private UpdateHandler mHandler = null;

	private SaveUtils mSaveUtils = null;
	private HashMap<String, String> mParams = null;

	private Gson mGson = null;

	@Override
	protected void onCreate(Bundle mBundle) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(mBundle);
		setContentView(R.layout.activity_bindcard);
		init(this);
		mCommonListener = new CommonCallbackListener();
		mHandler = new UpdateHandler(this);
		mGson = new Gson();
		mSaveUtils = new SaveUtils(this);
		mParams = new HashMap<String, String>();
		mCheckFrame = (LinearLayout) findViewById(R.id.activity_bindcard_check);
		mBindFrame = (LinearLayout) findViewById(R.id.ll_activity_bindcard_bind);
		mCardEdit = (EditText) findViewById(R.id.activity_bindcard_card);
		mPassEdit = (EditText) findViewById(R.id.activity_bindcard_pass);
		findViewById(R.id.activity_bindcard_cancel).setOnClickListener(
				mCommonListener);
		findViewById(R.id.bindcard_bind_btn)
				.setOnClickListener(mCommonListener);
		findViewById(R.id.activity_bingcard_back_btn).setOnClickListener(
				mCommonListener);
		mParams.clear();
		mParams.put("user_id", mSaveUtils.getStrSP("user_id"));
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return false;
	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("BindEntitycardActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("BindEntitycardActivity"); // 保证 onPageEnd
															// 在onPause 之前调用,因为
															// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	private class CommonCallbackListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_bindcard_cancel:
				break;
			case R.id.activity_bingcard_back_btn:
				finish(0,0);
				break;
			case R.id.bindcard_bind_btn:
				String mCard = mCardEdit.getText().toString().trim();
				String mPass = mPassEdit.getText().toString().trim();

				if (TextUtils.isEmpty(mCard)) {
					showdialog("请输入卡号");
					return;
				}
				if (TextUtils.isEmpty(mPass)) {
					showdialog("请输入密码");
					return;
				}
				mParams.clear();
				mParams.put("user_id", mSaveUtils.getStrSP("user_id"));
				mParams.put("sn_code", mCard);
				mParams.put("sn_password", mPass);
				postdate(mParams, Constants.getbindmembercard, mHandler,
						BIND_CORRECT_CODE, BIND_ERROR_CODE, true, true);

				break;
			}
		}

	}

	private class UpdateHandler extends Handler {

		private SoftReference<BindEntitycardActivity> mActivityRef = null;

		public UpdateHandler(BindEntitycardActivity mActivity) {
			mActivityRef = new SoftReference<BindEntitycardActivity>(mActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			BindEntitycardActivity mActivity = mActivityRef.get();
			if (mActivity == null) {
				return;
			}

			switch (msg.what) {
			case BIND_CORRECT_CODE: {
				Type contentType = new TypeToken<BindEntityCardBean>() {
				}.getType();
				BindEntityCardBean mInfo = null;

				try {
					mInfo = mActivity.mGson.fromJson((String) msg.obj,
							contentType);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				if (mInfo != null) {
					if (mInfo.isRet()) {
						showdialog("绑定成功");
						new Thread() {
							@Override
							public void run() {
								super.run();
								try {
									Thread.sleep(2002);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								EventBus.getDefault().post(
										new OrderListAdapterEvent("getprice"));
								finish(0,0);
							}
						}.start();

					} else {
						Toast.makeText(mActivity, "绑定失败 " + mInfo.getError(),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					return;
				}

				break;
			}
			case BIND_ERROR_CODE:
				showdialog("绑定失败");
				break;
			case CHECK_CORRECT_CODE: {
				Type contentType = new TypeToken<HttpCommonBean>() {
				}.getType();
				HttpCommonBean mInfo = null;
				try {
					mInfo = mActivity.mGson.fromJson((String) msg.obj,
							contentType);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				if (mInfo != null) {

					if (mInfo.isRet() && TextUtils.isEmpty(mInfo.getError())) {
						mActivity.mCheckFrame.setVisibility(View.VISIBLE);
						// mActivity.mBindFrame.setVisibility(View.GONE);

						contentType = new TypeToken<EntityCardBean>() {
						}.getType();
						@SuppressWarnings("unused")
						EntityCardBean mCardBean = null;
						try {
							mCardBean = mActivity.mGson.fromJson(
									mInfo.getData(), contentType);
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}

					} else {
						// mActivity.mCheckFrame.setVisibility(View.GONE);
						mActivity.mBindFrame.setVisibility(View.VISIBLE);
					}

				} else {
					Toast.makeText(mActivity, "数据错误", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				break;
			}
			case CHECK_ERROR_CODE:
				Toast.makeText(mActivity, "查询失败", Toast.LENGTH_SHORT).show();
				break;
			}

		}

	}

}
