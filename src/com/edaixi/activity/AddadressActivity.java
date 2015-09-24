package com.edaixi.activity;

import java.util.Arrays;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.edaixi.data.AddressIntentdata;
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.AddressBean;
import com.edaixi.modle.CitysAreaBean;
import com.edaixi.modle.HttpCommonBean;
import com.edaixi.modle.MapItemBean;
import com.edaixi.util.Constants;
import com.edaixi.util.IsChinese;
import com.edaixi.util.LogUtil;
import com.edaixi.util.NetUtil;
import com.edaixi.util.SaveUtils;
import com.edaixi.wheelpicker.widget.CitiesPopup;
import com.google.gson.Gson;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("HandlerLeak")
public class AddadressActivity extends BaseActivity implements OnClickListener,
		OnGeocodeSearchListener {
	private HashMap<String, String> parm;
	private com.edaixi.view.CleanEditText reciever;
	private com.edaixi.view.CleanEditText street_edit;
	private TextView tv_address_edit;
	private AutoCompleteTextView tel_edit;
	private final int CREATADSSUCCED = 0;
	private final int CREATADSFAILD = 1;
	private final int GETADSSUCCED = 2;
	private final int GETADSFAILD = 3;
	private final int GETCITYSUCESS = 6;
	private final int GETCITYFAIL = 7;
	private final int VERIFYCITYSUCESS = 8;
	private final int VERIFYCITYFAIL = 9;
	private TextView add_save_btn;
	private SaveUtils saveUtils;
	private ImageView addaddress_back_btn;
	private CitiesPopup cityPopup;
	private RelativeLayout place_order;
	private RelativeLayout rl_edit_phone;
	private RelativeLayout rl_to_mapview;
	private RelativeLayout rl_city_area;
	private CitysAreaBean cityAreaBean;
	private String[] city = null;
	private String[][] area = null;
	private TextView titel_text;
	private TextView tv_lady;
	private TextView tv_men;
	private TextView tv_select_city;
	private TextView tv_select_area;
	private String path = Constants.getcreataddress;
	private String address_id;
	private final int UPDATEADSSUCCED = 4;
	private final int UPDATEADSFAILD = 5;
	private AddressBean lastaddressbean;
	private String address_Gender = "女士";
	private ImageView clear_btn_tel;
	private ImageView edit_map_logo;
	private GeocodeSearch geocoderSearch;
	private String resultCityARea;
	private String resultCityString;
	private double latitude = 0;
	private double longitude = 0;
	private int cityIndex = 0;
	private boolean IS_SHOW_CITYPOP = false;
	private boolean IS_SHOW_AREAPOP = false;

	private AddressHandler addressHandler = new AddressHandler();
	private MapItemBean serializableMapBean;

	public class AddressHandler extends Handler {
		private Gson gson = new Gson();

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CREATADSSUCCED:
				HttpCommonBean jsonCommonBean = gson.fromJson(
						(String) msg.obj.toString(), HttpCommonBean.class);
				if (jsonCommonBean.isRet()) {
					if (getIntent().getExtras() != null) {
						if (getIntent().getExtras().get("FROM")
								.equals(AddressIntentdata.FROMPLACEORDER)) {
							Intent createIntent = new Intent();
							createIntent.putExtra(
									KeepingData.Create_Address_Sucess, true);
							createIntent.putExtra("test", "Test");
							setResult(Activity.RESULT_OK, createIntent);
							try {
								Thread.sleep(200);
								finish(0, 0);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						if (getIntent().getExtras().get("FROM")
								.equals(AddressIntentdata.FROMADSLIST)) {
							try {
								Thread.sleep(200);
								finish(0, 0);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				} else {
					showdialog(jsonCommonBean.getError());
				}

				break;
			case CREATADSFAILD:
				showdialog("地址创建失败");
				break;
			case GETADSSUCCED:
				if (getIntent().getExtras() != null) {
					if (getIntent().getExtras().containsKey("FROM")) {
						try {
							Thread.sleep(200);
							finish(0, 0);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			case GETADSFAILD:
				break;
			case UPDATEADSSUCCED:
				if (getIntent().getExtras() != null) {
					if (getIntent().getExtras().containsKey("FROM")) {
						try {
							Thread.sleep(200);
							finish(0, 0);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			case UPDATEADSFAILD:
				showdialog("更新地址失败");
				break;
			case GETCITYSUCESS:
				String cityResultSucess = (String) msg.obj;
				LogUtil.e("获取开通城市区域-" + cityResultSucess);
				String data;
				try {
					data = new JSONObject(cityResultSucess).getString("data");
					AppConfig.getInstance().setCityareastr(data);
					Gson gson = new Gson();
					cityAreaBean = gson.fromJson(data, CitysAreaBean.class);
					fillCityData(cityAreaBean);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case GETCITYFAIL:
				// showdialog("获取开通城市失败");
				break;
			case VERIFYCITYSUCESS:
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					boolean retData = jsonObject.getBoolean("ret");
					if (retData) {
						String verifyData = jsonObject.getString("data");
						JSONObject jsonObjectData = new JSONObject(verifyData);
						boolean verifyRes = jsonObjectData
								.getBoolean("on_service");
						if (verifyRes) {
							String flag = String.valueOf(jsonObjectData
									.getInt("flag"));
							if (flag.equals("0")) {
								IS_SHOW_CITYPOP = false;
								IS_SHOW_AREAPOP = false;
								tv_address_edit
										.setText(saveUtils
												.getStrSP(KeepingData.USER_ADDRESS_FILL));
								tv_select_city.setText(saveUtils
										.getStrSP(KeepingData.Current_City));
								tv_select_area.setText(saveUtils
										.getStrSP(KeepingData.Current_Area));
							} else if (flag.equals("10")) {
							}
						} else {
							String errorMsg = jsonObjectData
									.getString("message");
							if (errorMsg != null)
								showdialog(errorMsg);
							IS_SHOW_CITYPOP = true;
							IS_SHOW_AREAPOP = true;
							tv_address_edit.setText("");
							tv_select_city.setText("请选择城市");
							tv_select_area.setText("请选择区域");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				LogUtil.e("判断是否在服务范围成功：" + msg.obj.toString());
				break;
			case VERIFYCITYFAIL:
				LogUtil.e("判断是否在服务范围失败：" + msg.obj.toString());
				IS_SHOW_CITYPOP = false;
				IS_SHOW_AREAPOP = false;
				tv_address_edit.setText(saveUtils
						.getStrSP(KeepingData.USER_ADDRESS_FILL));
				tv_select_city.setText(saveUtils
						.getStrSP(KeepingData.Current_City));
				tv_select_area.setText(saveUtils
						.getStrSP(KeepingData.Current_Area));
				break;
			}
			super.handleMessage(msg);
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addadressactivity);
		init(this);
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		initView();
	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("AddadressActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("AddadressActivity");
		MobclickAgent.onPause(this);
	}

	@SuppressWarnings("rawtypes")
	@SuppressLint("HandlerLeak")
	private void initView() {
		saveUtils = new SaveUtils(getContext());
		parm = new HashMap<String, String>();
		getCityArea();
		reciever = (com.edaixi.view.CleanEditText) findViewById(R.id.reciever);
		reciever.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		reciever.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					reciever.hintCleanLogo();
				}
			}
		});
		tv_address_edit = (TextView) findViewById(R.id.tv_address_edit);
		tv_address_edit.setOnClickListener(this);
		tv_lady = (TextView) findViewById(R.id.tv_lady);
		tv_lady.setOnClickListener(this);
		tv_select_city = (TextView) findViewById(R.id.tv_select_city);
		tv_select_city.setOnClickListener(this);
		tv_select_area = (TextView) findViewById(R.id.tv_select_area);
		tv_select_area.setOnClickListener(this);
		tv_men = (TextView) findViewById(R.id.tv_men);
		tv_men.setOnClickListener(this);
		clear_btn_tel = (ImageView) findViewById(R.id.clear_btn_tel);
		clear_btn_tel.setOnClickListener(this);
		street_edit = (com.edaixi.view.CleanEditText) findViewById(R.id.street_edit);
		street_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		street_edit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					street_edit.hintCleanLogo();
				}
			}
		});
		tel_edit = (AutoCompleteTextView) findViewById(R.id.tel_edit);
		tel_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					clear_btn_tel.setVisibility(View.VISIBLE);
				} else {
					clear_btn_tel.setVisibility(View.GONE);
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
		tel_edit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					clear_btn_tel.setVisibility(View.INVISIBLE);
				}
			}
		});
		String[] arr = { saveUtils.getStrSP(KeepingData.PHONE) };
		ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,
				R.layout.address_fill_tel, R.id.tv_show_save_tel, arr);
		tel_edit.setAdapter(arrayAdapter);
		add_save_btn = (TextView) findViewById(R.id.add_save_btn);
		add_save_btn.setOnClickListener(this);
		place_order = (RelativeLayout) findViewById(R.id.place_order);
		rl_edit_phone = (RelativeLayout) findViewById(R.id.rl_edit_phone);
		rl_edit_phone.setOnClickListener(this);
		rl_to_mapview = (RelativeLayout) findViewById(R.id.rl_to_mapview);
		rl_to_mapview.setOnClickListener(this);
		rl_city_area = (RelativeLayout) findViewById(R.id.rl_city_area);
		rl_city_area.setOnClickListener(this);
		addaddress_back_btn = (ImageView) findViewById(R.id.addaddress_back_btn);
		edit_map_logo = (ImageView) findViewById(R.id.edit_map_logo);
		edit_map_logo.setOnClickListener(this);
		titel_text = (TextView) findViewById(R.id.titel_text);
		addaddress_back_btn.setOnClickListener(this);
		parserjson();
		if (lastaddressbean != null) {
			reciever.setText(lastaddressbean.getUsername());
			reciever.hintCleanLogo();
			tv_select_city.setText(lastaddressbean.getCity());
			tv_select_area.setText(lastaddressbean.getArea());
			tel_edit.setText(lastaddressbean.getTel());
			clear_btn_tel.setVisibility(View.INVISIBLE);
		}
	}

	private void parserjson() {
		Gson gson = new Gson();
		if (getIntent().getExtras() != null) {
			cityAreaBean = gson.fromJson(AppConfig.getInstance()
					.getCityareastr(), CitysAreaBean.class);
			if (getIntent().getExtras().containsKey("TYPE")) {
				if (getIntent().getExtras().get("TYPE")
						.equals(AddressIntentdata.ADDADS)) {
					// 添加地址的逻辑
					titel_text.setText("添加");
					// --------添加地址---新逻辑------------------------
					if (!AppConfig.getInstance().isLocationFail()) {
						// 定位成功
						verifyCityArea(
								saveUtils.getStrSP(KeepingData.Current_City),
								saveUtils.getStrSP(KeepingData.Current_Area),
								"0");
					} else {
						// 定位失败
						IS_SHOW_CITYPOP = true;
						IS_SHOW_AREAPOP = true;
						showdialog("获取位置失败");
						tv_address_edit.setText("");
						tv_select_city.setText(saveUtils
								.getStrSP(KeepingData.User_City));
						tv_select_area.setText("请选择区域");
					}
					// --------添加地址---新逻辑------------------------
					path = Constants.getcreataddress;
				} else {
					// 编辑地址的逻辑
					titel_text.setText("编辑");
					lastaddressbean = (AddressBean) getIntent().getExtras()
							.getSerializable(AddressIntentdata.ADDRESSBEAN);
					if (lastaddressbean != null) {
						address_id = lastaddressbean.getAddress_id();
					}
					if (lastaddressbean != null
							&& lastaddressbean.getAddress_line_1() != null) {
						tv_address_edit.setText(lastaddressbean
								.getAddress_line_1());
					}
					if (lastaddressbean != null
							&& lastaddressbean.getAddress_line_2() != null) {
						street_edit
								.setText(lastaddressbean.getAddress_line_2());
					}
					if (lastaddressbean != null
							&& lastaddressbean.getGender() != null
							&& lastaddressbean.getGender().equals("男士")) {
						address_Gender = "男士";
						Drawable drawableMen = getResources().getDrawable(
								R.drawable.address_press);
						drawableMen.setBounds(0, 0,
								drawableMen.getMinimumWidth(),
								drawableMen.getMinimumHeight()); // 设置边界
						tv_men.setCompoundDrawables(null, null, drawableMen,
								null);
						Drawable drawableLadyDefault = getResources()
								.getDrawable(R.drawable.address_default);
						drawableLadyDefault.setBounds(0, 0,
								drawableLadyDefault.getMinimumWidth(),
								drawableLadyDefault.getMinimumHeight()); // 设置边界
						tv_lady.setCompoundDrawables(null, null,
								drawableLadyDefault, null);
					}
					IS_SHOW_CITYPOP = true;
					IS_SHOW_AREAPOP = true;
					path = Constants.getupdateaddress;
				}
			}
		}
	}

	private void fillCityData(CitysAreaBean cityAreaBean) {
		city = new String[cityAreaBean.cities.size()];

		for (int i = 0; i < cityAreaBean.cities.size(); i++) {
			city[i] = cityAreaBean.cities.get(i);
		}
		area = new String[cityAreaBean.cities.size()][];

		for (int j = 0; j < cityAreaBean.areas.size(); j++) {

			area[j] = new String[cityAreaBean.areas.get(j).size()];
			for (int k = 0; k < cityAreaBean.areas.get(j).size(); k++) {
				area[j][k] = cityAreaBean.areas.get(j).get(k);
			}
		}
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clear_btn_tel:
			tel_edit.setText("");
			clear_btn_tel.setVisibility(View.GONE);
			break;
		case R.id.edit_map_logo:
			if (!AppConfig.getInstance().isLocationFail()) {
				// 定位成功
				verifyCityArea(saveUtils.getStrSP(KeepingData.Current_City),
						saveUtils.getStrSP(KeepingData.Current_Area), "0");
			}
			break;
		case R.id.tv_select_city:
		case R.id.tv_select_area:
		case R.id.rl_city_area:
			invisibleInputmethod(rl_city_area);
			if (IS_SHOW_CITYPOP && IS_SHOW_AREAPOP) {
				showCityAreaSelect();
			}
			// -----------------------------------------------
			// if (getIntent().getExtras().get("FROM")
			// .equals(AddressIntentdata.FROMADSLIST)) {
			// invisibleInputmethod(rl_city_area);
			// showCityAreaSelect();
			// } else if (getIntent().getExtras().get("FROM")
			// .equals(AddressIntentdata.FROMPLACEORDER)) {
			// invisibleInputmethod(rl_city_area);
			// showAreaSelect();
			// }
			// -----------------------------------------------
			break;
		case R.id.tv_address_edit:
			invisibleInputmethod(tv_address_edit);
			Intent searchIntent = new Intent(getContext(),
					SearchAddressActivity.class);
			searchIntent.putExtra("SearchKeyWord", tv_address_edit.getText()
					.toString());
			startActivityForResult(searchIntent,
					AddressIntentdata.Result_For_MapView);
			break;
		case R.id.addaddress_back_btn:
			finish(0, 0);
			break;
		case R.id.tv_lady:
			address_Gender = "女士";
			Drawable drawableLady = getResources().getDrawable(
					R.drawable.address_press);
			drawableLady.setBounds(0, 0, drawableLady.getMinimumWidth(),
					drawableLady.getMinimumHeight()); // 设置边界
			tv_lady.setCompoundDrawables(null, null, drawableLady, null);
			Drawable drawableMenDefault = getResources().getDrawable(
					R.drawable.address_default);
			drawableMenDefault.setBounds(0, 0,
					drawableMenDefault.getMinimumWidth(),
					drawableMenDefault.getMinimumHeight()); // 设置边界
			tv_men.setCompoundDrawables(null, null, drawableMenDefault, null);
			break;
		case R.id.tv_men:
			address_Gender = "男士";
			Drawable drawableMen = getResources().getDrawable(
					R.drawable.address_press);
			drawableMen.setBounds(0, 0, drawableMen.getMinimumWidth(),
					drawableMen.getMinimumHeight()); // 设置边界
			tv_men.setCompoundDrawables(null, null, drawableMen, null);
			Drawable drawableLadyDefault = getResources().getDrawable(
					R.drawable.address_default);
			drawableLadyDefault.setBounds(0, 0,
					drawableLadyDefault.getMinimumWidth(),
					drawableLadyDefault.getMinimumHeight()); // 设置边界
			tv_lady.setCompoundDrawables(null, null, drawableLadyDefault, null);
			break;
		case R.id.add_save_btn:
			invisibleInputmethod(add_save_btn);
			if (!NetUtil.getNetworkState(this)) {
				showdialog("网络不可用，请检查您的网络连接");
				return;
			}
			if (TextUtils.isEmpty(reciever.getText().toString().trim())) {
				showdialog("姓名不能为空");
				return;
			}
			if (TextUtils.isEmpty(tv_select_city.getText().toString().trim())
					|| TextUtils.isEmpty(tv_select_area.getText().toString()
							.trim())) {
				showdialog("城市或区域不能为空");
				return;
			}
			if (IsChinese.iszhongwen(reciever.getText().toString().trim())) {
				showdialog("姓名不能含有非法字符");
				return;
			}
			if (TextUtils.isEmpty(address_Gender)) {
				showdialog("您是先生还是女士呢？");
				return;
			}
			if (TextUtils.isEmpty(tel_edit.getText().toString().trim())) {
				showdialog("手机号不能为空");
				return;
			}
			if (tel_edit.getText().toString().trim().length() != 11) {
				showdialog("手机号格式不正确");
				return;
			}
			if (TextUtils.isEmpty(tv_address_edit.getText().toString().trim())) {
				showdialog("地址不能为空");
				return;
			}
			if (TextUtils.isEmpty(street_edit.getText().toString().trim())) {
				showdialog("详细地址不能为空");
				return;
			}
			if (IsChinese.iszhongwen(street_edit.getText().toString().trim())) {
				showdialog("详细地址不能含有非法字符");
				return;
			}

			if (path.equals(Constants.getupdateaddress)) {
				// 编辑地址
				parm.clear();
				parm.put("address_id", address_id);
				parm.put("username", reciever.getText().toString().trim());
				parm.put("city", tv_select_city.getText().toString().trim()
						.substring(0, 2).trim());
				if ((serializableMapBean != null)
						&& serializableMapBean.getMapItemLog() != null) {
					parm.put("customer_lat",
							serializableMapBean.getMapItemLat());
					parm.put("customer_lng",
							serializableMapBean.getMapItemLog());
					parm.put("is_edit", "false");
				} else {
					parm.put("customer_lat", "0");
					parm.put("customer_lng", "0");
					parm.put("is_edit", "true");
				}
				parm.put("province", "");
				parm.put("tel", tel_edit.getText().toString().replace(" ", "")
						.replace("\n", "").trim());
				parm.put("area", tv_select_area.getText().toString().trim());
				parm.put("address_line_1", tv_address_edit.getText().toString()
						.replace(" ", "").replace("\n", "").trim());
				parm.put("address_line_2", street_edit.getText().toString()
						.replace(" ", "").replace("\n", "").trim());
				parm.put("gender", address_Gender);
				postdate(parm, path, addressHandler, UPDATEADSSUCCED,
						UPDATEADSFAILD, true, true);
			} else {
				parm.clear();
				parm.put("user_id", saveUtils.getStrSP(KeepingData.USER_ID));
				parm.put("username", reciever.getText().toString().trim());
				if ((serializableMapBean != null)
						&& serializableMapBean.getMapItemLog() != null) {
					parm.put("customer_lat",
							serializableMapBean.getMapItemLat());
					parm.put("customer_lng",
							serializableMapBean.getMapItemLog());
					parm.put("is_edit", "false");
				} else {
					parm.put("customer_lat", "0");
					parm.put("customer_lng", "0");
					parm.put("is_edit", "true");
				}
				parm.put("province", "");
				parm.put("tel", tel_edit.getText().toString().replace(" ", "")
						.replace("\n", "").trim());
				parm.put("city", tv_select_city.getText().toString().trim());
				parm.put("area", tv_select_area.getText().toString().trim());
				parm.put("address_line_1", tv_address_edit.getText().toString()
						.replace(" ", "").replace("\n", "").trim());
				parm.put("address_line_2", street_edit.getText().toString()
						.replace(" ", "").replace("\n", "").trim());
				parm.put("gender", address_Gender);
				postdate(parm, path, addressHandler, CREATADSSUCCED,
						CREATADSFAILD, true, true);
			}
			break;
		}
	}

	/** 判断城市区域是否在服务范围 ***/
	public void verifyCityArea(String cityStr, String areaStr, String flag) {
		parm.clear();
		parm.put("city", cityStr);
		parm.put("area", areaStr);
		parm.put("flag", flag);
		postdate(parm, Constants.getverifyaddress, addressHandler,
				VERIFYCITYSUCESS, VERIFYCITYFAIL, false, true);
	}

	/** 判断区域是否在服务范围 **/
	public boolean judgeIsRightArea(String areaSting) {
		if (area != null) {
			for (int i = 0; i < area.length; i++) {
				for (int j = 0; j < area[i].length; j++) {
					if (tv_select_area.getText().toString()
							.contains(area[i][j])) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/** 获取开通城市和区域 **/
	public void getCityArea() {
		parm.clear();
		getdate(parm, Constants.getcitiesoptions, addressHandler,
				GETCITYSUCESS, GETCITYFAIL, false, true, false);
	}

	private void showAreaSelect() {
		if ((city == null) || (city.length < 1)) {
			showdialog("网络异常，稍后重试.");
		} else {
			cityPopup = new CitiesPopup(getContext(), sortCity(city,
					getCityIndex(city)), sortArea(area, getCityIndex(city)),
					true, tv_select_city, tv_select_area, true);
			cityPopup.show(place_order);
		}
	}

	private void showCityAreaSelect() {
		if ((city == null) || (city.length < 1)) {
			showdialog("网络异常，稍后重试.");
		} else {
			cityPopup = new CitiesPopup(getContext(), city, area, true,
					tv_select_city, tv_select_area, false);
			cityPopup.show(place_order);
		}
	}

	/** 获取定位城市索引 **/
	public int getCityIndex(String[] city) {
		for (int i = 0; i < city.length; i++) {
			if (city[i].equals(saveUtils.getStrSP(KeepingData.User_City))) {
				cityIndex = i;
			}
		}
		return cityIndex;
	}

	/** 使用 Arrays.copyOf重新排序定位城市 **/
	public static String[] sortCity(String[] arr, int index) {
		String[] copyArr = Arrays.copyOf(arr, arr.length);
		copyArr[0] = arr[index];
		copyArr[index] = arr[0];
		return copyArr;
	}

	/** 使用 Arrays.copyOf重新排序定位区域 **/
	public static String[][] sortArea(String[][] arr, int index) {
		String[][] copyArr = Arrays.copyOf(arr, arr.length);
		copyArr[0] = arr[index];
		copyArr[index] = arr[0];
		return copyArr;
	}

	/** 选择高德地图回调结果 **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == AddressIntentdata.Result_For_MapView
				&& resultCode == RESULT_OK) {
			if (intent != null) {
				Bundle bundleExtra = intent.getExtras();
				serializableMapBean = (MapItemBean) bundleExtra
						.getSerializable(KeepingData.select_Map_Item);
				if (serializableMapBean != null) {
					if (serializableMapBean.getMapItemLat() != null) {
						// getLatlon(serializableMapBean.getMapItemName());
						IS_SHOW_CITYPOP = false;
						IS_SHOW_AREAPOP = false;
						tv_address_edit.setText(serializableMapBean
								.getMapItemTitle());
						tv_select_city.setText(serializableMapBean
								.getMapItemCity());
						tv_select_area.setText(serializableMapBean
								.getMapItemArea());
						verifyCityArea(tv_select_city.getText().toString(),
								tv_select_area.getText().toString(), "10");
					} else {
						IS_SHOW_CITYPOP = true;
						IS_SHOW_AREAPOP = true;
						tv_address_edit.setText(serializableMapBean
								.getMapItemName());
					}
				}
			}
		}
	}

	/**
	 * 响应逆地理编码
	 */
	public void getAddress(LatLonPoint latLonPoint) {
		// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 1000,
				GeocodeSearch.AMAP);
		// 设置同步逆地理编码请求
		geocoderSearch.getFromLocationAsyn(query);
	}

	/**
	 * 响应地理编码
	 * 
	 * @param name
	 */
	public void getLatlon(String name) {
		GeocodeSearch geocodeSearch = new GeocodeSearch(this);
		geocodeSearch.setOnGeocodeSearchListener(this);
		GeocodeQuery query = new GeocodeQuery(name,
				saveUtils.getStrSP(KeepingData.City_Code));
		geocoderSearch.getFromLocationNameAsyn(query);
	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				latitude = address.getLatLonPoint().getLatitude();
				longitude = address.getLatLonPoint().getLongitude();
				resultCityARea = address.getDistrict();
				resultCityString = address.getCity();
				tv_select_area.setText(resultCityARea);
				tv_select_city.setText(resultCityString);
			}
		}
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				String formatAddress = result.getRegeocodeAddress()
						.getFormatAddress().toString();
			}
		}
	}
}
