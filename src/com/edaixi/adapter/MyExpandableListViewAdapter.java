package com.edaixi.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.edaixi.activity.R;
import com.edaixi.modle.ClothingOrderInfo;

/**
 * clothing detail expandable listview....
 * 
 * @author wei-spring
 * 
 */

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ClothingOrderInfo> parseClothingyRes;
	private List<String> child_list;

	public MyExpandableListViewAdapter(
			ArrayList<ClothingOrderInfo> parseClothingyRes,
			List<String> child_list, Context context) {
		this.parseClothingyRes = parseClothingyRes;
		this.child_list = child_list;
		this.context = context;
	}

	@Override
	public int getGroupCount() {
		return parseClothingyRes.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return parseClothingyRes.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return child_list.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		@SuppressWarnings("unused")
		ClothingOrderInfo clothingItem = parseClothingyRes.get(groupPosition);
		View view;
		GroupHolder gHolder = null;
		if (convertView != null) {
			view = convertView;
			gHolder = (GroupHolder) view.getTag();
		} else {
			view = View.inflate(context, R.layout.clothing_list_item_group,
					null);
			gHolder = new GroupHolder();
			gHolder.tv_clothing_color = (TextView) view
					.findViewById(R.id.tv_clothing_color);
			gHolder.tv_clothing_title = (TextView) view
					.findViewById(R.id.tv_clothing_title);
			gHolder.tv_clothing_cloth_condition = (TextView) view
					.findViewById(R.id.tv_clothing_cloth_condition);
			gHolder.iv_clothingdetail_show = (ImageView) view
					.findViewById(R.id.iv_clothingdetail_show);
			view.setTag(gHolder);
			gHolder.tv_clothing_color.setText(parseClothingyRes.get(
					groupPosition).getColor());
			gHolder.tv_clothing_title.setText(parseClothingyRes.get(
					groupPosition).getCloth_title());
			// gHolder.tv_clothing_wash_result.setText(parseClothingyRes.get(
			// groupPosition).getWash_result());
		}
		// ----------------------------------------------------------
		if (parseClothingyRes.size() > 0
				&& groupPosition < parseClothingyRes.size()) {
			// 设置父item右边的箭头
			if (parseClothingyRes.get(groupPosition) != null
					&& child_list.size() != 0) {
				gHolder.iv_clothingdetail_show
						.setImageResource(R.drawable.details_close);
			} else {
				gHolder.iv_clothingdetail_show.setImageResource(0);
			}
			// 如果展开时 替换右边的箭头
			if (isExpanded && child_list.size() > 0
					&& child_list.get(groupPosition) != null) {
				gHolder.iv_clothingdetail_show
						.setImageResource(R.drawable.details_open);
			}
		}
		// ---------------------------------------------------------
		return view;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view;
		ItemHolder chHolder = null;
		if (convertView != null) {
			view = convertView;
			chHolder = (ItemHolder) view.getTag();
		} else {
			view = View.inflate(context, R.layout.clothing_list_item_child,
					null);
			chHolder = new ItemHolder();
			chHolder.tv_clothing_detail_1 = (TextView) view
					.findViewById(R.id.tv_clothing_detail_1);
			view.setTag(chHolder);
			chHolder.tv_clothing_detail_1.setText(parseClothingyRes.get(
					groupPosition).getCloth_condition());
		}
		return view;

	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private class GroupHolder {
		public ImageView iv_clothingdetail_show;
		public TextView tv_clothing_title;
		public TextView tv_clothing_color;
		@SuppressWarnings("unused")
		public TextView tv_clothing_cloth_condition;
	}

	private class ItemHolder {
		public TextView tv_clothing_detail_1;
	}

}
