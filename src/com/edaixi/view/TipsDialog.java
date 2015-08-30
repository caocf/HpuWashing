package com.edaixi.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.edaixi.activity.R;

/**
 * @author wei-spring custom dialog ,when you need,below demo is your want
 * 
 */
public class TipsDialog extends Dialog implements OnClickListener {
	int layoutRes;
	@SuppressWarnings("unused")
	private Context context;
	private TextView tips_btn;

	private TipsDialog(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * self define layout
	 * 
	 * @param context
	 * @param resLayout
	 */
	public TipsDialog(Context context, int resLayout) {
		super(context);
		this.context = context;
		this.layoutRes = resLayout;
	}

	public TipsDialog(Context context, int theme, int resLayout) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layoutRes);
		tips_btn = (TextView) findViewById(R.id.tips_btn);
		tips_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tips_btn:
			this.cancel();
			break;
		}
	}
}