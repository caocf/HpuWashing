package com.edaixi.activity;

import java.lang.ref.SoftReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.edaixi.adapter.MyAddressListAdapter;
import com.edaixi.data.AddressIntentdata;
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.dataset.MyAddressDataSet;
import com.edaixi.modle.AddressBean;
import com.edaixi.modle.CitysAreaBean;
import com.edaixi.modle.HttpCommonBean;
import com.edaixi.swipemenu.widget.*;
import com.edaixi.swipemenu.widget.SwipeMenuListView.OnMenuItemClickListener;
import com.edaixi.util.Constants;
import com.edaixi.util.DensityUtil;
import com.edaixi.util.LogUtil;
import com.edaixi.util.SaveUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的常用地址页面
 */
public class MyAddressActivity extends BaseActivity implements OnClickListener {
	private static final int CHECK_CORRECT_CODE = 10;
	private static final int CHECK_ERROR_CODE = 11;
	private static final int DELETE_CORRECT_CODE = 12;
	private static final int DELETE_ERROR_CODE = 13;
	private SwipeMenuListView mList = null;
	private MyAddressDataSet mListDataSet = null;
	private MyAddressListAdapter mListAdapter = null;
	private UpdateHandler mHandler = null;
	private CommonCallBackListener mCommonListener = null;
	private SaveUtils mSaveUtils = null;
	private HashMap<String, String> mParams = null;
	private Gson mGson = null;
	private int positions;
	private ImageView addresslist_no_order;
	private ImageView addresslist_loading;
	private ImageView iv_no_address_tips;
	private static final int CITYSUCESS = 14;
	private static final int CITYFAIL = 15;
	private TextView no_address_text;
	private AnimationDrawable animationDrawable;

