package com.edaixi.activity;

import java.util.ArrayList;
import java.util.List;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;
import com.edaixi.adapter.SearchListadapter;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.MapItemBean;
import com.edaixi.util.LogUtil;
import com.edaixi.util.SaveUtils;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SearchAddressActivity extends BaseActivity implements
		OnPoiSearchListener, OnClickListener {

	private com.edaixi.view.CleanEditText edit_search_address;
	private List<MapItemBean> listMaps = new ArrayList<MapItemBean>();
	private SearchListadapter searchListadapter;
	private ListView list_search_result;
	private TextView tv_search_ok;
	private SaveUtils saveUtils;
	private ImageView iv_search_address_back;
	private ProgressBar search_progress_bar;

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return false;
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search_address);
		init(this);
		saveUtils = new SaveUtils(this);
		search_progress_bar = (ProgressBar) findViewById(R.id.search_progress_bar);
		edit_search_address = (com.edaixi.view.CleanEditText) findViewById(R.id.edit_search_address);
		edit_search_address.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence chars, int start,
					int before, int count) {
				// queryAddressString(chars.toString().trim());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable chars) {
				if (chars.toString().trim() == null
						|| chars.toString().trim().equals("")) {
					search_progress_bar.setVisibility(View.GONE);
					if (listMaps != null && searchListadapter != null) {
						listMaps.clear();
						searchListadapter.setMadslist(listMaps);
						searchListadapter.notifyDataSetChanged();
					}
				}else{
					doSearchQuery(chars.toString().trim());
				}
			}
		});
		String keyWordString = getIntent().getStringExtra("SearchKeyWord");
		if (keyWordString != null) {
			edit_search_address.setText(keyWordString);
		}
		list_search_result = (ListView) findViewById(R.id.list_search_result);
		searchListadapter = new SearchListadapter(this, listMaps);
		list_search_result.setAdapter(searchListadapter);
		list_search_result.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				if ((listMaps != null) && (listMaps.size() > 0)) {
					bundle.putSerializable(KeepingData.select_Map_Item,
							listMaps.get(position));
				}
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish(0, 0);
			}
		});
		tv_search_ok = (TextView) findViewById(R.id.tv_search_ok);
		tv_search_ok.setOnClickListener(this);
		iv_search_address_back = (ImageView) findViewById(R.id.iv_search_address_back);
		iv_search_address_back.setOnClickListener(this);
	}

	/** 根据输入框字符串反查询地图位置，以及周边位置 **/
	public void queryAddressString(String addressString) {
		Inputtips inputTips = new Inputtips(SearchAddressActivity.this,
				new InputtipsListener() {
					@Override
					public void onGetInputtips(List<Tip> tipList, int rCode) {
						if (rCode == 0 && listMaps != null
								&& searchListadapter != null) {
							listMaps.clear();
							for (int i = 0; i < tipList.size(); i++) {
								MapItemBean mapItemBean = new MapItemBean();
								mapItemBean.setMapItemName(tipList.get(i)
										.getName());
								mapItemBean.setMapItemTitle(tipList.get(i)
										.getDistrict());
								if (listMaps != null) {
									listMaps.add(mapItemBean);
								}
							}
							searchListadapter.setMadslist(listMaps);
							searchListadapter.notifyDataSetChanged();
						}
					}
				});
		try {
			inputTips.requestInputtips(addressString,
					saveUtils.getStrSP(KeepingData.City_Code));
		} catch (com.amap.api.services.core.AMapException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search_address_back:
			finish(0, 0);
			break;
		case R.id.tv_search_ok:
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			MapItemBean mapItemBean = new MapItemBean();
			mapItemBean.setMapItemName(edit_search_address.getText().toString()
					.trim());
			bundle.putSerializable(KeepingData.select_Map_Item, mapItemBean);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish(0, 0);
			break;
		}
	}

	/** 开始进行poi搜索 **/
	protected void doSearchQuery(String keyword) {
		// String serString =
		// "60100|190100|190200|190300|190400|190500|190600|60300|60400|60700|70000|100000|110000|120000|130000|140000";
		String serString = "120000|190000";
		Query query = new PoiSearch.Query(keyword, "",
				saveUtils.getStrSP(KeepingData.User_City));
		query.setPageSize(20);
		query.setPageNum(0);

		PoiSearch poiSearch = new PoiSearch(this, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
		search_progress_bar.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail result, int rCode) {

	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (result != null) {
			search_progress_bar.setVisibility(View.GONE);
			List<PoiItem> poiItems = result.getPois();
			if (poiItems.size() > 0) {
				listMaps.clear();
				for (int i = 0; i < poiItems.size(); i++) {
					MapItemBean mapItemBean = new MapItemBean();
					if ((poiItems.get(i).getTitle() != "")
							&& (poiItems.get(i).getSnippet() != "")) {
						mapItemBean.setMapItemTitle(poiItems.get(i).getTitle());
						mapItemBean
								.setMapItemName(poiItems.get(i).getSnippet());
						mapItemBean.setMapItemLat(poiItems.get(i)
								.getLatLonPoint().getLatitude()
								+ "");
						mapItemBean.setMapItemLog(poiItems.get(i)
								.getLatLonPoint().getLongitude()
								+ "");
						mapItemBean.setMapItemCity(poiItems.get(i)
								.getCityName());
						mapItemBean.setMapItemArea(poiItems.get(i).getAdName());
						LogUtil.e(mapItemBean.toString());
						if (listMaps != null) {
							listMaps.add(mapItemBean);
						}
					}
				}
				searchListadapter.setMadslist(listMaps);
				searchListadapter.notifyDataSetChanged();

			} else {
				List<SuggestionCity> searchSuggestionCitys = result
						.getSearchSuggestionCitys();
				if (searchSuggestionCitys.size() > 0) {
					for (SuggestionCity poiItem : searchSuggestionCitys) {
					}
				}
			}
		}
	}
}
