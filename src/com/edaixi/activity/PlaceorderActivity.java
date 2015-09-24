package com.edaixi.activity;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.edaixi.data.AddressIntentdata;
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.dataset.MyAddressDataSet;
import com.edaixi.modle.AddressBean;
import com.edaixi.modle.BannerlistBean;
import com.edaixi.modle.CitysAreaBean;
import com.edaixi.modle.HttpCommonBean;
import com.edaixi.modle.InappUrlbean;
import com.edaixi.util.Constants;
import com.edaixi.util.IsChinese;
import com.edaixi.util.LogUtil;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.util.SaveUtils;
import com.edaixi.wheelpicker.widget.Wheelpicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tendcloud.tenddata.TCAgent;

import de.greenrobot.event.EventBus;

@SuppressLint("HandlerLeak")
public class PlaceorderActivity extends BaseActivity implements OnClickListener {
	private static final int CREATORDERSUCCED = 0;
	private static final int CREATORDERFAILD = 1;
	protected static final int CITYSUCESS = 2;
	protected static final int CITYFAIL = 3;
	private static final int GETADSSUCCED = 4;
	private static final int GETADSFAILD = 5;
	private static final int GRTDELFEEFAILED = 6;
	private static final int GRTDELFEESUCCED = 7;
	private Calendar nowTime;
	private String mdate;
	private String midstr;
	private HashMap<String, String> parms;
	private MyAddressDataSet mDataSet;
	private TextView select_add_text;
	private String dealutads = "";
	private String adsid;
	private RelativeLayout select_ads_layout;
	private CitysAreaBean cityAreaBean;
	private RelativeLayout time_layout;
	private View addmanager_layout;
	private TextView order_date;
	private TextView order_text;
	private AddressBean adsbean;
	private TextView deli_fee;
	private TextView tv_create_order_title;
	private RelativeLayout address_info;
	private LinearLayout ll_create_order_price;
	private TextView place_name;
	private TextView place_phone;
	private TextView place_address;
	private SaveUtils saveUtils;
	private Button create_order_btn;
	private TextView point_text;
	private TextView tv_create_order_price;
	private EditText et_order_comment;
	List<AddressBean> mMyAddressDataSet = null;
	private boolean is_fill_address = false;
	private boolean is_create_address = false;
	private boolean is_Create_Order = false;
	private BannerlistBean bannerlistbean;

	private PlaceOrderHandler placeOrderHandler = new PlaceOrderHandler();

