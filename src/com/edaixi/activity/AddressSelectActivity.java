package com.edaixi.activity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.edaixi.adapter.SelectAddressListAdapter;
import com.edaixi.data.AddressIntentdata;
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.dataset.MyAddressDataSet;
import com.edaixi.modle.AddressBean;
import com.edaixi.modle.HttpCommonBean;
import com.edaixi.swipemenu.widget.SwipeMenu;
import com.edaixi.swipemenu.widget.SwipeMenuListView;
import com.edaixi.swipemenu.widget.SwipeMenuListView.OnMenuItemClickListener;
import com.edaixi.swipemenu.widget.Swipemenucreater;
import com.edaixi.util.Constants;
import com.edaixi.util.SaveUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

public class AddressSelectActivity extends BaseActivity implements
		OnClickListener {
	private static final int GETADSSUCCED = 4;
	private static final int GETADSFAILD = 5;
	private final int DELETEADSSUCCED = 6;
	private final int DELETEADSFAILD = 7;
	private final int NOTYFYDATACHANGE = 10;
	private Handler handler;
	private HashMap<String, String> parms;
	private MyAddressDataSet mDataSet;
	private String adsid;
	private SaveUtils saveUtils;
	private boolean firstcome = true;
	private com.edaixi.swipemenu.widget.SwipeMenuListView order_address_list;
	private com.edaixi.swipemenu.widget.SwipeMenuListView order_address_list_notuse;
	private AddressBean avilbleitem;
	protected static final String TAG = "PlaceorderActivity";
	private ImageView selectlist_no_wifi;
	private ImageView selectlist_loading;
	private TextView select_list_add_btn;
	private TextView tv_address_true_tips;
	private TextView tv_address_false_tips;
	private ImageView iv_no_address_tips;
	private TextView no_address_text;
	private MyAddressDataSet judgeOrderAddressData;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addressselect);
		init(this);
		saveUtils = new SaveUtils(this);
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				Gson gson = new Gson();

				switch (msg.what) {
				case GETADSSUCCED:
					Type contentType = new TypeToken<HttpCommonBean>() {
					}.getType();
					HttpCommonBean mInfo = null;
					try {
						mInfo = gson.fromJson((String) msg.obj, contentType);
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					if (mInfo != null) {
						if (mInfo.isRet()
								&& TextUtils.isEmpty(mInfo.getError())) {

							contentType = new TypeToken<List<AddressBean>>() {
							}.getType();
							List<AddressBean> mMyAddressDataSet = null;
							try {
								mMyAddressDataSet = gson.fromJson(
										mInfo.getData(), contentType);
							} catch (Exception e) {
								e.printStackTrace();
								return;
							}
							AddressSelectActivity.this.mDataSet.clear();
							for (AddressBean mBean : mMyAddressDataSet) {
								AddressSelectActivity.this.mDataSet
										.addBean(mBean);
							}
							if (mDataSet.size() <= 0) {
								no_address_text.setVisibility(View.VISIBLE);
								iv_no_address_tips.setVisibility(View.VISIBLE);
								tv_address_true_tips.setVisibility(View.GONE);
								tv_address_false_tips.setVisibility(View.GONE);
								order_address_list.setVisibility(View.GONE);
								order_address_list_notuse
										.setVisibility(View.GONE);
							} else {
								no_address_text.setVisibility(View.GONE);
								iv_no_address_tips.setVisibility(View.GONE);
								judgeFillOrderAddress(mDataSet);
							}
						}

					} else {
						return;
					}
					break;
				case GETADSFAILD:
					showdialog("获取地址失败");
					break;
				case DELETEADSSUCCED:
					AppConfig.getInstance().setRefresh_Address(true);
					showdialog("删除地址成功");
					mDataSet.clear();
					getaddresslist(false);
					break;
				case DELETEADSFAILD:
					showdialog("删除地址失败");
					break;
				case NOTYFYDATACHANGE:
					// order_address_list.setAdapter(adpter);
					break;
				}
				super.handleMessage(msg);

			}
		};
		initView();
	}

	private void initView() {
		selectlist_no_wifi = (ImageView) findViewById(R.id.selectlist_no_wifi);
		selectlist_no_wifi.setOnClickListener(this);
		selectlist_loading = (ImageView) findViewById(R.id.selectlist_loading);
		select_list_add_btn = (TextView) findViewById(R.id.select_list_add_btn);
		tv_address_true_tips = (TextView) findViewById(R.id.tv_address_true_tips);
		tv_address_false_tips = (TextView) findViewById(R.id.tv_address_false_tips);
		select_list_add_btn.setOnClickListener(this);
		iv_no_address_tips = (ImageView) findViewById(R.id.iv_no_address_tips);
		no_address_text = (TextView) findViewById(R.id.no_address_text);
		order_address_list = (SwipeMenuListView) findViewById(R.id.order_address_list);
		order_address_list_notuse = (SwipeMenuListView) findViewById(R.id.order_address_list_notuse);
		ImageView order_back_btn = (ImageView) findViewById(R.id.order_back_btn);
		order_back_btn.setOnClickListener(this);
		parms = new HashMap<String, String>();
		mDataSet = new MyAddressDataSet();
		if (!isHasNet()) {
			selectlist_no_wifi.setVisibility(View.VISIBLE);
			return;
		} else {
			selectlist_no_wifi.setVisibility(View.GONE);
		}
	}

	/*
	 * 设置可服务侧滑按钮
	 */
	private void setflingbuttontrue(SwipeMenuListView List,
			final MyAddressDataSet mDataSets) {
		Swipemenucreater swipemenucreater = new Swipemenucreater(getContext());
		List.setMenuCreator(swipemenucreater.getswipemenucreater());
		List.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				switch (index) {
				case 0:
					Intent intent = new Intent(AddressSelectActivity.this,
							AddadressActivity.class);
					intent.putExtra("TYPE", AddressIntentdata.EDITADS);
					intent.putExtra("FROM", AddressIntentdata.FROMPLACEORDER);
					intent.putExtra("cityarea", AppConfig.getInstance()
							.getCityareastr());
					intent.putExtra(AddressIntentdata.ADDRESSBEAN,
							mDataSets.getIndexBean(position));
					startActivity(intent);
					break;
				case 1:
					parms.clear();
					parms.put("user_id", saveUtils.getStrSP("user_id"));
					parms.put(KeepingData.ADDRESS_ID,
							mDataSets.getIndexBean(position).getAddress_id());
					postdate(parms, Constants.getdeleteaddress, handler,
							DELETEADSSUCCED, DELETEADSFAILD, false, true);
					break;
				}
				return false;
			}
		});
	}

	/*
	 * 获取地址列表
	 */
	private void getaddresslist(boolean isShow) {
		mDataSet.clear();
		parms.clear();
		parms.put("user_id", getUerId());
		if (isShow) {
			getdate(parms, Constants.getaddress, handler, GETADSSUCCED,
					GETADSFAILD, true, false, false);
		} else {
			getdate(parms, Constants.getaddress, handler, GETADSSUCCED,
					GETADSFAILD, false, false, false);
		}
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		firstcome = true;
		mDataSet.clear();
		new Handler().postDelayed(new Runnable() {
			public void run() {
				getaddresslist(true);
			}
		}, 200);
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("PlaceorderActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_list_add_btn:
			Intent intent = new Intent(getContext(), AddadressActivity.class);
			intent.putExtra(AddressIntentdata.TYPE, AddressIntentdata.ADDADS);
			intent.putExtra("FROM", AddressIntentdata.FROMPLACEORDER);
			startActivity(intent);
			break;
		case R.id.order_back_btn:
			finish(0, 0);
			break;
		case R.id.selectlist_no_wifi:
			selectlist_loading.setVisibility(View.VISIBLE);
			new Thread(new Runnable() {
				public void run() {
					try {
						getaddresslist(true);
						Thread.sleep(1000);
						AddressSelectActivity.this
								.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										selectlist_loading
												.setVisibility(View.GONE);
									}
								});
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
			break;
		}
	}

	/** 判断用户下单可用地址，然后分别填充不同的ListView **/
	public void judgeFillOrderAddress(MyAddressDataSet mDataSet) {
		String user_City = saveUtils.getStrSP(KeepingData.User_City);
		final MyAddressDataSet mDataSetTrue = new MyAddressDataSet();
		final MyAddressDataSet mDataSetFalse = new MyAddressDataSet();
		if (user_City != "") {
			for (int i = 0; i < mDataSet.size(); i++) {
				if (mDataSet.getIndexBean(i).getCity().equals(user_City)) {
					mDataSet.getIndexBean(i).setCan_Order_Select(true);
					mDataSetTrue.addBean(mDataSet.getIndexBean(i));
				} else {
					mDataSet.getIndexBean(i).setCan_Order_Select(false);
					mDataSetFalse.addBean(mDataSet.getIndexBean(i));
				}
			}
			if (mDataSetTrue.size() > 0) {
				tv_address_true_tips.setVisibility(View.VISIBLE);
				SelectAddressListAdapter adpterTrue = new SelectAddressListAdapter(
						AddressSelectActivity.this, mDataSetTrue, true);
				order_address_list.setVisibility(View.VISIBLE);
				order_address_list.setAdapter(adpterTrue);
				setflingbuttontrue(order_address_list, mDataSetTrue);
				order_address_list
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								avilbleitem = mDataSetTrue
										.getIndexBean(position);
								if (avilbleitem.isCan_Order_Select()) {
									Intent intent = new Intent();
									Bundle bundle = new Bundle();
									bundle.putSerializable(
											KeepingData.ADS_BEAN, avilbleitem);
									intent.putExtras(bundle);
									setResult(RESULT_OK, intent);
									finish(0, 0);
								}
							}
						});
			} else {
				tv_address_true_tips.setVisibility(View.GONE);
				order_address_list.setVisibility(View.GONE);
			}
			if (mDataSetFalse.size() > 0) {
				tv_address_false_tips.setVisibility(View.VISIBLE);
				SelectAddressListAdapter adpterFalse = new SelectAddressListAdapter(
						AddressSelectActivity.this, mDataSetFalse, false);
				order_address_list_notuse.setVisibility(View.VISIBLE);
				order_address_list_notuse.setAdapter(adpterFalse);
				setflingbuttontrue(order_address_list_notuse, mDataSetFalse);
				order_address_list_notuse
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								avilbleitem = mDataSetFalse
										.getIndexBean(position);
								if (avilbleitem.isCan_Order_Select()) {
									Intent intent = new Intent();
									Bundle bundle = new Bundle();
									bundle.putSerializable(
											KeepingData.ADS_BEAN, avilbleitem);
									intent.putExtras(bundle);
									setResult(RESULT_OK, intent);
									finish(0, 0);
								}
							}
						});
			} else {
				tv_address_false_tips.setVisibility(View.GONE);
				order_address_list_notuse.setVisibility(View.GONE);
			}
		}
	}

	public void addList(MyAddressDataSet mDataSet,
			MyAddressDataSet mDataSetDefault) {
		if (mDataSet != null && mDataSetDefault != null) {
			if (mDataSetDefault.size() > 0) {
				for (int i = 0; i < mDataSetDefault.size(); i++) {
					mDataSet.addBean(mDataSetDefault.getIndexBean(i));
				}
			}
		}
	}
}
