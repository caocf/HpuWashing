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

	@SuppressLint("ViewHolder") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		@SuppressWarnings("unused")
		final int index = position;
		OrderDeliveryInfo deliveryItem = parseOrderDevlivery.get(position);
		View view;
		ViewHolder viHolder = null;
//		if (convertView != null) {
//			view = convertView;
//			viHolder = (ViewHolder) view.getTag();
//		} else {
			view = View.inflate(context, R.layout.delivery_list_item, null);
			viHolder = new ViewHolder();
			viHolder.iv_delivert_master = (ImageView) view
					.findViewById(R.id.iv_delivert_master);
			viHolder.iv_bottom_line_1 = (ImageView) view
					.findViewById(R.id.iv_bottom_line_1);
			viHolder.iv_bottom_line_2 = (ImageView) view
					.findViewById(R.id.iv_bottom_line_2);
			viHolder.iv_delivert_branch = (ImageView) view
					.findViewById(R.id.iv_delivert_branch);
			viHolder.tv_delivery_text_newest = (TextView) view
					.findViewById(R.id.tv_delivery_text_newest);
			viHolder.tv_delivery_time_1 = (TextView) view
					.findViewById(R.id.tv_delivery_time_1);
			viHolder.tv_delivery_time_2 = (TextView) view
					.findViewById(R.id.tv_delivery_time_2);
			viHolder.tv_delivery_text_past = (TextView) view
					.findViewById(R.id.tv_delivery_text_past);
//			view.setTag(viHolder);
//		}

		if (position == 0) {
			viHolder.tv_delivery_text_newest.setText(deliveryItem.getText());
			viHolder.tv_delivery_time_1.setText(deliveryItem.getTime());
			if(parseOrderDevlivery.size()==1){
				viHolder.iv_bottom_line_1.setVisibility(View.GONE);
			}
		}
		if (position > 0) {
			viHolder.iv_delivert_master.setVisibility(View.GONE);
			viHolder.tv_delivery_text_newest.setVisibility(View.GONE);
			viHolder.tv_delivery_time_1.setVisibility(View.GONE);
			viHolder.iv_bottom_line_1.setVisibility(View.GONE);
			viHolder.iv_bottom_line_2.setVisibility(View.VISIBLE);
			viHolder.iv_delivert_branch.setVisibility(View.VISIBLE);
			viHolder.tv_delivery_text_past.setVisibility(View.VISIBLE);
			viHolder.tv_delivery_time_2.setVisibility(View.VISIBLE);
			if(deliveryItem.getText().length()>28){
				viHolder.tv_delivery_text_past.setText(new StringBuilder(deliveryItem.getText()).insert(20, "\n"));
			}else{
				viHolder.tv_delivery_text_past.setText(deliveryItem.getText());
			}
			viHolder.tv_delivery_time_2.setText(deliveryItem.getTime());
			if(position == parseOrderDevlivery.size()-1){
				viHolder.iv_bottom_line_2.setVisibility(View.GONE);
			}
		}
		return view;
	}

	private static class ViewHolder {
		public ImageView iv_delivert_master;
		public ImageView iv_delivert_branch;
		public ImageView iv_bottom_line_1;
		public ImageView iv_bottom_line_2;
		public TextView tv_delivery_text_newest;
		public TextView tv_delivery_text_past;
		public TextView tv_delivery_time_1;
		public TextView tv_delivery_time_2;
	}

}
