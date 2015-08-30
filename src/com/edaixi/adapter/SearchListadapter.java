package com.edaixi.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.edaixi.activity.R;
import com.edaixi.modle.MapItemBean;

public class SearchListadapter extends BaseAdapter {
	private LayoutInflater minflter = null;
	private ViewHolder holder;
	private View view;
	private List<MapItemBean> madslist;

	public SearchListadapter(Context context, List<MapItemBean> madslist) {
		super();
		minflter = LayoutInflater.from(context);
		this.madslist = madslist;
	}

	public List<MapItemBean> getMadslist() {
		return madslist;
	}

	public void setMadslist(List<MapItemBean> madslist) {
		this.madslist = madslist;
	}

	@Override
	public int getCount() {
		return madslist.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
		}
		holder = new ViewHolder();
		view = minflter.inflate(R.layout.item_search_list, null);
		holder.title_loction_text = (TextView) view
				.findViewById(R.id.title_loction_text);
		holder.loction_value = (TextView) view.findViewById(R.id.loction_value);
		if (madslist.get(position).getMapItemCity() == null) {
			holder.loction_value.setText(madslist.get(position)
					.getMapItemName());
		} else {
			holder.loction_value
					.setText(madslist.get(position).getMapItemCity()
							+ madslist.get(position).getMapItemName());
		}
		holder.title_loction_text.setText(madslist.get(position)
				.getMapItemTitle());
		return view;
	}

	private class ViewHolder {
		public TextView loction_value;
		public TextView title_loction_text;
	}
}
