package com.edaixi.wheelpicker.widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.edaixi.modle.CitysAreaBean;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class Wheelpicker {

	private static Calendar nowTime;
	public final static String dayNames[] = { "（周日）", "（周一）", "（周二）", "（周三）",
			"（周四）", "（周五）", "（周六）" };

	public final static String[] time = { "10:00-12:00", "12:00-14:00",
			"14:00-16:00", "16:00-18:00", "18:00-20:00", "20:00-22:00",
			"22:00-24:00" };
	private static CitiesPopup cityPopup;

	public static void fillcitydate(CitysAreaBean cityAreaBean) {

	}

	public static void show(Context context, View addmanager_layout,
			TextView order_time, TextView order_date) {
		nowTime = Calendar.getInstance();
		if (nowTime.get(Calendar.HOUR_OF_DAY) > 21) {
			nowTime.add(Calendar.DATE, 1);
		}
		final String[] date_7 = new String[7];
		final SimpleDateFormat sDateFormat = new SimpleDateFormat("MM-dd");
		for (int i = 0; i < 7; i++) {
			if (i != 0) {
				nowTime.add(Calendar.DATE, 1);
			}
			int dayOfWeek = nowTime.get(Calendar.DAY_OF_WEEK);
			String weekString = dayNames[dayOfWeek - 1];
			date_7[i] = weekString + sDateFormat.format(nowTime.getTime());
		}
		final String[] hours;
		final Calendar nowhour = Calendar.getInstance();
		int hour = nowhour.get(Calendar.HOUR_OF_DAY);
		if (hour < 10 || hour > 21) {
			hours = time;
		} else {
			hours = new String[12 - hour / 2 - 1];
			for (int i = 0; i < hours.length; i++) {
				hours[i] = String.valueOf(2 * ((hour / 2 + i + 1))) + ":00-"
						+ String.valueOf(2 * (hour / 2 + i + 2)) + ":00";
			}
		}
		final String[][] hh = { hours, time, time, time, time, time, time };
		cityPopup = new CitiesPopup(context, date_7, hh, false, order_time,
				order_date);
		cityPopup.show(addmanager_layout);
	}

}