	public class PlaceOrderHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Gson gson = new Gson();
			switch (msg.what) {
			case CREATORDERSUCCED:
				HttpCommonBean conmonBean = gson.fromJson((String) msg.obj,
						HttpCommonBean.class);
				if (conmonBean.isRet()) {
					EventBus.getDefault().post(
							new OrderListAdapterEvent("OrderSucess"));
					AppConfig.getInstance().setNewOrder(true);
					finish();
				} else {
					showdialog(conmonBean.getError());
					AppConfig.getInstance().setCanCreateOrder(true);
				}
				break;
			case CREATORDERFAILD:
				showdialog("创建订单失败");
				AppConfig.getInstance().setCanCreateOrder(true);
				break;
			case CITYSUCESS:
				HttpCommonBean conmonBeanCity = gson.fromJson((String) msg.obj,
						HttpCommonBean.class);
				String cityResultSucess = (String) msg.obj;
				Log.d("Citys--sucess", cityResultSucess);
				if (conmonBeanCity.isRet()) {
					cityAreaBean = gson.fromJson(conmonBeanCity.getData(),
							CitysAreaBean.class);
					AppConfig.getInstance().setCityareastr(
							conmonBeanCity.getData());
					Log.d("Citys--area", cityAreaBean.toString());
				}
				break;
			case CITYFAIL:
				String cityResultFail = (String) msg.obj;
				Log.d("Citys--fail", cityResultFail);
				break;
			case GETADSSUCCED:
				is_create_address = true;
				Type contentType = new TypeToken<HttpCommonBean>() {
				}.getType();
				HttpCommonBean mInfo = null;
				try {
					mInfo = gson.fromJson((String) msg.obj, contentType);
					LogUtil.e("地址列表数据----来自下单页面" + msg.obj);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				if (mInfo != null) {
					if (mInfo.isRet() && TextUtils.isEmpty(mInfo.getError())) {

						contentType = new TypeToken<List<AddressBean>>() {
						}.getType();
						try {
							mMyAddressDataSet = gson.fromJson(mInfo.getData(),
									contentType);
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
						if (mMyAddressDataSet.size() > 0) {
							for (AddressBean addressbean : mMyAddressDataSet) {
								if (addressbean.isFrequently_address()
										|| is_fill_address) {
									LogUtil.e("---------000555--------------");
									if (AppConfig.getInstance().isFillAddressAuto()
											&& addressbean
													.getCity()
													.equals(saveUtils
															.getStrSP(KeepingData.User_City))) {
										LogUtil.e("---------000666--------------");
										address_info
												.setVisibility(View.VISIBLE);
										select_add_text
												.setVisibility(View.GONE);
										place_name.setText("  "
												+ addressbean.getUsername());
										place_phone.setText("  "
												+ addressbean.getTel());
										if (addressbean.getAddress() != null)
											place_address.setText("  "
													+ addressbean.getAddress()
															.toString().trim());
										adsid = addressbean.getAddress_id();
									}
								}
							}
						}
						if (mMyAddressDataSet.size() == 0) {
							address_info.setVisibility(View.GONE);
							select_add_text.setVisibility(View.VISIBLE);
						}
						if (AppConfig.getInstance().isRefresh_Address()) {
							boolean is_Contain_Fill = false;
							for (AddressBean addressbean : mMyAddressDataSet) {
								if (adsid == addressbean.getAddress_id()) {
									is_Contain_Fill = true;
								}
							}
							if (!is_Contain_Fill) {
								address_info.setVisibility(View.GONE);
								select_add_text.setVisibility(View.VISIBLE);
								AppConfig.getInstance().setRefresh_Address(
										false);
							}
						}
					}
				} else {
					return;
				}
				break;
			case GETADSFAILD:
				break;
			case GRTDELFEESUCCED:
				HttpCommonBean conmonBeanFee = gson.fromJson((String) msg.obj,
						HttpCommonBean.class);
				if (conmonBeanFee.isRet()) {
					try {
						point_text.setVisibility(View.VISIBLE);
						deli_fee.setVisibility(View.VISIBLE);
						String tipString = null;
						StringBuffer sBuffer = new StringBuffer();
						JSONArray jsonArray = new JSONArray(
								conmonBeanFee.getData());
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArray
									.opt(i);
							if (jsonObject.has("tip")) {
								tipString = jsonObject.getString("tip");
							}
							if (i < jsonArray.length() - 1) {
								sBuffer.append(jsonObject.getString("title")
										+ "\n");
							} else {
								sBuffer.append(jsonObject.getString("title"));
							}

						}
						if (tipString != null) {
							point_text.setText(tipString);
						} else {
							point_text.setText("");
						}
						deli_fee.setText(sBuffer);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showdialog(conmonBeanFee.getError());
				}
				break;
			case GRTDELFEEFAILED:
				point_text.setVisibility(View.INVISIBLE);
				deli_fee.setVisibility(View.INVISIBLE);
				break;
			default:
				break;
			}
			super.handleMessage(msg);

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_placeorder);
		init(this);
		saveUtils = new SaveUtils(this);
		AppConfig.getInstance().setCanCreateOrder(true);
		initView();
	}

