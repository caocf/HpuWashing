package com.edaixi.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import com.edaixi.activity.R;

/**
 * custom dialog ,when you need,below demo is your want
 * 
 * CustomDialog dialog=new CustomDialog(content, R.style.customdialog_style,
 * R.layout.customdialog,"这是首页测试dialog"); dialog.show();
 * 
 * @author wei-spring
 * 
 */
public class CustomDialog extends Dialog {

	private Context context = null;
	private static CustomDialog customProgressDialog = null;

	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public static CustomDialog createProgressDialog(Context context) {
		customProgressDialog = new CustomDialog(context,
				R.style.customdialog_style);
		customProgressDialog.setContentView(R.layout.customdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (customProgressDialog == null) {
			return;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2000);
					if (customProgressDialog != null) {
						customProgressDialog.cancel();
						customProgressDialog = null;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	public CustomDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog
				.findViewById(R.id.tv_customdialog_text);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		return customProgressDialog;
	}
}