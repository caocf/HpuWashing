package com.edaixi.swipemenu.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import com.edaixi.activity.R;

public class Swipemenucreater {

	private Context context;

	public Swipemenucreater(Context context) {
		super();
		this.context = context;
	}

	// step 1. create a MenuCreator
	public SwipeMenuCreator getswipemenucreater() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "edit" item
				SwipeMenuItem editItem = new SwipeMenuItem(context);
				// set item background
				editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				editItem.setWidth(dp2px(90));
				// set item title
				editItem.setBackground(R.drawable.order_edit_btn);

				editItem.setIcon(R.drawable.edit);
				// add to menu
				menu.addMenuItem(editItem);
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(context);
				// set item background
				deleteItem.setBackground(R.drawable.order_delete_btn);
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		return creator;
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}
}