	private void initView() {
		AppConfig.getInstance().setFillAddressAuto(true);
		create_order_btn = (Button) findViewById(R.id.create_order_btn);
		create_order_btn.setOnClickListener(this);
		point_text = (TextView) findViewById(R.id.point_text);
		tv_create_order_price = (TextView) findViewById(R.id.tv_create_order_price);
		tv_create_order_price.setOnClickListener(this);
		tv_create_order_title = (TextView) findViewById(R.id.tv_create_order_title);
		place_name = (TextView) findViewById(R.id.place_name);
		place_phone = (TextView) findViewById(R.id.place_phone);
		place_address = (TextView) findViewById(R.id.place_address);
		address_info = (RelativeLayout) findViewById(R.id.address_info);
		ll_create_order_price = (LinearLayout) findViewById(R.id.ll_create_order_price);
		ll_create_order_price.setOnClickListener(this);
		deli_fee = (TextView) findViewById(R.id.deli_fee);
		et_order_comment = (EditText) findViewById(R.id.et_order_comment);
		select_add_text = (TextView) findViewById(R.id.select_add_text);
		select_add_text.setOnClickListener(this);
		order_date = (TextView) findViewById(R.id.order_date);
		order_date.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)
						&& !TextUtils.isEmpty(place_name.getText().toString())) {
					create_order_btn
							.setBackgroundResource(R.drawable.order_yellow_btn_bg);
					is_Create_Order = true;
				} else {
					create_order_btn
							.setBackgroundResource(R.drawable.order_gray_btn_bg_default);
					is_Create_Order = false;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		place_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)
						&& !TextUtils.isEmpty(order_date.getText().toString())) {
					create_order_btn
							.setBackgroundResource(R.drawable.order_yellow_btn_bg);
					is_Create_Order = true;
				} else {
					create_order_btn
							.setBackgroundResource(R.drawable.order_gray_btn_bg_default);
					is_Create_Order = false;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		order_text = (TextView) findViewById(R.id.order_text);
		time_layout = (RelativeLayout) findViewById(R.id.time_layout);
		time_layout.setOnClickListener(this);
		select_ads_layout = (RelativeLayout) findViewById(R.id.select_ads_layout);
		select_ads_layout.setOnClickListener(this);
		findViewById(R.id.back_placeorder_btn).setOnClickListener(this);
		addmanager_layout = findViewById(R.id.addmanager_layout);
		parms = new HashMap<String, String>();
		mDataSet = new MyAddressDataSet();
		getcityfee();
		if (getIntent().getSerializableExtra("bannerlistbean") != null) {
			bannerlistbean = (BannerlistBean) getIntent().getSerializableExtra(
					"bannerlistbean");
			tv_create_order_title.setText(bannerlistbean.getTitle());
			if (bannerlistbean.getInner_title() != null) {
				tv_create_order_price.setText(bannerlistbean.getInner_title()
						+ "  ");
			}
		}
		if (!saveUtils.getBoolSP(KeepingData.is_Show_Order_Guide)) {
			placeOrderHandler.postDelayed(orderGuideRunnable, 0);
			saveUtils.saveBoolSP(KeepingData.is_Show_Order_Guide, true);
		}
	}

	private Runnable orderGuideRunnable = new Runnable() {
		@Override
		public void run() {
			startActivity(new Intent(getActivity(),
					GuideOrderTipsActivity.class));
		}
	};

	/*
	 * 获取地址列表
	 */
	private void getaddresslist() {
		mDataSet.clear();
		parms.clear();
		parms.put("user_id", getUerId());
		getdate(parms, Constants.getaddress, placeOrderHandler, GETADSSUCCED,
				GETADSFAILD, false, true, true);
	}

	/**
	 * 获取运费列表
	 * 
	 * @return
	 */
	private void getcityfee() {
		parms.clear();
		if (saveUtils.getStrSP(KeepingData.User_City_Id) != null) {
			parms.put("city_id", saveUtils.getStrSP(KeepingData.User_City_Id));
		} else {
			parms.put("city_id", "1");
		}
		if (bannerlistbean != null) {
			Gson gson = new Gson();
			InappUrlbean inappurlbean = gson.fromJson(bannerlistbean.getUrl(),
					InappUrlbean.class);
			parms.put("category_id", inappurlbean.getId());
		} else {
			parms.put("category_id", "1");
		}
		getdate(parms, Constants.GET_CITY_DELIVERY_FEE, placeOrderHandler,
				GRTDELFEESUCCED, GRTDELFEEFAILED, false, false, true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getaddresslist();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == KeepingData.ADSREQUSECODE && resultCode == RESULT_OK) {
			if (intent != null) {
				adsbean = (AddressBean) intent.getExtras().getSerializable(
						KeepingData.ADS_BEAN);
				if (adsbean != null) {
					AppConfig.getInstance().setFillAddressAuto(false);
					address_info.setVisibility(View.VISIBLE);
					select_add_text.setVisibility(View.GONE);
					place_name.setText("  " + adsbean.getUsername());
					place_phone.setText("  " + adsbean.getTel());
					place_address.setText("  " + adsbean.getAddress().trim());
					adsid = adsbean.getAddress_id();
					dealutads = place_name.getText().toString().trim();
				} else {
					AppConfig.getInstance().setFillAddressAuto(true);
				}
			} else {
				AppConfig.getInstance().setFillAddressAuto(true);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_ads_layout:
		case R.id.select_add_text:
			TCAgent.onEvent(PlaceorderActivity.this, "下单_添加/选择地址");
			if (mMyAddressDataSet != null && mMyAddressDataSet.size() > 0) {
				Intent selectAdressIntent = new Intent(getContext(),
						AddressSelectActivity.class);
				selectAdressIntent.putExtra("Is_Select_Address", true);
				startActivityForResult(selectAdressIntent,
						KeepingData.ADSREQUSECODE);
			} else if (is_create_address) {
				is_fill_address = true;
				Intent createAdressIntent = new Intent(getContext(),
						AddadressActivity.class);
				createAdressIntent.putExtra(AddressIntentdata.TYPE,
						AddressIntentdata.ADDADS);
				createAdressIntent.putExtra("FROM",
						AddressIntentdata.FROMPLACEORDER);
				startActivityForResult(createAdressIntent,
						KeepingData.Create_Address_Order);
			} else {
				Intent selectAdressIntent = new Intent(getContext(),
						AddressSelectActivity.class);
				selectAdressIntent.putExtra("Is_Select_Address", true);
				startActivityForResult(selectAdressIntent,
						KeepingData.ADSREQUSECODE);
			}
			break;
		case R.id.time_layout:
			closeKeyBoard();
			Wheelpicker.show(getContext(), addmanager_layout, order_date,
					order_text);
			break;
		case R.id.create_order_btn:
			LogUtil.e("进入点击了 isCanCreateOrder"
					+ AppConfig.getInstance().isCanCreateOrder());
			TCAgent.onEvent(PlaceorderActivity.this, "下单_下单按钮");
			if (AppConfig.getInstance().isCanCreateOrder()) {
				LogUtil.e("进入下单了");
				createOrder();
			}
			break;
		case R.id.back_placeorder_btn:
			onBackKeyDown();
			break;
		case R.id.ll_create_order_price:
		case R.id.tv_create_order_price:
			TCAgent.onEvent(PlaceorderActivity.this,
					"下单页面_" + bannerlistbean.getInner_title());
			if (bannerlistbean != null
					&& bannerlistbean.getInner_type() != null) {
				if (bannerlistbean.getInner_type().equals("web")
						&& bannerlistbean.getInner_url() != null) {
					Intent intent = new Intent(getActivity(), WebActivity.class);
					BannerlistBean bannerlistBeansBean = new BannerlistBean();
					bannerlistBeansBean.setTitle(bannerlistbean
							.getInner_title());
					bannerlistBeansBean.setUrl(bannerlistbean.getInner_url());
					intent.putExtra("bannerlistbean", bannerlistBeansBean);
					startActivity(intent);
				}
			}
			break;
		default:
			break;
		}
	}

	/*
	 * 创建订单
	 */
	@SuppressLint("SimpleDateFormat")
	protected void createOrder() {
		if (is_Create_Order) {
			if (IsChinese.iszhongwen(et_order_comment.getText().toString()
					.trim())) {
				AppConfig.getInstance().setCanCreateOrder(true);
				showdialog("备注不能含有非法字符");
				return;
			}
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy");
			nowTime = Calendar.getInstance();
			mdate = sDateFormat.format(nowTime.getTime());
			midstr = order_date.getText().toString().replace("月", "-")
					.replace("日", "").subSequence(2, 7).toString();
			parms.clear();
			parms.put(
					"order_time",
					order_date
							.getText()
							.subSequence(order_date.getText().length() - 11,
									order_date.getText().length()).toString());
			parms.put("user_id", getUerId());
			parms.put("user_type", "3");
			parms.put("order_date",
					mdate.toString().trim() + "-" + midstr.trim());
			parms.put("good", "18");
			parms.put("paytype", "3");
			parms.put("totalnum", "1");
			parms.put("coupon_id", "");
			if (bannerlistbean != null) {
				Gson gson = new Gson();
				InappUrlbean inappurlbean = gson.fromJson(
						bannerlistbean.getUrl(), InappUrlbean.class);
				TCAgent.onEvent(PlaceorderActivity.this,
						"下单品类_" + inappurlbean.getId());
				parms.put("category_id", inappurlbean.getId());
			} else {
				parms.put("category_id", "1");
			}
			parms.put("comment",
					et_order_comment.getText().toString().replace("\n", "")
							.replace(" ", ""));
			parms.put("order_place", adsid);
			postdate(parms, Constants.getcreateorder, placeOrderHandler,
					CREATORDERSUCCED, CREATORDERFAILD, false, true);
			AppConfig.getInstance().setCanCreateOrder(false);
		}
	}

	/** 隐藏软键盘 **/
	public void closeKeyBoard() {
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return false;
	}
}
