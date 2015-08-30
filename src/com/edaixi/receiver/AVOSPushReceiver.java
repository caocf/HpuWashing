package com.edaixi.receiver;

import java.util.Date;
import org.json.JSONObject;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.avos.avoscloud.AVOSCloud;
import com.edaixi.activity.CouponActivity;
import com.edaixi.activity.DepositActivity;
import com.edaixi.activity.MainActivity;
import com.edaixi.activity.R;

public class AVOSPushReceiver extends BroadcastReceiver {

	Intent resultIntent;
	String message;
	String channel;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			if (intent.getAction().equals("com.edaixi.activity")) {
				JSONObject json = new JSONObject(intent.getExtras().getString(
						"com.avos.avoscloud.Data"));
				if (json.has("alert")) {
					message = json.getString("alert");
				}
				if (json.has("channels")) {
					channel = json.getString("channels");
				}
				if ((channel != null) && channel.equals("CouponActivity")) {
					resultIntent = new Intent(AVOSCloud.applicationContext,
							CouponActivity.class);
				} else if ((channel != null)
						&& channel.equals("DepositActivity")) {
					resultIntent = new Intent(AVOSCloud.applicationContext,
							DepositActivity.class);
				} else {
					resultIntent = new Intent(AVOSCloud.applicationContext,
							MainActivity.class);
				}
				PendingIntent pendingIntent = PendingIntent.getActivity(
						AVOSCloud.applicationContext, 0, resultIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
						AVOSCloud.applicationContext)
						.setContentTitle(
								AVOSCloud.applicationContext.getResources()
										.getString(R.string.app_name))
						.setContentText(message).setTicker(message)
						.setWhen(new Date().getTime());

				mBuilder.setSmallIcon(R.drawable.ic_launcher);
				mBuilder.setContentIntent(pendingIntent);
				mBuilder.setAutoCancel(true);
				mBuilder.setLights(0xff0000ff, 300, 0);

				int mNotificationId = 10086;
				NotificationManager mNotifyMgr = (NotificationManager) AVOSCloud.applicationContext
						.getSystemService(Context.NOTIFICATION_SERVICE);
				mNotifyMgr.notify(mNotificationId, mBuilder.build());
			}
		} catch (Exception e) {
		}
	}
}
