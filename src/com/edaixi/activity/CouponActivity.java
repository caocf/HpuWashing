package com.edaixi.activity;

import java.lang.ref.SoftReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.edaixi.Enum.CouponEntity;
import com.edaixi.Enum.UseConponType;
import com.edaixi.Enum.WebPageType;
import com.edaixi.adapter.CouponActivityListAdapter;
import com.edaixi.data.AppConfig;
import com.edaixi.dataset.CouponsDataSet;
import com.edaixi.modle.BindCouponBean;
import com.edaixi.modle.CouponBean;
import com.edaixi.modle.GetCouponsBean;
import com.edaixi.util.Constants;
import com.edaixi.util.LogUtil;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.util.SaveUtils;
import com.edaixi.view.CouponExchangeDialog;
import com.edaixi.view.CouponExchangeDialog.ExchangeDialogButtonListener;
import com.edaixi.view.ListViewWithNoScrollbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import de.greenrobot.event.EventBus;

/**
 * 优惠券页面 打开此页面时请务必在bundle中指定UseConponType类型
 */
@SuppressLint("HandlerLeak")
public class CouponActivity extends BaseActivity {

	private static final int REFRESH_CORRECT_CODE = 10;
	private static final int REFRESH_ERROR_CODE = 11;
	private static final int EXCHANGE_CORRECT_CODE = 12;
	private static final int EXCHANGE_ERROR_CODE = 13;
	private ListViewWithNoScrollbar mListView = null;
	private CouponsDataSet mListDataSet = null;
	private CouponActivityListAdapter mListAdapter = null;
	private UpdateHandler mHandler = null;
	private CommonCallbackListener mCommonListener = null;
	private SaveUtils mSaveUtils = null;
	private HashMap<String, String> mParams = new HashMap<String, String>();
	private Gson mGson = null;
	private AppConfig mAppConfig = null;
	private UseConponType mType;
	private double mMoney = -1;
	private ImageView no_coupon_img;
	private TextView no_userable_coupon;
	private TextView coupon_info_btn;
	private String mCard;
	private CouponExchangeDialog exchange_dialog;
	private String order_IdString;
	private String category_IdString;

	@Override
	protected void onCreate(Bundle args) {
		super.onCreate(args);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_coupon);
		init(this);
		mHandler = new UpdateHandler(this);
		mCommonListener = new CommonCallbackListener();
		mGson = new Gson();
		mSaveUtils = new SaveUtils(this);
		mAppConfig = AppConfig.getInstance();
		Bundle mBundle = getIntent().getExtras();
		if (mBundle != null) {
			order_IdString = mBundle.getString("Order_Id");
			category_IdString = mBundle.getString("Category_Id");
			mType = (UseConponType) mBundle.getSerializable("TYPE");
			switch (mType) {
			case CHECK:
				AppConfig.getInstance().setCouponId(0);
				break;
			case USE:
				mMoney = mBundle.getDouble("MONEY", -1);
				break;
			}
		}

