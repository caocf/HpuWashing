package com.edaixi.wheelpicker.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.edaixi.activity.R;

public class CitiesPopup {
	private Context mContext;
	private boolean scrolling = false;
	private int mIndex;
	private String mDateSelected;
	private String mTimeSelected;
	private boolean isAddress;
	private boolean isFromOrder = false;
	private final PopupWindow mPopupWindow;
	private final String countries[];
	private final String cities[][];
	private TextView mdata;
	private TextView maddressCity;
	private TextView maddressArea;
	private TextView mtitle;

	public CitiesPopup(Context context, String[] country, String[][] city,
			boolean address, TextView addssCity, TextView addssArea,
			boolean isFromOrder) {
		mContext = context;
		countries = country;
		cities = city;
		maddressArea = addssArea;
		maddressCity = addssCity;
		isAddress = address;
		this.isFromOrder = isFromOrder;
		View suggestionContainerLayoutView = createSuggestionContainerView(context);
		mPopupWindow = new PopupWindow(suggestionContainerLayoutView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
	}

	public CitiesPopup(Context context, String[] country, String[][] city,
			boolean address, View date, TextView title) {
		mContext = context;
		mtitle = title;
		mdata = (TextView) date;
		countries = country;
		cities = city;
		isAddress = address;
		View suggestionContainerLayoutView = createSuggestionContainerView(context);
		mPopupWindow = new PopupWindow(suggestionContainerLayoutView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
		mPopupWindow.setAnimationStyle(R.style.SuggestionPopupAnimation);
	}

	public void show(View view) {
		mPopupWindow.showAtLocation(view, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		mPopupWindow.update();
	}

	public boolean isShowing() {
		return mPopupWindow.isShowing();
	}

	public void dismiss() {
		mPopupWindow.dismiss();
	}

	private View createSuggestionContainerView(Context context) {
		View v = LayoutInflater.from(context).inflate(R.layout.cities_layout,
				null, false);
		final WheelView country = (WheelView) v.findViewById(R.id.country);
		country.setVisibleItems(3);
		country.setViewAdapter(new CountryAdapter(context));
		if (isFromOrder) {
			country.setCurrentItem(3, true);
			country.setTouchable(false);
		}

		final WheelView city = (WheelView) v.findViewById(R.id.city);
		city.setVisibleItems(5);

		country.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!scrolling) {
					updateCities(city, cities, newValue);
				}
			}
		});

		city.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;
				if (cities[mIndex].length != 0) {
					mTimeSelected = cities[mIndex][city.getCurrentItem()];
				}
			}
		});

		country.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;
				updateCities(city, cities, country.getCurrentItem());
			}
		});

		country.setCurrentItem(0);
		updateCities(city, cities, country.getCurrentItem());
		View cancel = v.findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mPopupWindow.dismiss();
			}
		});
		View confrim = v.findViewById(R.id.editdone);
		confrim.setOnClickListener(new View.OnClickListener() {
			private TextView textview;

			@Override
			public void onClick(View arg0) {
				if (isAddress) {
					// 获取选择区域，可以传递给其他业务逻辑
					if (isAddress) {
						maddressCity.setText(mDateSelected);
						maddressArea.setText(mTimeSelected);
					}
				} else {
					// 获取选择时间，可以传递给其他业务逻辑
					mdata.setVisibility(View.VISIBLE);
					mtitle.setVisibility(View.GONE);
					CharSequence weekday = mDateSelected.subSequence(1, 3);
					String datastr = (String) mDateSelected.subSequence(4, 9);
					String replace = datastr.replace("-", "月");
					mdata.setText("  " + replace + "日" + "  " + weekday + "  "
							+ mTimeSelected);

				}
				mPopupWindow.dismiss();
			}
		});
		v.requestFocus();
		v.setFocusableInTouchMode(true);
		v.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if ((arg1 == KeyEvent.KEYCODE_BACK)
						&& (mPopupWindow.isShowing())) {
					mPopupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
		return v;
	}

	/**
	 * Updates the city wheel
	 */
	public void getselectitem() {

	};

	private void updateCities(WheelView city, String cities[][], int index) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				mContext, cities[index]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
		mDateSelected = countries[index];
		mIndex = index;
		if (cities[mIndex].length != 0) {
			mTimeSelected = cities[mIndex][city.getCurrentItem()];
		}
	}

	/**
	 * Adapter for countries
	 */
	private class CountryAdapter extends AbstractWheelTextAdapter {

		/**
		 * Constructor
		 */
		protected CountryAdapter(Context context) {
			super(context, R.layout.country_layout, NO_RESOURCE);

			setItemTextResource(R.id.country_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return countries.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return countries[index];
		}
	}
}
