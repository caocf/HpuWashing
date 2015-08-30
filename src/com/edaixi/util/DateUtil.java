package com.edaixi.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

	public static String getCouponDate(String dateString) {
		StringBuilder sBuffer = new StringBuilder(dateString);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		if (df.format(cal.getTime()).equals(sBuffer.toString())) {
			System.out.println("Today");
			sBuffer = sBuffer.insert(10, "过期(今天)  ");
		}
		cal.roll(Calendar.DAY_OF_YEAR, 1);
		if (df.format(cal.getTime()).equals(sBuffer.toString())) {
			System.out.println("Tomorrow");
			sBuffer = sBuffer.insert(10, "过期(明天)  ");
		}
		cal.roll(Calendar.DAY_OF_YEAR, 1);
		if (df.format(cal.getTime()).equals(sBuffer.toString())) {
			System.out.println("The day after");
			sBuffer = sBuffer.insert(10, "过期(后天)  ");
		}
		if (!sBuffer.toString().contains("(")) {
			sBuffer = sBuffer.insert(10, "  ");
		}
		String replace = sBuffer.toString().replace("-", ".");
		return replace;
	}
}