		findViewById(R.id.activity_coupon_back_btn).setOnClickListener(
				mCommonListener);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		findViewById(R.id.activity_coupon_exchange).setOnClickListener(
				mCommonListener);
		findViewById(R.id.ll_coupon_exchange).setOnClickListener(
				mCommonListener);
		no_coupon_img = (ImageView) findViewById(R.id.no_coupon_img);
		no_userable_coupon = (TextView) findViewById(R.id.no_userable_coupon);
		mListView = (ListViewWithNoScrollbar) findViewById(R.id.activity_coupon_list);
		coupon_info_btn = (TextView) findViewById(R.id.coupon_info_btn);
		coupon_info_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TCAgent.onEvent(getActivity(), "优惠券说明");
				Bundle mBundle = new Bundle();
				Intent intent = new Intent();
				mBundle.putInt("TYPE", WebPageType.COUPON_INFO.getType());
				mBundle.putBoolean("couponinfo", true);
				intent.putExtras(mBundle);
				intent.setClass(CouponActivity.this, WebActivity.class);
				startActivity(intent);

			}
		});
		mListDataSet = new CouponsDataSet();
		mListAdapter = new CouponActivityListAdapter(this, mListDataSet);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(mCommonListener);

		mParams.clear();
		mParams.put("user_id", mSaveUtils.getStrSP("user_id"));
		if (order_IdString != null) {
			mParams.put("order_id", order_IdString);
		}
		getdate(mParams, Constants.getcoupons, mHandler, REFRESH_CORRECT_CODE,
				REFRESH_ERROR_CODE, true, true, false);

		EventBus.getDefault().register(this);
	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("CouponActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("CouponActivity"); // 保证 onPageEnd 在onPause
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected boolean onBackKeyDown() {
		setResult(Constants.ADSREQUSEFAILD);
		finish(0, 0);
		return false;
	}

	/**
	 * 公共回调监听器
	 * 
	 */
	private class CommonCallbackListener implements OnClickListener,
			OnItemClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			/* 返回按钮 */
			case R.id.activity_coupon_back_btn:
				onBackKeyDown();
				break;
			/* 兑换按钮 */
			case R.id.ll_coupon_exchange:
			case R.id.activity_coupon_exchange:
				TCAgent.onEvent(CouponActivity.this, "兑换优惠券");
				exchange_dialog = new CouponExchangeDialog(CouponActivity.this,
						R.style.customdialog_style,
						R.layout.coupon_exchange_dialog);
				exchange_dialog.show();
				exchange_dialog
						.setYourListener(new ExchangeDialogButtonListener() {

							@Override
							public void setExchangeCoupon(String couponCode) {
								mCard = couponCode;
								getcouponlist();
							}
						});
				break;
			default:
				break;
			}
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (mType.equals(UseConponType.USE)) {
				if (mListDataSet.getIndexBean(position).isValid()) {
					mAppConfig.setCouponId(mListDataSet.getIndexBean(position)
							.getId());
					Intent mIntent = new Intent();
					Bundle mBundle = new Bundle();
					mBundle.putSerializable("DATA",
							mListDataSet.getIndexBean(position));
					mIntent.putExtras(mBundle);
					setResult(RESULT_OK, mIntent);
					finish();
				} else {
					showdialog("此优惠券不满足使用条件");
				}
			}
		}

	}

	private class UpdateHandler extends Handler {

		private SoftReference<CouponActivity> mActivityRef = null;

		public UpdateHandler(CouponActivity mActivity) {
			mActivityRef = new SoftReference<CouponActivity>(mActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			CouponActivity mActivity = mActivityRef.get();
			if (mActivity == null) {
				return;
			}
			switch (msg.what) {
			case REFRESH_CORRECT_CODE: {
				Type contentType = new TypeToken<GetCouponsBean>() {
				}.getType();
				GetCouponsBean mInfo = null;
				try {
					mInfo = mActivity.mGson.fromJson((String) msg.obj,
							contentType);
				} catch (Exception e) {
					e.printStackTrace();
					showdialog("数据解析错误");
					return;
				}
				if (mInfo != null && mInfo.isRet()
						&& TextUtils.isEmpty(mInfo.getError())) {
					contentType = new TypeToken<ArrayList<CouponBean>>() {
					}.getType();
					List<CouponBean> mCouponArray = null;
					try {
						mCouponArray = mActivity.mGson.fromJson(
								mInfo.getData(), contentType);
					} catch (Exception e) {
						e.printStackTrace();
						showdialog("数据解析错误");
						return;
					}

					mActivity.mListDataSet.clear();
					if (mCouponArray != null) {
						for (int i = 0, size = mCouponArray.size(); i < size; i++) {
							mActivity.mListDataSet.addBean(new CouponEntity(
									mCouponArray.get(i)));
						}
					}
					if (mCouponArray.size() <= 0) {
						no_coupon_img.setVisibility(View.VISIBLE);
						no_userable_coupon.setVisibility(View.VISIBLE);
					} else {
						no_coupon_img.setVisibility(View.GONE);
						no_userable_coupon.setVisibility(View.GONE);
					}
					if (mActivity.mType == UseConponType.USE) {
						mActivity.mListDataSet
								.setDataSetValid(mActivity.mMoney);
					}
					mListAdapter = null;
					if (category_IdString != null) {
						CouponsDataSet mNewListDataSet = judgeCouponData(mListDataSet);
						mListDataSet = mNewListDataSet;
					}
					mListAdapter = new CouponActivityListAdapter(
							CouponActivity.this, mListDataSet);
					mListView.setAdapter(mListAdapter);
				} else {
					showdialog("数据错误");
				}
				break;
			}
			case REFRESH_ERROR_CODE:
				showdialog("数据错误");
				break;
			case EXCHANGE_CORRECT_CODE: {
				Type contentType = new TypeToken<BindCouponBean>() {
				}.getType();
				BindCouponBean mBean = null;
				try {
					mBean = mActivity.mGson.fromJson((String) msg.obj,
							contentType);
				} catch (Exception e) {
					e.printStackTrace();
					showdialog("兑换解析错误");
					return;
				}

				if (mBean.isRet()) {
					if (!isFinishing()) {
						showdialog("兑换成功");
					}
					mActivity.mParams.clear();
					mActivity.mParams.put("user_id",
							mActivity.mSaveUtils.getStrSP("user_id"));
					mActivity.getdate(mActivity.mParams, Constants.getcoupons,
							mActivity.mHandler, REFRESH_CORRECT_CODE,
							REFRESH_ERROR_CODE, true, true, false);
				} else {
					if (!isFinishing()) {
						showdialog(mBean.getError());
					}
				}
				break;
			}
			case EXCHANGE_ERROR_CODE:
				Type contentType = new TypeToken<BindCouponBean>() {
				}.getType();
				BindCouponBean mBean = null;
				try {
					mBean = mActivity.mGson.fromJson((String) msg.obj,
							contentType);
					showdialog(mBean.getError());
				} catch (Exception e) {
					e.printStackTrace();
					showdialog("解析错误");
					return;
				}
				break;
			}

		}

	}

	public void getcouponlist() {
		mParams.clear();
		mParams.put("user_id", mSaveUtils.getStrSP("user_id"));
		mParams.put("sncode", mCard);
		postdate(mParams, Constants.getbindcoupon, mHandler,
				EXCHANGE_CORRECT_CODE, EXCHANGE_ERROR_CODE, false, true);
	}

	// 处理优惠券数据，重新排列优惠券顺序和是否可用
	public CouponsDataSet judgeCouponData(CouponsDataSet mListDataSet) {
		CouponsDataSet allCouponsDataSet = new CouponsDataSet();
		CouponsDataSet availableCouponsDataSet = new CouponsDataSet();
		CouponsDataSet unavailableCouponsDataSet = new CouponsDataSet();
		for (int i = 0; i < mListDataSet.size(); i++) {
			if ((mListDataSet.getIndexBean(i).isValid())
					&& ((mListDataSet.getIndexBean(i).getCategory_id()
							.equals(category_IdString)) || mListDataSet
							.getIndexBean(i).getCategory_id().equals("0"))) {
				availableCouponsDataSet.addBean(mListDataSet.getIndexBean(i));
			} else {
				mListDataSet.getIndexBean(i).setValid(-1);
				unavailableCouponsDataSet.addBean(mListDataSet.getIndexBean(i));
			}
		}
		for (int i = 0; i < availableCouponsDataSet.size(); i++) {
			allCouponsDataSet.addBean(availableCouponsDataSet.getIndexBean(i));
		}
		for (int i = 0; i < unavailableCouponsDataSet.size(); i++) {
			allCouponsDataSet
					.addBean(unavailableCouponsDataSet.getIndexBean(i));
		}
		return allCouponsDataSet;
	}

	public void onEvent(OrderListAdapterEvent event) {
		switch (event.getText()) {
		case "HidenExchangeCouponInput":
			invisibleInputmethod(coupon_info_btn);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		default:
			break;
		}
	}
}
