package com.edaixi.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.edaixi.modle.ClothingOrderInfo;
import com.edaixi.modle.OrderDeliveryInfo;
import com.edaixi.modle.OrderListItemBean;

public class ParseOrderList {
	private ArrayList<OrderListItemBean> parseOrderListRes;
	private ArrayList<OrderDeliveryInfo> parseOrderDeliveryRes;
	private ArrayList<ClothingOrderInfo> parseClothingyRes;
	private OrderListItemBean orderItemInfo;

	/***
	 * parse data from service,order info
	 * 
	 * @param orderData
	 * @return
	 * @throws
	 * @author wei-spring
	 */
	public ArrayList<OrderListItemBean> parseOrderList(String orderData) {
		parseOrderListRes = new ArrayList<OrderListItemBean>();
		if (orderData.substring(7, 11).equals("true")) {
			try {
				String data = new JSONObject(orderData).getString("data");
				JSONArray jsonArray = new JSONArray(data);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jo = (JSONObject) jsonArray.opt(i);
					OrderListItemBean oItembean = new OrderListItemBean();
					oItembean.setOrder_id(jo.getString("order_id"));
					oItembean.setOrder_sn(jo.getString("order_sn"));
					if (jo.has("yuyue_qujian_time")) {
						oItembean.setYuyue_qujian_time(jo
								.getString("yuyue_qujian_time"));
					}
					oItembean.setPay_status_text(jo
							.getString("pay_status_text"));
					oItembean.setCategory_id(jo.getString("category_id"));
					oItembean.setCan_be_canceled(jo
							.getString("can_be_canceled"));
					oItembean.setHas_clothes_detail(jo
							.getString("has_clothes_detail"));
					if (jo.has("can_be_commented")) {
						oItembean.setCan_be_commented(jo
								.getString("can_be_commented"));
					}
					if (jo.has("good")) {
						oItembean.setGood(jo.getString("good"));
					}
					oItembean.setCan_be_paid(jo.getString("can_be_paid"));
					if (jo.has("order_comments")) {
						oItembean.setOrder_comments(jo
								.getString("order_comments"));
					}
					oItembean.setDelivery_status_text(jo
							.getString("delivery_status_text"));
					if (jo.has("delivery_status")) {
						oItembean.setDelivery_status(jo
								.getString("delivery_status"));
					}
					oItembean.setOrder_status_text(jo
							.getString("order_status_text"));
					DecimalFormat df = new DecimalFormat("0.00");
					double d1 = Double.parseDouble(jo.getString("order_price"));
					oItembean.setOrder_price(df.format(d1));
					oItembean.setCoupon_sn(jo.getString("coupon_sn"));
					oItembean.setPay_status(jo.getString("pay_status"));
					oItembean.setCoupon_paid(jo.getString("coupon_paid"));
					if (jo.getString("can_be_commented").equals("1")) {
						oItembean.setWashing_score(jo.getInt("washing_score")
								+ "");
						oItembean.setLogistics_score(jo
								.getInt("logistics_score") + "");
						oItembean.setService_score(jo.getInt("service_score")
								+ "");
						oItembean.setOrder_comments(jo
								.getString("order_comments"));
					}
					if (jo.has("exclusive_channels")
							&& jo.getString("exclusive_channels") != "null") {
						oItembean.setExclusive_channels(jo
								.getString("exclusive_channels"));
					}
					if (jo.has("order_can_share")) {
						oItembean.setOrder_can_share(jo
								.getString("order_can_share"));
					}
					if (jo.has("share_url")) {
						oItembean.setShare_url(jo.getString("share_url"));
					}
					if (jo.has("share_title")) {
						oItembean.setShare_title(jo.getString("share_title"));
					}
					if (jo.has("share_content")) {
						oItembean.setShare_content(jo
								.getString("share_content"));
					}
					if (jo.has("share_image_url")) {
						oItembean.setShare_image_url(jo
								.getString("share_image_url"));
					}
					parseOrderListRes.add(oItembean);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return parseOrderListRes;

		} else {
			return null;
		}
	}

	/**
	 * parse order information from service
	 * 
	 * @param orderInfo
	 * @return
	 * @author wei-spring
	 */
	public OrderListItemBean parseOrderInfo(String orderInfo) {
		orderItemInfo = new OrderListItemBean();
		if (orderInfo.substring(7, 11).equals("true")) {
			try {
				String data = new JSONObject(orderInfo).getString("data");
				String order_id = new JSONObject(data).getString("order_id");
				orderItemInfo.setOrder_id(order_id);
				String order_sn = new JSONObject(data).getString("order_sn");
				orderItemInfo.setOrder_sn(order_sn);
				String order_status_text = new JSONObject(data)
						.getString("order_status_text");
				orderItemInfo.setOrder_status_text(order_status_text);
				String washing_date = new JSONObject(data)
						.getString("washing_date");
				orderItemInfo.setWashing_date(washing_date);
				String good = new JSONObject(data).getString("good");
				orderItemInfo.setGood(good);
				String washing_time = new JSONObject(data)
						.getString("washing_time");
				orderItemInfo.setWashing_time(washing_time);
				String order_username = new JSONObject(data)
						.getString("order_username");
				orderItemInfo.setOrder_username(order_username);
				String order_tel = new JSONObject(data).getString("order_tel");
				orderItemInfo.setOrder_tel(order_tel);
				String address_qu = new JSONObject(data)
						.getString("address_qu");
				orderItemInfo.setAddress_qu(address_qu);
				String address_song = new JSONObject(data)
						.getString("address_song");
				orderItemInfo.setAddress_qu(address_song);
				String exclusive_channels = new JSONObject(data)
						.getString("exclusive_channels");
				orderItemInfo.setExclusive_channels(exclusive_channels);
				String order_can_share = new JSONObject(data)
						.getString("order_can_share");
				orderItemInfo.setOrder_can_share(order_can_share);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return orderItemInfo;
	}

	/**
	 * parse order delivery information
	 * 
	 * @param reslutDelivery
	 * @return
	 * @author wei-spring
	 */
	public ArrayList<OrderDeliveryInfo> parseOrderDevlivery(
			String reslutDelivery) {
		parseOrderDeliveryRes = new ArrayList<OrderDeliveryInfo>();
		if (reslutDelivery.substring(7, 11).equals("true")) {
			try {
				String data = new JSONObject(reslutDelivery).getString("data");
				String Deliverydata = new JSONObject(data)
						.getString("delivery_status_list");
				JSONArray jsonArray = new JSONArray(Deliverydata);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jo = (JSONObject) jsonArray.opt(i);
					OrderDeliveryInfo orderDelivery = new OrderDeliveryInfo();
					orderDelivery.setText(jo.getString("text"));
					orderDelivery.setTime(jo.getString("time"));
					parseOrderDeliveryRes.add(orderDelivery);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return parseOrderDeliveryRes;
	}

	/***
	 * parse clothing detail for a order_id
	 * 
	 * @param reslutClothing
	 * @return
	 */
	public ArrayList<ClothingOrderInfo> parseClothingDetail(
			String reslutClothing) {
		parseClothingyRes = new ArrayList<ClothingOrderInfo>();
		if (reslutClothing.substring(7, 11).equals("true")) {
			try {
				String data = new JSONObject(reslutClothing).getString("data");
				JSONArray jsonArray = new JSONArray(data);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jo = (JSONObject) jsonArray.opt(i);
					ClothingOrderInfo clothingdetailbean = new ClothingOrderInfo();
					clothingdetailbean.setOrdersn(jo.getString("ordersn"));
					clothingdetailbean.setCloth_title(jo
							.getString("cloth_title"));
					clothingdetailbean.setColor(jo.getString("color"));
					clothingdetailbean.setOriginal_price(jo
							.getString("original_price"));
					clothingdetailbean.setCloth_condition(jo
							.getString("cloth_condition"));
					clothingdetailbean.setWash_result(jo
							.getString("wash_result"));
					clothingdetailbean.setCurrent_price(jo
							.getString("current_price"));
					parseClothingyRes.add(clothingdetailbean);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return parseClothingyRes;
	}

	/***
	 * judge clothing detail is choose order_id
	 * 
	 * @param reslutClothing
	 * @return
	 */
	public boolean judgeClothingDetail(String clothingRes) {
		if (clothingRes.substring(7, 11).equals("true")) {
			try {
				String data = new JSONObject(clothingRes).getString("data");
				JSONArray jsonArray = new JSONArray(data);
				if (jsonArray.length() > 0) {
					return true;
				} else {
					return false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}
}
