package com.edaixi.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.edaixi.activity.R;

public class RongchainProgressDialog extends Dialog {

	private Context context = null;
	private static RongchainProgressDialog customProgressDialog = null;

	public RongchainProgressDialog(Context context) {
		super(context);
	}

	public RongchainProgressDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public RongchainProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static RongchainProgressDialog createProgressDialog(Context context) {
		customProgressDialog = new RongchainProgressDialog(context,
				R.style.rongchain_progress_dialog);
		customProgressDialog.setContentView(R.layout.rongchain_progress_dialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (customProgressDialog == null) {
			return;
		}

		RelativeLayout rl_rongchain_progress_dialog = (RelativeLayout) customProgressDialog
				.findViewById(R.id.rl_rongchain_progress_dialog);
		rl_rongchain_progress_dialog.getBackground().setAlpha(125);
		ImageView imageView = (ImageView) customProgressDialog
				.findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		animationDrawable.start();
	}

	public RongchainProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog
				.findViewById(R.id.id_tv_loadingmsg);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		return customProgressDialog;
	}

	@Override
	public void show() {
		super.show();
		this.setCanceledOnTouchOutside(false);
	}
}
