/*
 * this adapter is for deliverylist
 * by wei-spring
 */
package com.edaixi.adapter;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.edaixi.activity.R;
import com.edaixi.modle.OrderDeliveryInfo;

public class DeliveryListAdapter extends BaseAdapter {

	private ArrayList<OrderDeliveryInfo> parseOrderDevlivery;
	private Context context;

	public DeliveryListAdapter(
			ArrayList<OrderDeliveryInfo> parseOrderDevlivery, Context context) {
		this.parseOrderDevlivery = parseOrderDevlivery;
		this.context = context;
	}

	@Override
	public int getCount() {
		return parseOrderDevlivery.size();
	}

	@Override
	public Object getItem(int position) {
		return parseOrderDevlivery.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		@SuppressWarnings("unused")
		final int index = position;
		OrderDeliveryInfo deliveryItem = parseOrderDevlivery.get(position);
		View view;
		ViewHolder viHolder = null;
		// if (convertView != null) {
		// view = convertView;
		// viHolder = (ViewHolder) view.getTag();
		// } else {
		view = View.inflate(context, R.layout.delivery_list_item, null);
		viHolder = new ViewHolder();
		viHolder.dl_delivert_master = (com.edaixi.view.DeliveryLineViewMaster) view
				.findViewById(R.id.dl_delivert_master);
		viHolder.dl_delivert_branch = (com.edaixi.view.DeliveryLineView) view
				.findViewById(R.id.dl_delivert_branch);
		viHolder.tv_delivery_time_text = (TextView) view
				.findViewById(R.id.tv_delivery_time_text);
		viHolder.tv_delivery_text_newest = (TextView) view
				.findViewById(R.id.tv_delivery_text_newest);
		viHolder.tv_delivery_text_past = (TextView) view
				.findViewById(R.id.tv_delivery_text_past);
		viHolder.tv_delivery_time_past_text = (TextView) view
				.findViewById(R.id.tv_delivery_time_past_text);
		viHolder.iv_bottom_line_item_master = (ImageView) view
				.findViewById(R.id.iv_bottom_line_item_master);
		viHolder.iv_bottom_line_item_branch = (ImageView) view
				.findViewById(R.id.iv_bottom_line_item_branch);
		// view.setTag(viHolder);
		// }

		if (position == 0) {
			viHolder.dl_delivert_master.setVisibility(View.VISIBLE);
			viHolder.iv_bottom_line_item_master.setVisibility(View.VISIBLE);
			viHolder.dl_delivert_branch.setVisibility(View.GONE);
			viHolder.iv_bottom_line_item_branch.setVisibility(View.GONE);
			viHolder.tv_delivery_text_past.setVisibility(View.GONE);
			viHolder.tv_delivery_time_past_text.setVisibility(View.GONE);
			viHolder.tv_delivery_text_newest.setVisibility(View.VISIBLE);
			viHolder.tv_delivery_time_text.setVisibility(View.VISIBLE);
			viHolder.tv_delivery_text_newest.setText(deliveryItem.getText());
			viHolder.tv_delivery_time_text.setText(deliveryItem.getTime());
		} else {
			viHolder.dl_delivert_master.setVisibility(View.GONE);
			viHolder.tv_delivery_text_newest.setVisibility(View.GONE);
			viHolder.tv_delivery_time_text.setVisibility(View.GONE);
			viHolder.iv_bottom_line_item_master.setVisibility(View.GONE);
			viHolder.dl_delivert_branch.setVisibility(View.VISIBLE);
			viHolder.tv_delivery_text_past.setVisibility(View.VISIBLE);
			viHolder.iv_bottom_line_item_branch.setVisibility(View.VISIBLE);
			viHolder.tv_delivery_text_past.setText(deliveryItem.getText());
			viHolder.tv_delivery_time_past_text.setVisibility(View.VISIBLE);
			viHolder.tv_delivery_time_past_text.setText(deliveryItem.getTime());
		}
		return view;
	}

	private static class ViewHolder {
		public com.edaixi.view.DeliveryLineViewMaster dl_delivert_master;
		public TextView tv_delivery_text_newest;
		public TextView tv_delivery_time_text;
		public TextView tv_delivery_text_past;
		public TextView tv_delivery_time_past_text;
		public com.edaixi.view.DeliveryLineView dl_delivert_branch;
		public ImageView iv_bottom_line_item_master;
		public ImageView iv_bottom_line_item_branch;
	}

}
