package com.edaixi.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.edaixi.adapter.MyExpandableListViewAdapter;
import com.edaixi.modle.ClothingOrderInfo;
import com.edaixi.util.Constants;
import com.edaixi.util.ParseOrderList;
import com.edaixi.util.SaveUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

/**
 * activity for clothing detail...
 * 
 * @author wei-spring
 */
public class ClothingDetialActivity extends BaseActivity {

	private static final String TAG = "ClothingDetialActivity.class";
	private String order_id;
	private String order_sn;
	public static final int TUREMESSAGECLOTHING = 15;
	public static final int ERRORMESSAGECLOTHING = 16;
	private HashMap<String, String> clothingDeliveryParams = new HashMap<String, String>();
	private SaveUtils saveUtils;
	private ParseOrderList parseOrderList;
	private List<String> child_list;
	private MyExpandableListViewAdapter myadapter;
	private ArrayList<ClothingOrderInfo> parseClothingDetail;

	@ViewInject(R.id.exlv_order_clothing)
	private ExpandableListView exlv_order_clothing;
	@ViewInject(R.id.tv_clothingdetail_title)
	private TextView tv_clothingdetail_title;
	@ViewInject(R.id.tv_clothingdetail_price)
	private TextView tv_clothingdetail_price;

	@Override
	protected boolean onBackKeyDown() {
		finish(0,0);
		return false;
	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("ClothingDetialActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("ClothingDetialActivity"); // 保证 onPageEnd
															// 在onPause 之前调用,因为
															// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 15:
				String clothingResultSucess = (String) msg.obj;
				Log.d(TAG, "clothing-scuess-info" + clothingResultSucess);
				parseClothingDetail = parseOrderList
						.parseClothingDetail(clothingResultSucess);

				if (parseClothingDetail != null
						&& parseClothingDetail.size() > 0) {
					List<String> clothingCondition = getClothingCondition(parseClothingDetail);
					if (myadapter == null) {
						myadapter = new MyExpandableListViewAdapter(
								parseClothingDetail, clothingCondition,
								getApplicationContext());
						exlv_order_clothing.setAdapter(myadapter);
						double getOrignPrice = 0;
						for (ClothingOrderInfo clothingOrderInfo : parseClothingDetail) {
							getOrignPrice += Double
									.parseDouble(clothingOrderInfo
											.getOriginal_price());
						}
						double getCurrentPrice = 0;
						for (ClothingOrderInfo clothingOrderInfo : parseClothingDetail) {
							getCurrentPrice += Double
									.parseDouble(clothingOrderInfo
											.getCurrent_price());
						}
						DecimalFormat dcmFmt = new DecimalFormat("0.00");
						double cut = (getCurrentPrice / getOrignPrice) * 10;
					}
				}
				tv_clothingdetail_price.setText(" 收到衣物共"
						+ parseClothingDetail.size() + "件!");
				break;
			case 16:
				String clothingResultFail = (String) msg.obj;
				Log.d(TAG, "clothing-fail-info" + clothingResultFail);
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_clothingdetail);
		ViewUtils.inject(this);
		init(this);
		exlv_order_clothing.setGroupIndicator(null);
		saveUtils = new SaveUtils(getApplicationContext());
		parseOrderList = new ParseOrderList();

		Intent intent = this.getIntent();
		order_sn = intent.getStringExtra("order_sn");
		order_id = intent.getStringExtra("order_id");
		if (order_id != "") {
			tv_clothingdetail_title.setText("  订单" + order_sn + "已经分拣完成");
		}
		getClothingDetail();
	}

	public void goBack(View view) {
		finish(0,0);
	}

	/**
	 * get clothing condition data
	 * 
	 * @return
	 */
	public List<String> getClothingCondition(
			ArrayList<ClothingOrderInfo> parseClothingDetail) {
		child_list = new ArrayList<String>();
		for (ClothingOrderInfo clothingOrderInfo : parseClothingDetail) {
			String cloth_condition = clothingOrderInfo.getCloth_condition();
			child_list.add(cloth_condition);
		}
		return child_list;
	}

	/**
	 * get clothing detail from sevice
	 */
	public void getClothingDetail() {
		if (saveUtils.getStrSP("user_id") != null) {
			clothingDeliveryParams
					.put("user_id", saveUtils.getStrSP("user_id"));
		}
		clothingDeliveryParams.put("order_id", order_id);
		this.getdate(clothingDeliveryParams, Constants.getorderclothing,
				handler, TUREMESSAGECLOTHING, ERRORMESSAGECLOTHING, true, true,
				false);
	}

}
