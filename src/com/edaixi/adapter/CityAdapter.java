package com.edaixi.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edaixi.activity.R;
import com.edaixi.citylist.widget.ContactItemInterface;
import com.edaixi.citylist.widget.ContactListAdapter;

public class CityAdapter extends ContactListAdapter {

	public CityAdapter(Context _context, int _resource,
			List<ContactItemInterface> _items) {
		super(_context, _resource, _items);
	}

	public void populateDataForRow(View parentView, ContactItemInterface item,
			int position) {
		View infoView = parentView.findViewById(R.id.infoRowContainer);
		TextView nicknameView = (TextView) infoView.findViewById(R.id.cityName);
		ImageView itemSelectView = (ImageView) infoView
				.findViewById(R.id.iv_city_item_select);
		nicknameView.setText(item.getDisplayInfo());
		if (item.getCitySelectInfo()) {
			itemSelectView.setVisibility(View.VISIBLE);
		} else {
			itemSelectView.setVisibility(View.GONE);
		}
	}
}
