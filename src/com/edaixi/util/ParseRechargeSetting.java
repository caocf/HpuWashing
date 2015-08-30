package com.edaixi.util;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.edaixi.modle.RechargeSettingInfo;

public class ParseRechargeSetting {
	private ArrayList<RechargeSettingInfo> parseRechargeSettingRes;

	/***
	 * parse data from service,recharge title...
	 * 
	 * @param rechargesetting
	 *            Data
	 * @return
	 * @throws
	 * @author wei-spring
	 */
	public ArrayList<RechargeSettingInfo> parseRechargeSettingInfos(
			String rechargeSettingData) {
		parseRechargeSettingRes = new ArrayList<RechargeSettingInfo>();
		if (rechargeSettingData.substring(7, 11).equals("true")) {
			try {
				String data = new JSONObject(rechargeSettingData)
						.getString("data");
				JSONArray jsonArray = new JSONArray(data);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jo = (JSONObject) jsonArray.opt(i);
					RechargeSettingInfo rechargeSettingInfo = new RechargeSettingInfo();
					if (jo.has("id")) {
						rechargeSettingInfo.setId(jo.getString("id"));
					}
					if (jo.has("weid")) {
						rechargeSettingInfo.setWeid(jo.getString("weid"));
					}
					if (jo.has("money_give")) {
						rechargeSettingInfo.setMoney_give(jo
								.getString("money_give"));
					}
					if (jo.has("min")) {
						rechargeSettingInfo.setMin(jo.getString("min"));
					}
					if (jo.has("max")) {
						rechargeSettingInfo.setMax(jo.getString("max"));
					}
					if (jo.has("dateline")) {
						rechargeSettingInfo.setDateline(jo
								.getString("dateline"));
					}
					if (jo.has("money_giveCopy")) {
						rechargeSettingInfo.setMoney_giveCopy(jo
								.getString("money_giveCopy"));
					}
					parseRechargeSettingRes.add(rechargeSettingInfo);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return parseRechargeSettingRes;

		} else {
			return null;
		}
	}
}