	@Override
	protected void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_myaddress);
		super.onCreate(arg0);
		init(this);

		mHandler = new UpdateHandler(this);
		mCommonListener = new CommonCallBackListener();
		mGson = new Gson();
		mSaveUtils = new SaveUtils(this);
		mParams = new HashMap<String, String>();
		findViewById(R.id.activity_myaddress_back_btn).setOnClickListener(
				mCommonListener);
		findViewById(R.id.activity_myaddress_add_btn).setOnClickListener(
				mCommonListener);
		iv_no_address_tips = (ImageView) findViewById(R.id.iv_no_address_tips);
		mList = (SwipeMenuListView) findViewById(R.id.activity_myaddress_list);
		no_address_text = (TextView) findViewById(R.id.no_address_text);
		addresslist_no_order = (ImageView) findViewById(R.id.addresslist_no_order);
		addresslist_no_order.setOnClickListener(this);
		addresslist_loading = (ImageView) findViewById(R.id.addresslist_loading);
		mListDataSet = new MyAddressDataSet();
		mListAdapter = new MyAddressListAdapter(getContext(), mListDataSet,
				false, false, mList);
		mList.requestLayout();
		mList.setAdapter(mListAdapter);
		setflingbutton();
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TCAgent.onEvent(getActivity(), "常用地址_编辑");
				Intent intent = new Intent(MyAddressActivity.this,
						AddadressActivity.class);
				intent.putExtra("TYPE", AddressIntentdata.EDITADS);
				intent.putExtra("FROM", AddressIntentdata.FROMADSLIST);
				intent.putExtra("cityarea", AppConfig.getInstance()
						.getCityareastr());
				intent.putExtra(AddressIntentdata.ADDRESSBEAN,
						mListDataSet.getIndexBean(position));
				mListDataSet.clear();
				startActivity(intent);
			}
		});
		mList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		animationDrawable = (AnimationDrawable) addresslist_no_order
				.getBackground();
		if (!isHasNet()) {
			addresslist_no_order.setVisibility(View.VISIBLE);
			return;
		} else {
			addresslist_no_order.setVisibility(View.GONE);
		}

		mParams.clear();
		getdate(mParams, Constants.getcitiesoptions, mHandler, CITYSUCESS,
				CITYFAIL, false, false, false);

		if (!mSaveUtils.getBoolSP(KeepingData.is_Show_Address_Guide)) {
			mHandler.postDelayed(addressGuideRunnable, 0);
			mSaveUtils.saveBoolSP(KeepingData.is_Show_Address_Guide, true);
		}
	}

	private Runnable addressGuideRunnable = new Runnable() {
		@Override
		public void run() {
			startActivity(new Intent(getActivity(),
					GuideAddressTipsActivity.class));
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		if (mListAdapter != null) {
			mListAdapter.notifyDataSetChanged();
		}
		mListDataSet.clear();
		new Handler().postDelayed(new Runnable() {
			public void run() {
				getaddresslist();
			}
		}, 200);
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("MyAddressActivity");
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("MyAddressActivity");
		MobclickAgent.onPause(this);
	}

	private void getaddresslist() {
		mParams.clear();
		mParams.put("user_id", mSaveUtils.getStrSP("user_id"));
		getdate(mParams, Constants.getaddress, mHandler, CHECK_CORRECT_CODE,
				CHECK_ERROR_CODE, true, false, true);
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return true;
	}

	private class UpdateHandler extends Handler {

		private SoftReference<MyAddressActivity> mActivityRef = null;
		private CitysAreaBean cityAreaBean;

		public UpdateHandler(MyAddressActivity mActivity) {
			mActivityRef = new SoftReference<MyAddressActivity>(mActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			MyAddressActivity mActivity = mActivityRef.get();
			if (mActivity == null) {
				return;
			}
			switch (msg.what) {
			case CHECK_CORRECT_CODE: {
				Type contentType = new TypeToken<HttpCommonBean>() {
				}.getType();
				HttpCommonBean mInfo = null;
				try {
					mInfo = mActivity.mGson.fromJson((String) msg.obj,
							contentType);

				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				if (mInfo != null) {
					addresslist_no_order.setVisibility(View.GONE);
					if (mInfo.isRet() && TextUtils.isEmpty(mInfo.getError())) {

						contentType = new TypeToken<List<AddressBean>>() {
						}.getType();
						List<AddressBean> mMyAddressDataSet = null;

						try {
							LogUtil.e(mInfo.getData());
							mMyAddressDataSet = mActivity.mGson.fromJson(
									mInfo.getData(), contentType);
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
						mActivity.mListDataSet.clear();
						mListAdapter.notifyDataSetChanged();
						for (AddressBean mBean : mMyAddressDataSet) {
							mActivity.mListDataSet.addBean(mBean);
						}
						if (mMyAddressDataSet.size() <= 0) {
							no_address_text.setVisibility(View.VISIBLE);
							iv_no_address_tips.setVisibility(View.VISIBLE);
						} else {
							no_address_text.setVisibility(View.GONE);
							iv_no_address_tips.setVisibility(View.GONE);
						}
						MyAddressActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								mListAdapter.notifyDataSetChanged();
							}
						});
					}
				} else {
					return;
				}
				setflingbutton();
				break;
			}
			case CHECK_ERROR_CODE:
				break;
			case CITYSUCESS:
				String cityResultSucess = (String) msg.obj;
				String data;
				try {
					data = new JSONObject(cityResultSucess).getString("data");
					Gson gson = new Gson();
					cityAreaBean = gson.fromJson(data, CitysAreaBean.class);
					AppConfig.getInstance().setCityareastr(data);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case DELETE_CORRECT_CODE: {
				Type contentType = new TypeToken<HttpCommonBean>() {
				}.getType();
				HttpCommonBean mBean = null;
				try {
					mBean = mActivity.mGson.fromJson((String) msg.obj,
							contentType);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				if (mBean == null) {
					return;
				} else {
					if (mBean.isRet()) {
						showdialog("删除成功");
						mListDataSet.removeIndexBean(positions);
						MyAddressActivity.this.mListAdapter
								.notifyDataSetChanged();
					} else {
						showdialog("删除失败 " + mBean.getError());
					}
				}
				break;
			}
			case DELETE_ERROR_CODE:
				showdialog("删除失败");
				break;
			}
		}
	}

	private class CommonCallBackListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_myaddress_back_btn:
				finish(0, 0);
				break;
			case R.id.activity_myaddress_add_btn:
				Intent intent = new Intent(getContext(),
						AddadressActivity.class);
				intent.putExtra(AddressIntentdata.CITYAREA, AppConfig
						.getInstance().getCityareastr());
				intent.putExtra(AddressIntentdata.TYPE,
						AddressIntentdata.ADDADS);
				intent.putExtra("FROM", AddressIntentdata.FROMADSLIST);
				startActivity(intent);
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addresslist_no_order:
			addresslist_loading.setVisibility(View.VISIBLE);
			new Thread(new Runnable() {
				public void run() {
					try {
						getaddresslist();
						Thread.sleep(1000);
						MyAddressActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								addresslist_loading.setVisibility(View.GONE);
							}
						});
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
			break;
		default:
			break;
		}
	}

	/*
	 * 设置侧滑按钮
	 */
	private void setflingbutton() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				deleteItem.setBackground(R.drawable.order_delete_btn);
				deleteItem.setWidth(DensityUtil.dip2px(getApplicationContext(),
						90));
				deleteItem.setIcon(R.drawable.delete);
				menu.addMenuItem(deleteItem);
			}
		};
		mList.setMenuCreator(creator);
		mList.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				switch (index) {
				case 0:
					TCAgent.onEvent(getActivity(), "常用地址_删除");
					mParams.clear();
					mParams.put("user_id", mSaveUtils.getStrSP("user_id"));
					mParams.put(KeepingData.ADDRESS_ID, mListDataSet
							.getIndexBean(position).getAddress_id());
					System.out.println(mListDataSet.size() + "changdule");
					positions = position;
					postdate(mParams, Constants.getdeleteaddress, mHandler,
							DELETE_CORRECT_CODE, DELETE_ERROR_CODE, true, true);

					break;
				}
				return false;
			}
		});
	}
}
