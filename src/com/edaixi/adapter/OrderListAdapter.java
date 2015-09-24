/*
 * this adapter is for orderlist
 * by wei-spring
 */
package com.edaixi.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.edaixi.activity.AppraiseActivity;
import com.edaixi.activity.PayActivityBak;
import com.edaixi.activity.R;
import com.edaixi.modle.OrderListItemBean;
import com.tendcloud.tenddata.TCAgent;

public class OrderListAdapter extends BaseAdapter {

	public ArrayList<OrderListItemBean> orderList;
	private Context context;
	private String flag;
	@SuppressWarnings("unused")
	private int index;

	public OrderListAdapter(ArrayList<OrderListItemBean> orderList,
			Context context, String flag) {
		this.orderList = orderList;
		this.context = context;
		this.flag = flag;
	}

	@Override
	public int getCount() {
		return orderList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		index = position;
		final OrderListItemBean orderItem = orderList.get(position);
		View view;
		ViewHolder viHolder = null;
		if (convertView != null) {
			view = convertView;
			viHolder = (ViewHolder) view.getTag();
		} else {
			view = View.inflate(context, R.layout.order_list_item, null);
			viHolder = new ViewHolder();
			viHolder.order_category_logo = (ImageView) view
					.findViewById(R.id.order_category_logo);
			viHolder.order_category_text = (TextView) view
					.findViewById(R.id.order_category_text);
			viHolder.iv_order_status = (ImageView) view
					.findViewById(R.id.iv_order_status);
			viHolder.tv_order_status = (TextView) view
					.findViewById(R.id.tv_order_status);
			viHolder.tv_order_sn = (TextView) view
					.findViewById(R.id.tv_order_sn);
			viHolder.tv_order_time = (TextView) view
					.findViewById(R.id.tv_order_time);
			viHolder.tv_order_price = (TextView) view
					.findViewById(R.id.tv_order_price);
			viHolder.tv_order_pay = (TextView) view
					.findViewById(R.id.tv_order_pay);
			viHolder.tv_order_comment_tips = (TextView) view
					.findViewById(R.id.tv_order_comment_tips);
			viHolder.rl_order_item_bottom = (RelativeLayout) view
					.findViewById(R.id.rl_order_item_bottom);
			view.setTag(viHolder);
		}
		viHolder.tv_order_comment_tips.setVisibility(View.INVISIBLE);
		if (flag.equals("COMPLETEDORDER")) {
			// 已完成订单处理逻辑
			viHolder.iv_order_status.setVisibility(View.INVISIBLE);
			viHolder.tv_order_price.setText("订单总额：  "
					+ orderItem.getOrder_price() + "元");
			viHolder.tv_order_pay
					.setBackgroundResource(R.drawable.order_commeted_bg);
			viHolder.tv_order_pay.setTextColor(context.getResources().getColor(
					R.color.yijingpingjiaziti));
			if (orderItem.getCan_be_commented().equals("1")) {
				viHolder.tv_order_pay.setText("已评价");
				viHolder.tv_order_pay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						TCAgent.onEvent(context, "点击订单评价");
						Intent intent = new Intent();
						intent.setClass(context, AppraiseActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("orderItem", orderItem);
						intent.putExtras(bundle);
						context.startActivity(intent);
					}
				});
			} else {
				viHolder.tv_order_pay.setText("已过期");
				viHolder.tv_order_pay.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
			}
		} else {
			if (orderItem.getDelivery_status() != null) {
				viHolder.tv_order_status.setText(orderItem
						.getDelivery_status_text().trim());
				switch (orderItem.getDelivery_status()) {
				case "11":
				case "0":
					viHolder.iv_order_status.setVisibility(View.VISIBLE);
					viHolder.iv_order_status
							.setImageResource(R.drawable.order_status_1);
					viHolder.rl_order_item_bottom.setVisibility(View.GONE);
					viHolder.tv_order_price.setVisibility(View.GONE);
					viHolder.tv_order_pay.setVisibility(View.GONE);
					break;
				case "9":
					viHolder.iv_order_status.setVisibility(View.VISIBLE);
					viHolder.iv_order_status
							.setImageResource(R.drawable.order_status_2);
					viHolder.rl_order_item_bottom.setVisibility(View.VISIBLE);
					if (orderItem.getCan_be_paid().equals("true")) {
						viHolder.tv_order_price.setVisibility(View.VISIBLE);
						viHolder.tv_order_pay.setVisibility(View.VISIBLE);
						viHolder.tv_order_price.setText("订单总额：  "
								+ orderItem.getOrder_price() + "元");
						viHolder.tv_order_pay
								.setBackgroundResource(R.drawable.order_pay_bg);
						viHolder.tv_order_pay.setTextColor(context
								.getResources().getColor(R.color.fukuanziti));
						viHolder.tv_order_pay.setText("付款");
						viHolder.tv_order_pay
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										Intent intent = new Intent();
										intent.setClass(context,
												PayActivityBak.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("PayOrderItem",
												orderItem);
										intent.putExtras(bundle);
										context.startActivity(intent);
									}
								});
					} else if (!orderItem.getOrder_price().equals("0.00")) {
						viHolder.tv_order_price.setVisibility(View.VISIBLE);
						viHolder.tv_order_pay.setVisibility(View.VISIBLE);
						viHolder.tv_order_price.setText("实付款：     "
								+ orderItem.getYingfu() + "元");
						viHolder.tv_order_pay
								.setBackgroundResource(R.drawable.order_cuidan_bg);
						viHolder.tv_order_pay.setText("催单");
						viHolder.tv_order_pay.setTextColor(context
								.getResources().getColor(R.color.cuidanziti));
						viHolder.tv_order_pay
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// 拨打取件员电话
										if (orderItem.getCourier_phone_qu()
												.length() != 0) {
											Uri uriQu = Uri.parse("tel:"
													+ orderItem
															.getCourier_phone_qu());
											Intent intentQu = new Intent(
													Intent.ACTION_DIAL, uriQu);
											context.startActivity(intentQu);
										}
									}
								});
					} else {
						viHolder.tv_order_price.setVisibility(View.VISIBLE);
						viHolder.tv_order_pay.setVisibility(View.VISIBLE);
						viHolder.tv_order_price.setText("等待上门计价付款");
						viHolder.tv_order_pay.setText("催单");
						viHolder.tv_order_pay
								.setBackgroundResource(R.drawable.order_cuidan_bg);
						viHolder.tv_order_pay.setTextColor(context
								.getResources().getColor(R.color.cuidanziti));
						viHolder.tv_order_pay
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// 拨打取件员电话
										if (orderItem.getCourier_phone_qu()
												.length() != 0) {
											Uri uriQu = Uri.parse("tel:"
													+ orderItem
															.getCourier_phone_qu());
											Intent intentQu = new Intent(
													Intent.ACTION_DIAL, uriQu);
											context.startActivity(intentQu);
										}
									}
								});
					}
					break;
				case "1":
				case "4":
				case "5":
				case "6":
				case "8":
					viHolder.iv_order_status.setVisibility(View.VISIBLE);
					viHolder.iv_order_status
							.setImageResource(R.drawable.order_status_3);
					viHolder.rl_order_item_bottom.setVisibility(View.VISIBLE);
					viHolder.tv_order_price.setVisibility(View.VISIBLE);
					viHolder.tv_order_price.setText("实付款：     "
							+ orderItem.getYingfu() + "元");
					viHolder.tv_order_pay.setVisibility(View.VISIBLE);
					viHolder.tv_order_pay.setText("催单");
					viHolder.tv_order_pay.setTextColor(context.getResources()
							.getColor(R.color.cuidanziti));
					viHolder.tv_order_pay
							.setBackgroundResource(R.drawable.order_cuidan_bg);
					viHolder.tv_order_pay
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// 拨打客服电话
									Uri uri = Uri.parse("tel:4008187171");
									Intent intent = new Intent(
											Intent.ACTION_DIAL, uri);
									context.startActivity(intent);
								}
							});
					break;
				case "15":
					viHolder.iv_order_status.setVisibility(View.VISIBLE);
					viHolder.iv_order_status
							.setImageResource(R.drawable.order_status_4);
					viHolder.rl_order_item_bottom.setVisibility(View.VISIBLE);
					viHolder.tv_order_price.setVisibility(View.VISIBLE);
					viHolder.tv_order_price.setText("实付款：     "
							+ orderItem.getYingfu() + "元");
					viHolder.tv_order_pay.setVisibility(View.VISIBLE);
					viHolder.tv_order_pay.setText("催单");
					viHolder.tv_order_pay
							.setBackgroundResource(R.drawable.order_cuidan_bg);
					viHolder.tv_order_pay.setTextColor(context.getResources()
							.getColor(R.color.cuidanziti));
					viHolder.tv_order_pay
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// 拨打客服电话
									Uri uri = Uri.parse("tel:4008187171");
									Intent intent = new Intent(
											Intent.ACTION_DIAL, uri);
									context.startActivity(intent);
								}
							});
					break;
				case "2":
				case "7":
					viHolder.iv_order_status.setVisibility(View.VISIBLE);
					viHolder.tv_order_price.setVisibility(View.VISIBLE);
					viHolder.iv_order_status
							.setImageResource(R.drawable.order_status_4);
					viHolder.rl_order_item_bottom.setVisibility(View.VISIBLE);
					viHolder.tv_order_price.setText("实付款：     "
							+ orderItem.getYingfu() + "元");
					viHolder.tv_order_pay.setVisibility(View.VISIBLE);
					viHolder.tv_order_pay.setText("催单");
					viHolder.tv_order_pay
							.setBackgroundResource(R.drawable.order_cuidan_bg);
					viHolder.tv_order_pay.setTextColor(context.getResources()
							.getColor(R.color.cuidanziti));
					viHolder.tv_order_pay
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// 拨打送件员电话
									if (orderItem.getCourier_phone_song()
											.length() != 0) {
										Uri uriSong = Uri.parse("tel:"
												+ orderItem
														.getCourier_phone_song());
										Intent intentSong = new Intent(
												Intent.ACTION_DIAL, uriSong);
										context.startActivity(intentSong);
									}
								}
							});
					break;
				case "3":
					viHolder.tv_order_price.setVisibility(View.VISIBLE);
					viHolder.tv_order_pay.setVisibility(View.VISIBLE);
					viHolder.iv_order_status.setVisibility(View.INVISIBLE);
					viHolder.rl_order_item_bottom.setVisibility(View.VISIBLE);
					viHolder.tv_order_price.setText("实付款：     "
							+ orderItem.getYingfu() + "元");
					if (orderItem.getCan_be_commented().equals("0")) {
						viHolder.tv_order_comment_tips
								.setVisibility(View.VISIBLE);
						viHolder.tv_order_pay
								.setBackgroundResource(R.drawable.order_cuidan_bg);
						viHolder.tv_order_pay.setTextColor(context
								.getResources().getColor(R.color.pingjiaziti));
						viHolder.tv_order_pay.setText("评价");
						viHolder.tv_order_pay
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										TCAgent.onEvent(context, "点击订单评价");
										Intent intent = new Intent();
										intent.setClass(context,
												AppraiseActivity.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("orderItem",
												orderItem);
										intent.putExtras(bundle);
										context.startActivity(intent);
									}
								});
					}
					break;
				case "-1":
				case "-2":
					viHolder.iv_order_status.setVisibility(View.INVISIBLE);
					viHolder.rl_order_item_bottom.setVisibility(View.GONE);
					break;
				default:
					break;
				}
			}
		}
		if (orderItem.getCategory_id() != null) {
			viHolder.order_category_text.setText(orderItem.getGood());
			switch (orderItem.getCategory_id()) {
			case "1":
				// 洗衣类别——洗衣服
				viHolder.order_category_logo
						.setImageResource(R.drawable.washing_clothes);
				break;
			case "2":
				// 洗衣类别——洗鞋
				viHolder.order_category_logo
						.setImageResource(R.drawable.washing_shose);
				break;
			case "3":
				// 洗衣类别——窗帘
				viHolder.order_category_logo
						.setImageResource(R.drawable.washing_window);
				break;
			case "4":
				// 洗衣类别——奢侈品衣物
				viHolder.order_category_logo
						.setImageResource(R.drawable.washing_luxury);
				break;
			case "5":
				// 洗衣类别——奢侈品皮具
				viHolder.order_category_logo
						.setImageResource(R.drawable.washing_leather);
				break;
			default:
				viHolder.order_category_logo
						.setImageResource(R.drawable.washing_clothes);
				break;
			}
		}
		String orderSn = orderItem.getOrder_sn();
		StringBuilder sBuilder = new StringBuilder(orderSn);
		viHolder.tv_order_sn.setText("订单编号： "
				+ sBuilder.insert(orderSn.length() - 6, "  "));
		if (getServingDate(orderItem.getYuyue_qujian_time()) != null) {
			viHolder.tv_order_time.setText("取件时间： "
					+ getServingDate(orderItem.getYuyue_qujian_time()));
		}
		return view;
	}

	public static class ViewHolder {
		private ImageView order_category_logo;
		private TextView order_category_text;
		private ImageView iv_order_status;
		private TextView tv_order_status;
		private TextView tv_order_sn;
		private TextView tv_order_time;
		private TextView tv_order_price;
		private TextView tv_order_pay;
		private TextView tv_order_comment_tips;
		private RelativeLayout rl_order_item_bottom;
	}

	public static String getServingDate(String dateString) {
		if (dateString.length() > 10) {
			StringBuilder sBuffer = new StringBuilder(dateString.substring(0,
					10));
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			if (df.format(cal.getTime()).equals(dateString.subSequence(0, 10))) {
				sBuffer = sBuffer.insert(10, "(今天)  ");
			}
			cal.roll(Calendar.DAY_OF_YEAR, 1);
			if (df.format(cal.getTime()).equals(dateString.subSequence(0, 10))) {
				sBuffer = sBuffer.insert(10, "(明天)  ");
			}
			cal.roll(Calendar.DAY_OF_YEAR, 1);
			if (df.format(cal.getTime()).equals(dateString.subSequence(0, 10))) {
				sBuffer = sBuffer.insert(10, "(后天)  ");
			}
			if (!sBuffer.toString().contains("(")) {
				sBuffer = sBuffer.insert(10, "  ");
			}
			String replace = sBuffer.toString().replace("-", ".");
			replace = replace
					+ (dateString.subSequence(10, dateString.length())
							.toString().replace(" ", ""));
			return replace;
		} else {
			return null;
		}
	}

	/***
	 * judge Internet is available
	 * 
	 * @author wei-spring
	 * @return
	 */
	public boolean isHasNet() {
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cwjManager.getActiveNetworkInfo() != null) {
			return cwjManager.getActiveNetworkInfo().isAvailable();
		} else {
			return false;
		}
	}
}
