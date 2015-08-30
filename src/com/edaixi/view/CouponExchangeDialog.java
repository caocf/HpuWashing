package com.edaixi.view;

import com.edaixi.activity.R;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.view.CancleOrderDialog.CancleDialogButtonListener;

import de.greenrobot.event.EventBus;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * <p>
 * Title: CustomDialog
 * 
 * @author wei-spring
 * @version 1.0
 */
public class CouponExchangeDialog extends Dialog implements
		android.view.View.OnClickListener {
	int layoutRes;
	Context context;
	private Button confirmBtn;
	private Button cancelBtn;
	private TextView tv_tips;
	private EditText exchange_coupon_code;
	private String exchangeCodeString;

	public CouponExchangeDialog(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * @author wei-spring
	 * @param context
	 * @param resLayout
	 */
	public CouponExchangeDialog(Context context, int resLayout) {
		super(context);
		this.context = context;
		this.layoutRes = resLayout;
	}

	/**
	 * @author wei-spring
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public CouponExchangeDialog(Context context, int theme, int resLayout) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
	}

	/**
	 * 自定义取消订单Dialog监听器
	 */
	public interface ExchangeDialogButtonListener {
		/**
		 * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
		 */
		public void setExchangeCoupon(String couponCode);
	}

	private ExchangeDialogButtonListener exchangeBtnListener;

	/**
	 * 带监听器参数的构造函数
	 */

	// 设置回调接口(监听器)的方法
	public void setYourListener(ExchangeDialogButtonListener exchangeBtnListener) {
		this.exchangeBtnListener = exchangeBtnListener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layoutRes);

		confirmBtn = (Button) findViewById(R.id.exchange_coupon_confirm_btn);
		cancelBtn = (Button) findViewById(R.id.exchange_coupon_cancel_btn);
		exchange_coupon_code = (EditText) findViewById(R.id.exchange_coupon_code);
		tv_tips = (TextView) findViewById(R.id.tv_tips);

		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		exchange_coupon_code.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				tv_tips.setVisibility(View.GONE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				tv_tips.setVisibility(View.GONE);
			}

			@Override
			public void afterTextChanged(Editable s) {
				tv_tips.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void show() {
		super.show();
		this.setCanceledOnTouchOutside(false);
	}

	public String getExchngeCode() {
		exchangeCodeString = exchange_coupon_code.getText().toString().trim();
		return exchangeCodeString;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exchange_coupon_confirm_btn:
			if (TextUtils.isEmpty(getExchngeCode())) {
				tv_tips.setVisibility(View.VISIBLE);
				return;
			}
			exchangeBtnListener.setExchangeCoupon(getExchngeCode());
			this.cancel();
			break;
		case R.id.exchange_coupon_cancel_btn:
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(exchange_coupon_code.getWindowToken(),
					0);
			this.cancel();
			break;
		}
	}
}