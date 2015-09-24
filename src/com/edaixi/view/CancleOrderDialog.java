package com.edaixi.view;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.edaixi.activity.R;
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.OrderListItemBean;
import com.edaixi.util.Constants;
import com.edaixi.util.LogUtil;
import com.edaixi.util.MyhttpUtils;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.util.SaveUtils;
import de.greenrobot.event.EventBus;

/**
 * custom dialog ,when you need,below demo is your want
 * 
 * CustomDialog dialog=new CustomDialog(content, R.style.customdialog_style,
 * R.layout.customdialog,"这是取消订单弹窗dialog"); dialog.show();
 * 
 * @author wei-spring
 * 
 */
@SuppressLint("HandlerLeak")
public class CancleOrderDialog extends Dialog implements OnClickListener {
	int layoutRes;
	private Context context;
	private TextView tv_btn_confim;
	private TextView tv_btn_cancle;
	private TextView tv_cancle_order_tips;
	private ListView lv_show_cancle_item;
	private OrderListItemBean orderItem;
	String[] tipItemStrings = null;
	boolean[] is_select_cancle = null;
	private MyhttpUtils myhttpUtils;
	private SaveUtils saveUtils;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 10:
				String data;
				LogUtil.e("进来了，请求取消原因接口。");
				try {
					data = new JSONObject(msg.obj.toString()).getString("data");
					JSONArray jsonArray = new JSONArray(data);
					tipItemStrings = new String[jsonArray.length()];
					is_select_cancle = new boolean[tipItemStrings.length];
					for (int i = 0; i < jsonArray.length(); i++) {
						String jo = (String) jsonArray.opt(i);
						tipItemStrings[i] = jo;
						if (i == 0) {
							is_select_cancle[0] = true;
						}
					}
					final CancleListAdapter cAdapter = new CancleListAdapter();
					lv_show_cancle_item.setAdapter(cAdapter);
					lv_show_cancle_item
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									for (int i = 0; i < 4; i++) {
										is_select_cancle[i] = false;
									}
									is_select_cancle[position] = true;
									cAdapter.notifyDataSetChanged();
									AppConfig.getInstance()
											.setCancleOrderString(
													tipItemStrings[position]);
								}
							});
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 11:
				Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	private CancleOrderDialog(Context context, boolean show_text) {
		super(context);
		this.context = context;
	}

	/**
	 * self define layout
	 * 
	 * @param context
	 * @param resLayout
	 */
	public CancleOrderDialog(Context context, int resLayout) {
		super(context);
		this.context = context;
		this.layoutRes = resLayout;
	}

	/**
	 * self define layout theme
	 * 
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public CancleOrderDialog(Context context, int theme, int resLayout,
			OrderListItemBean orderItem) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
		this.orderItem = orderItem;
		saveUtils = new SaveUtils(context);
		myhttpUtils = new MyhttpUtils(context);
		getCancleOrderReasons();
	}

	private void getCancleOrderReasons() {
		HashMap<String, String> parm = new HashMap<String, String>();
		if (saveUtils.getStrSP(KeepingData.User_City_Id) != null) {
			parm.put("city_id", saveUtils.getStrSP(KeepingData.User_City_Id));
		} else {
			parm.put("city_id", "1");
		}
		String url = myhttpUtils.getUrl(parm, Constants.getcancelorderreasons,context);
		myhttpUtils.getdate(context, handler, 10, 11, parm,
				url, false, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layoutRes);

		tv_btn_confim = (TextView) findViewById(R.id.tv_btn_confim);
		tv_btn_confim.setOnClickListener(this);
		tv_btn_cancle = (TextView) findViewById(R.id.tv_btn_cancle);
		tv_btn_cancle.setOnClickListener(this);
		tv_cancle_order_tips = (TextView) findViewById(R.id.tv_cancle_order_tips);
		lv_show_cancle_item = (ListView) findViewById(R.id.lv_show_cancle_item);

		if ((orderItem.getDelivery_status() != null)
				&& (orderItem.getDelivery_status().equals("9"))) {

			tv_cancle_order_tips.setVisibility(View.VISIBLE);
		} else {
			tv_cancle_order_tips.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_btn_confim:
			if ((orderItem != null) && (orderItem.getOrder_id() != null)) {
				EventBus.getDefault().post(
						new OrderListAdapterEvent(orderItem.getOrder_id()));
			}if(isShowing()){
				dismiss();
			}
			
			break;
		case R.id.tv_btn_cancle:
			if(isShowing()){
				dismiss();
			}
			break;
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	private class CancleListAdapter extends BaseAdapter {
		@SuppressWarnings("unused")
		private ViewHolder holder;

		@Override
		public int getCount() {
			return tipItemStrings.length;
		}

		@Override
		public Object getItem(int position) {
			return tipItemStrings[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder viHolder = null;
			if (convertView != null) {
				view = convertView;
				viHolder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(context, R.layout.cancle_order_item, null);
				viHolder = new ViewHolder();
				viHolder.iv_reason_select = (ImageView) view
						.findViewById(R.id.iv_reason_select);
				viHolder.tv_cancle_reason = (TextView) view
						.findViewById(R.id.tv_cancle_reason);
				view.setTag(viHolder);
			}
			viHolder.tv_cancle_reason.setText(tipItemStrings[position]);
			if (is_select_cancle[position]) {
				viHolder.iv_reason_select.setVisibility(View.VISIBLE);
			} else {
				viHolder.iv_reason_select.setVisibility(View.GONE);
			}
			return view;
		}

		private class ViewHolder {
			public TextView tv_cancle_reason;
			public ImageView iv_reason_select;
		}

	}
}