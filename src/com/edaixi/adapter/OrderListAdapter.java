/*
 * this adapter is for orderlist
 * by wei-spring
 */
package com.edaixi.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.edaixi.activity.AppraiseActivity;
import com.edaixi.activity.PayActivityBak;
import com.edaixi.activity.R;
import com.edaixi.modle.OrderListItemBean;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.view.CancleOrderDialog;
import com.tendcloud.tenddata.TCAgent;

import de.greenrobot.event.EventBus;

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
			viHolder.order_id = (TextView) view.findViewById(R.id.order_id);
			viHolder.fl_circle = (FrameLayout) view
					.findViewById(R.id.fl_circle);
			viHolder.tv_order_statue_circle = (TextView) view
					.findViewById(R.id.tv_order_statue_circle);
			viHolder.order_time_text = (TextView) view
					.findViewById(R.id.order_time_text);
			viHolder.order_item_tipname = (TextView) view
					.findViewById(R.id.order_item_tipname);
			viHolder.iv_order_statue_circle = (ImageView) view
					.findViewById(R.id.iv_order_statue_circle);
			viHolder.order_item_next_1 = (ImageView) view
					.findViewById(R.id.order_item_next_1);
			viHolder.order_item_next_2 = (ImageView) view
					.findViewById(R.id.order_item_next_2);
			viHolder.order_item_next_3 = (ImageView) view
					.findViewById(R.id.order_item_next_3);
			viHolder.iv_dotted_line = (ImageView) view
					.findViewById(R.id.iv_dotted_line);
			viHolder.order_pay_value = (TextView) view
					.findViewById(R.id.order_pay_value);
			viHolder.remove_order_btn = (TextView) view
					.findViewById(R.id.remove_order_btn);
			viHolder.comment_order_btn = (TextView) view
					.findViewById(R.id.comment_order_btn);
			viHolder.immediately_pay_btn = (TextView) view
					.findViewById(R.id.immediately_pay_btn);
			viHolder.ll_comment = (LinearLayout) view
					.findViewById(R.id.ll_comment);
			viHolder.ll_serving_order = (LinearLayout) view
					.findViewById(R.id.ll_serving_order);
			viHolder.order_item_tipcolor = (View) view
					.findViewById(R.id.order_item_tipcolor);
			if (flag.equals("COMPLETEDORDER")) {
				viHolder.ll_serving_order.setVisibility(View.GONE);
				viHolder.fl_circle.setVisibility(View.GONE);
				viHolder.iv_order_statue_circle.setVisibility(View.INVISIBLE);
				viHolder.tv_order_statue_circle.setVisibility(View.GONE);
				viHolder.ll_comment.setVisibility(View.VISIBLE);
				viHolder.comment_order_btn.setVisibility(View.VISIBLE);
				viHolder.order_item_next_1.setVisibility(View.INVISIBLE);
				viHolder.order_item_next_2.setVisibility(View.VISIBLE);
				viHolder.order_item_next_3.setVisibility(View.INVISIBLE);
				viHolder.remove_order_btn.setVisibility(View.INVISIBLE);
			}
			view.setTag(viHolder);
		}
		if (orderItem.getCan_be_canceled() != null) {
			if (!orderItem.getCan_be_canceled().equals("true")) {
				viHolder.remove_order_btn.setVisibility(View.GONE);
			} else {
				viHolder.remove_order_btn.setVisibility(View.VISIBLE);
				viHolder.remove_order_btn
						.setBackgroundResource(R.drawable.order_white_btn_bg);
				viHolder.remove_order_btn.setText("取消订单");
				viHolder.remove_order_btn.setTextColor(Color.GRAY);
				viHolder.remove_order_btn
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								TCAgent.onEvent(context, "取消订单");
								if (isHasNet()) {
									CancleOrderDialog cuDialog = new CancleOrderDialog(
											context,
											R.style.customdialog_style,
											R.layout.cancle_order_dialog,
											orderItem);
									cuDialog.show();
								} else {
									EventBus.getDefault().post(
											new OrderListAdapterEvent(
													"NoNetQuXiaoDingDan"));
								}
							}
						});
			}
		}

		if (!orderItem.getCan_be_paid().equals("true")) {
			viHolder.immediately_pay_btn
					.setBackgroundResource(R.drawable.order_white_btn_bg);
			viHolder.immediately_pay_btn
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
						}
					});
			if (orderItem.getPay_status().equals("1")) {
				viHolder.immediately_pay_btn.setVisibility(View.GONE);
				viHolder.iv_dotted_line.setVisibility(View.GONE);
			} else {
				viHolder.iv_dotted_line.setVisibility(View.VISIBLE);
				viHolder.immediately_pay_btn.setVisibility(View.VISIBLE);
				viHolder.immediately_pay_btn.setTextColor(R.color.gray);
				viHolder.immediately_pay_btn.setText("暂时不能付款");
			}

		} else {
			/***
			 * click to pay page
			 */
			viHolder.immediately_pay_btn.setVisibility(View.VISIBLE);
			viHolder.immediately_pay_btn
					.setBackgroundResource(R.drawable.order_yellow_btn_bg);
			viHolder.immediately_pay_btn.setText("立即支付");
			viHolder.immediately_pay_btn.setTextColor(Color.WHITE);
			viHolder.immediately_pay_btn
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setClass(context, PayActivityBak.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("order_id",
									orderItem.getOrder_id());
							bundle.putSerializable("order_sn",
									orderItem.getOrder_sn());
							bundle.putSerializable("order_price",
									orderItem.getOrder_price());
							bundle.putSerializable("coupon_sn",
									orderItem.getCoupon_sn());
							bundle.putSerializable("coupon_paid",
									orderItem.getCoupon_paid());
							bundle.putSerializable("exclusive_channels",
									orderItem.getExclusive_channels());
							intent.putExtras(bundle);
							context.startActivity(intent);
						}
					});

		}

		if (orderItem.getCan_be_commented() != null) {
			if (flag.equals("COMPLETEDORDER")) {
				viHolder.iv_dotted_line.setVisibility(View.VISIBLE);
			}
			if (orderItem.getCan_be_commented().equals("1")) {
				viHolder.comment_order_btn.setClickable(false);
				viHolder.comment_order_btn.setTextColor(Color.GRAY);
				viHolder.comment_order_btn
						.setBackgroundResource(R.drawable.order_white_btn_bg);
				viHolder.comment_order_btn.setText("   已评价");
				viHolder.comment_order_btn
						.setOnClickListener(new OnClickListener() {

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
			} else if (orderItem.getCan_be_commented().equals("2")) {
				viHolder.comment_order_btn.setClickable(false);
				viHolder.comment_order_btn.setTextColor(Color.GRAY);
				viHolder.comment_order_btn
						.setBackgroundResource(R.drawable.order_white_btn_bg);
				viHolder.comment_order_btn.setText("   已过期");
				viHolder.comment_order_btn
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
							}
						});
			} else {
				viHolder.comment_order_btn.setTextColor(Color.WHITE);
				viHolder.comment_order_btn.setText("   评价");
				viHolder.comment_order_btn
						.setBackgroundResource(R.drawable.order_yellow_btn_bg);
				viHolder.comment_order_btn
						.setOnClickListener(new OnClickListener() {
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
			}
		}

		if (orderItem.getDelivery_status() != null) {
			switch (orderItem.getDelivery_status()) {
			case "10":
			case "3":
				viHolder.iv_order_statue_circle.setVisibility(View.INVISIBLE);
				viHolder.tv_order_statue_circle.setVisibility(View.GONE);
				break;
			case "8":
			case "1":
			case "4":
			case "5":
			case "6":
				viHolder.iv_order_statue_circle.setVisibility(View.VISIBLE);
				viHolder.tv_order_statue_circle.setVisibility(View.VISIBLE);
				viHolder.tv_order_statue_circle.setTextColor(context
						.getResources().getColor(R.color.green));
				viHolder.iv_order_statue_circle
						.setImageResource(R.drawable.order_statue_circle_2);
				viHolder.tv_order_statue_circle.setText(" "
						+ orderItem.getDelivery_status_text().trim()
								.substring(0, 2)
						+ " "
						+ "\n"
						+ orderItem.getDelivery_status_text().trim()
								.substring(2, 5));
				break;
			case "-1":
			case "9":
				viHolder.iv_order_statue_circle.setVisibility(View.VISIBLE);
				viHolder.tv_order_statue_circle.setVisibility(View.VISIBLE);
				viHolder.tv_order_statue_circle.setTextColor(context
						.getResources().getColor(R.color.green));
				viHolder.iv_order_statue_circle
						.setImageResource(R.drawable.order_statue_circle_1);
				viHolder.tv_order_statue_circle.setText(" "
						+ orderItem.getDelivery_status_text().trim()
								.substring(0, 2)
						+ " "
						+ "\n"
						+ orderItem.getDelivery_status_text().trim()
								.substring(2, 5));
				break;
			case "0":
				viHolder.iv_order_statue_circle.setVisibility(View.VISIBLE);
				viHolder.tv_order_statue_circle.setVisibility(View.VISIBLE);
				viHolder.tv_order_statue_circle.setTextColor(context
						.getResources().getColor(R.color.green));
				viHolder.iv_order_statue_circle
						.setImageResource(R.drawable.order_statue_circle_0);
				viHolder.tv_order_statue_circle.setText(orderItem
						.getDelivery_status_text().trim());
				break;
			case "11":
				viHolder.iv_order_statue_circle.setVisibility(View.VISIBLE);
				viHolder.tv_order_statue_circle.setVisibility(View.VISIBLE);
				viHolder.tv_order_statue_circle.setTextColor(context
						.getResources().getColor(R.color.green));
				viHolder.iv_order_statue_circle
						.setImageResource(R.drawable.order_statue_circle_0);
				viHolder.tv_order_statue_circle.setText(orderItem
						.getDelivery_status_text().trim());
				break;
			case "-2":
			case "2":
			case "7":
			case "15":
				viHolder.iv_order_statue_circle.setVisibility(View.VISIBLE);
				viHolder.tv_order_statue_circle.setVisibility(View.VISIBLE);
				viHolder.tv_order_statue_circle.setTextColor(context
						.getResources().getColor(R.color.green));
				viHolder.iv_order_statue_circle
						.setImageResource(R.drawable.order_statue_circle_3);
				viHolder.tv_order_statue_circle.setText(" "
						+ orderItem.getDelivery_status_text().trim()
								.substring(0, 2)
						+ " "
						+ "\n"
						+ orderItem.getDelivery_status_text().trim()
								.substring(2, 5));
				break;
			default:
				break;
			}
		}
		viHolder.order_id.setText(orderItem.getOrder_sn());
		viHolder.order_pay_value.setText(orderItem.getOrder_price() + "元");
		viHolder.order_time_text.setText(getServingDate(orderItem
				.getYuyue_qujian_time()));
		if (orderItem.getCategory_id() != null) {
			switch (orderItem.getCategory_id()) {
			case "1":
				viHolder.order_item_tipcolor.setBackgroundColor(context
						.getResources().getColor(R.color.order_xiyi));
				viHolder.order_item_tipname.setTextColor(context.getResources()
						.getColor(R.color.order_xiyi));
				viHolder.order_item_tipname.setText(orderItem.getGood());
				break;
			case "2":
				viHolder.order_item_tipcolor.setBackgroundColor(context
						.getResources().getColor(R.color.order_xixie));
				viHolder.order_item_tipname.setTextColor(context.getResources()
						.getColor(R.color.order_xixie));
				viHolder.order_item_tipname.setText(orderItem.getGood());
				break;
			case "3":
				viHolder.order_item_tipcolor.setBackgroundColor(context
						.getResources().getColor(R.color.order_jiafang));
				viHolder.order_item_tipname.setTextColor(context.getResources()
						.getColor(R.color.order_jiafang));
				viHolder.order_item_tipname.setText(orderItem.getGood());
				break;
			default:
				viHolder.order_item_tipcolor.setBackgroundColor(context
						.getResources().getColor(R.color.order_xiyi));
				viHolder.order_item_tipname.setTextColor(context.getResources()
						.getColor(R.color.order_xiyi));
				viHolder.order_item_tipname.setText(orderItem.getGood());
				break;
			}
		}
		return view;
	}

	public static class ViewHolder {
		public TextView order_id;
		public TextView order_pay_value;
		public TextView remove_order_btn;
		public TextView immediately_pay_btn;
		public TextView comment_order_btn;
		public TextView tv_order_statue_circle;
		public TextView order_time_text;
		public TextView order_item_tipname;
		private View order_item_tipcolor;
		public ImageView iv_order_statue_circle;
		public ImageView order_item_next_1;
		public ImageView order_item_next_2;
		public ImageView order_item_next_3;
		public ImageView iv_dotted_line;
		public LinearLayout ll_comment;
		public FrameLayout fl_circle;
		public LinearLayout ll_serving_order;
	}

	public static StringBuffer getServingDate(String dateString) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(dateString.substring(5, 7).trim());
		sBuffer.append("月");
		sBuffer.append(dateString.substring(8, 10).trim());
		sBuffer.append("日 ");
		sBuffer.append(dateString.substring(10).trim());
		return sBuffer;
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
