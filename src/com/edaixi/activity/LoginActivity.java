package com.edaixi.activity;

import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.util.Constants;
import com.edaixi.util.NetUtil;
import com.edaixi.util.SaveUtils;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private Button authcode_btn;
	private MyCount mc;
	private Handler handler;
	private com.edaixi.view.CleanEditText phone_edit;
	private com.edaixi.view.CleanEditText auth_code_edit;
	private NetUtil netUtil;
	private TextView login_btn;
	private TextView server_tel;
	private static final int ERRORSMS = 3;
	private AppConfig mAppConfig;
	private SaveUtils saveutils;
	private String phone_editstr1;
	private String auth_code_editstr;
	private String phone_editstr;
	private String input_phonenum;
	private boolean is_get_authcode = false;
	private boolean is_get_login = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		init(this);
		initview();
	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("LoginActivity");
		MobclickAgent.onResume(this);
		if (input_phonenum != null) {
			phone_edit.setText(input_phonenum);
		}
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("LoginActivity");
		MobclickAgent.onPause(this);
	}

	@SuppressLint("HandlerLeak")
	private void initview() {
		saveutils = new SaveUtils(getContext());
		mAppConfig = AppConfig.getInstance();
		netUtil = new NetUtil(getContext());
		authcode_btn = (Button) findViewById(R.id.authcode_btn);
		authcode_btn.setOnClickListener(this);
		login_btn = (TextView) findViewById(R.id.login_btn);
		login_btn.setOnClickListener(this);
		phone_edit = (com.edaixi.view.CleanEditText) findViewById(R.id.phone_edit);
		phone_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().length() != 11) {
					is_get_authcode = false;
					authcode_btn
							.setBackgroundResource(R.drawable.get_authcode_btn_bg);
				} else {
					is_get_authcode = true;
					input_phonenum = s.toString().trim();
					authcode_btn.setBackgroundResource(R.drawable.login_btn_bg);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		phone_edit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					phone_edit.hintCleanLogo();
				}
			}
		});
		auth_code_edit = (com.edaixi.view.CleanEditText) findViewById(R.id.auth_code_edit);
		auth_code_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().length() != 4) {
					is_get_login = false;
					login_btn
							.setBackgroundResource(R.drawable.get_authcode_btn_bg);
				} else {
					if (phone_edit.getText().toString().length() == 11) {
						is_get_login = true;
						login_btn
								.setBackgroundResource(R.drawable.login_btn_bg);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		auth_code_edit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					auth_code_edit.hintCleanLogo();
				}
			}
		});
		server_tel = (TextView) findViewById(R.id.server_tel);
		server_tel.setOnClickListener(this);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ERRORSMS:
					if (msg.obj != null) {
						showdialog("验证码获取失败:" + msg.obj);
					}
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// 获取验证码
		case R.id.authcode_btn:
			if (is_get_authcode) {
				phone_editstr = phone_edit.getText().toString();
				if (TextUtils.isEmpty(phone_editstr)) {
					return;
				}
				if (!isHasNet()) {
					showdialog("网络无连接,稍后重试");
					return;
				}
				if (phone_editstr.length() != 11) {
					return;
				}
				mc = new MyCount(60000, 1000);
				mc.start();
				authcode_btn.setEnabled(false);
				authcode_btn.setFocusable(false);

				new Thread(new Runnable() {
					public void run() {
						String getsms = netUtil.getsms(phone_editstr.toString());
						if (!getsms.equals("true")) {
							Message message = Message.obtain();
							message.what = 3;
							message.obj = getsms;
							handler.sendEmptyMessage(ERRORSMS);
						}
					}
				}).start();
			}
			break;
		case R.id.login_btn:
			// 登录
			if (true) {
				phone_editstr1 = phone_edit.getText().toString();
				auth_code_editstr = auth_code_edit.getText().toString();
				if (TextUtils.isEmpty(phone_editstr1)) {
					// showdialog("手机号码不能为空");
					return;
				}
				if (TextUtils.isEmpty(auth_code_editstr)) {
					// showdialog("验证码不能为空");
					return;
				}
				;
				if (phone_edit.getText().toString().length() != 11) {
					// showdialog("手机号码格式不正确");
					return;
				}
				if (!isHasNet()) {
					showdialog("登录失败,网络无连接");
					return;
				}
				if (auth_code_editstr.length() != 4) {
					// showdialog("请输入正确验证码");
					return;
				}
				Thread thread = new Thread(new Runnable() {
					public void run() {
						final String getlogin = netUtil.getlogin(
								phone_editstr1, auth_code_editstr,
								Constants.avoskey);
						runOnUiThread(new Runnable() {
							public void run() {
								if (getlogin.equals("true")) {
									TalkingDataAppCpa.onLogin(phone_edit
											.getText().toString().trim());
									Intent intent = new Intent(getActivity(),
											MainActivity.class);
									saveutils.saveBoolSP(KeepingData.LOGINED,
											true);
									mAppConfig.setIslogin(true);
									saveutils.saveStrSP(KeepingData.PHONE,
											phone_edit.getText().toString()
													.trim());
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);
								} else {
									AppConfig.getInstance();
									if (getlogin.equals(AppConfig.ALREADYLOGIN)) {
										showdialog(getlogin);
									} else {
										showdialog(getlogin);
									}

								}
							}
						});
					}
				});
				thread.start();
			}
			break;
		case R.id.server_tel:
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ "4008187171"));
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected boolean onBackKeyDown() {
		if (getIntent().getExtras() != null
				&& getIntent().getExtras().getBoolean("iscanback")) {
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
		}
		finish(0, 0);
		return true;
	}

	private class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			authcode_btn.setText(millisUntilFinished / 1000 + "s");
			authcode_btn.setBackgroundResource(R.drawable.verification_press);

		}

		@Override
		public void onFinish() {
			authcode_btn.setEnabled(true);
			authcode_btn.setFocusable(true);
			authcode_btn.setBackgroundResource(R.drawable.verification_default);
			authcode_btn.setText("获取验证码");
		}

	}
}
