package com.edaixi.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Changetime {
	private static SimpleDateFormat df;
	private static String dateitem;

	public static boolean changetime(String startDate, String endDate) {
		final Calendar calendar = Calendar.getInstance();
		df = new SimpleDateFormat("yyyy-MM-dd");
		dateitem = df.format(calendar.getTime());
		String replacestr = dateitem.replace("-", "");
		String startDatestr = startDate.replace("-", "");
		String endDatestr = endDate.replace("-", "");
		if ((Long.valueOf(replacestr) >= Long.valueOf(startDatestr))
				&& (Long.valueOf(replacestr) <= Long.valueOf(endDatestr))) {
			return true;
		} else {
			return false;
		}
	};
}
