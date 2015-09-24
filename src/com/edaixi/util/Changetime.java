package com.edaixi.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Changetime {
	private static SimpleDateFormat df;
	private static String dateitem;

	public static boolean changetime(String date) {
		final Calendar calendar = Calendar.getInstance();
		df = new SimpleDateFormat("yyyy-MM-dd");
		dateitem = df.format(calendar.getTime());
		String replacestr = dateitem.replace("-", "");
		String datestr = date.replace("-", "");
		if (Long.valueOf(replacestr) >= Long.valueOf(datestr)) {
			// 可用
			return true;
		} else {
			// 不可用
			return false;
		}
	};
}
