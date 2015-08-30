package com.edaixi.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.edaixi.activity.R;
import com.edaixi.wechat.WX_Share;

/**
 * @author wei-spring custom dialog ,when you need,below demo is your want
 * 
 */
public class SharePacketsDialog extends Dialog implements OnClickListener {
	int layoutRes;
	private Context context;
	private TextView share_tv_title;
	private TextView share_tv_cancle;
	private TextView share_wechat_text;
	private TextView share_friends_text;
	private ImageView share_wechat_logo;
	private ImageView share_friends_logo;
	private String share_coupon_total;
	private String share_order_url;
	private String Share_title;
	private String Share_content;
	private String Share_image_url;
	private boolean shareApp = false;

	private SharePacketsDialog(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * self define layout
	 * 
	 * @param context
	 * @param resLayout
	 */
	public SharePacketsDialog(Context context, int resLayout) {
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
	public SharePacketsDialog(Context context, int theme, int resLayout,
			String share_coupon_total, String share_order_url,
			String Share_title, String Share_content, String Share_image_url) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
		this.share_coupon_total = share_coupon_total;
		this.share_order_url = share_order_url;
		this.Share_title = Share_title;
		this.Share_content = Share_content;
		this.Share_image_url = Share_image_url;
	}

	/**
	 * self define layout theme
	 * 
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public SharePacketsDialog(Context context, int theme, int resLayout,
			boolean shareApp) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
		this.shareApp = shareApp;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layoutRes);
		share_tv_title = (TextView) findViewById(R.id.share_tv_title);
		if (shareApp) {
			share_tv_title.setText("分享给好友");
		} else {
			share_tv_title.setText("恭喜您替朋友获得" + share_coupon_total
					+ "张优惠券， \n 分享您也能得1张");
		}
		share_tv_cancle = (TextView) findViewById(R.id.share_tv_cancle);
		share_tv_cancle.setOnClickListener(this);
		share_wechat_text = (TextView) findViewById(R.id.share_wechat_text);
		share_wechat_text.setOnClickListener(this);
		share_friends_text = (TextView) findViewById(R.id.share_friends_text);
		share_friends_text.setOnClickListener(this);
		share_wechat_logo = (ImageView) findViewById(R.id.share_wechat_logo);
		share_wechat_logo.setOnClickListener(this);
		share_friends_logo = (ImageView) findViewById(R.id.share_friends_logo);
		share_friends_logo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_tv_cancle:
			this.cancel();
			break;
		case R.id.share_wechat_logo:
		case R.id.share_wechat_text:
			if (shareApp) {
				WX_Share wechat_Share = new WX_Share(context, "", "", "", "", 0);
				wechat_Share.share2WX();
			} else {
				WX_Share wechat_Share = new WX_Share(context, share_order_url,
						Share_title, Share_content, Share_image_url, 0);
				wechat_Share.share2WX();
			}
			this.cancel();
			break;
		case R.id.share_friends_logo:
		case R.id.share_friends_text:
			if (shareApp) {
				WX_Share wx_friends_Share = new WX_Share(context, "", "", "",
						"", 1);
				wx_friends_Share.share2WX();
			} else {

				WX_Share wx_friends_Share = new WX_Share(context,
						share_order_url, Share_title, Share_content,
						Share_image_url, 1);
				wx_friends_Share.share2WX();
			}
			this.cancel();
			break;

		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}
}