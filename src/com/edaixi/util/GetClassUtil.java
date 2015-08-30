package com.edaixi.util;

import com.edaixi.activity.DepositActivity;
import com.edaixi.activity.PlaceorderActivity;
import com.edaixi.modle.InappUrlbean;

public class GetClassUtil {
	public static final String Kclass_Order = "order";
	public static final String Type_Create = "create";
	public static final String Kclass_Balance = "balance";
	public static final String Type_Recharge = "recharge";
	public static Class<? extends android.app.Activity> mActivity = null;

	public static Class<? extends android.app.Activity> getToclsss(
			InappUrlbean inappurlbean) {
		switch (inappurlbean.getKlass()) {
		case Kclass_Order:
			return PlaceorderActivity.class;
		case Kclass_Balance:
			return DepositActivity.class;
		}
		return mActivity;
	}
}
