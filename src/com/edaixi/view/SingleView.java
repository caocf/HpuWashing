package com.edaixi.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edaixi.activity.R;

public class SingleView extends RelativeLayout implements Checkable,
		OnClickListener {

	private ImageView iv_pay_type_logo;
	private TextView pay_type_title;
	private TextView pay_recharge_title;
	private CheckBox mCheckBox;

	@SuppressLint("NewApi")
	public SingleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public SingleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public SingleView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.item_single_layout, this, true);
		iv_pay_type_logo = (ImageView) v.findViewById(R.id.iv_pay_type_logo);
		pay_type_title = (TextView) v.findViewById(R.id.pay_type_title);
		pay_recharge_title = (TextView) v.findViewById(R.id.pay_recharge_title);
		mCheckBox = (CheckBox) v.findViewById(R.id.checkbox);
	}

	@Override
	public void setChecked(boolean checked) {
		mCheckBox.setChecked(checked);

	}

	@Override
	public boolean isChecked() {
		return mCheckBox.isChecked();
	}

	@Override
	public void toggle() {
		mCheckBox.toggle();
	}

	public void setPayTypeChecked(Boolean payTypeChecked) {
		mCheckBox.setChecked(payTypeChecked);
	}

	public void setPayTitle(String payText) {
		pay_type_title.setText(payText);
	}

	public void setPayRechargeText(String rechargeText) {
		pay_recharge_title.setText(rechargeText);
	}

	public void setPayTypeLogo(int payLogoId) {
		iv_pay_type_logo.setImageResource(payLogoId);
	}

	public void setPayRechargeVisable(Boolean payTypeRechargeVisable) {
		if (payTypeRechargeVisable) {
			pay_recharge_title.setVisibility(View.VISIBLE);
		} else {
			pay_recharge_title.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {

	}

}
