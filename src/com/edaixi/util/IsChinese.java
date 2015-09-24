package com.edaixi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsChinese {
	public static boolean iszhongwen(String mobiles) {
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(mobiles);
		if (m.find()) {
			return true;
		} else {
			return false;
		}
	}

}
