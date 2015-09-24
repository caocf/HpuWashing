package com.edaixi.data;

import android.app.Application;
import android.content.Context;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.edaixi.activity.DaoMaster;
import com.edaixi.activity.DaoMaster.OpenHelper;
import com.edaixi.activity.DaoSession;
import com.edaixi.activity.SplashActivity;
import com.edaixi.util.Constants;
import com.edaixi.util.LogUtil;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.tendcloud.tenddata.TCAgent;

/**
 * @author wei-spring
 * @deprecated init something important...
 */
public class EdaixiApplication extends Application {

	private static EdaixiApplication mInstance;
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;

	@Override
	public void onCreate() {
		super.onCreate();

		if (mInstance == null) {
			mInstance = this;
		}
		// avoscloud 测试id ,key
		// AVOSCloud.initialize(this,
		// "bi2vv14al3bxwdqu5xmaamuy94ru3b3cjuitj4y8rnc6khx1",
		// "sq828q7rgro7p6unkbphcrg7lp1chiq82p9geurhojmjcbrt");
		AVOSCloud.initialize(this,
				"6iebvdj6vkz13nxmjf3fzniw5h37prbv0bclvrlp67i9dhsx",
				"doe8qr2p3g0x7r8ldi2judtyvsdro2jfnwkygenh251yla7a");
		AVOSCloud.useAVCloudCN();
		initPush();
		com.tendcloud.tenddata.TCAgent.init(getApplicationContext());
		TalkingDataAppCpa.init(this.getApplicationContext(),
				"277bdedbcce4457eae9da083f24c3e22", "GooglePlay");
	}

	/**
	 * 取得DaoMaster
	 * 
	 * @param context
	 * @return
	 */
	public static DaoMaster getDaoMaster(Context context) {
		if (daoMaster == null) {
			OpenHelper helper = new DaoMaster.DevOpenHelper(context,
					Constants.GreenDao_OpenCitys, null);
			daoMaster = new DaoMaster(helper.getWritableDatabase());
		}
		return daoMaster;
	}

	/**
	 * 取得DaoSession
	 * 
	 * @param context
	 * @return
	 */
	public static DaoSession getDaoSession(Context context) {
		if (daoSession == null) {
			if (daoMaster == null) {
				daoMaster = getDaoMaster(context);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}

	/**
	 * 初始化推送代码
	 */
	public void initPush() {
		PushService.setDefaultPushCallback(getApplicationContext(),
				SplashActivity.class);
		AVInstallation.getCurrentInstallation().saveInBackground(
				new SaveCallback() {
					@Override
					public void done(AVException e) {
						AVInstallation.getCurrentInstallation()
								.saveInBackground();
						if (e == null) {
							// 保存成功
							 String installationId = AVInstallation
							 .getCurrentInstallation()
							 .getInstallationId();
							 LogUtil.e("avoles------id"+installationId);
							// 关联 installationId 到用户表等操作……
						} else {
							// 保存失败，输出错误信息
						}
					}
				});
	}
}
