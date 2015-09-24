package com.edaixi.wechat;

import java.io.InputStream;
import java.net.URL;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;
import com.edaixi.activity.R;
import com.edaixi.util.Constants;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * a util for wechat share ....
 * 
 * @author wei-spring
 */
public class WX_Share {
	private IWXAPI api;
	private Context context;
	private int flag;
	private String shareUrl;
	private String Share_title;
	private String Share_content;
	private String Share_image_url;

	public WX_Share(Context context, String shareUrl, String Share_title,
			String Share_content, String Share_image_url, int flag) {
		super();
		this.context = context;
		this.flag = flag;
		this.shareUrl = shareUrl;
		this.Share_title = Share_title;
		this.Share_content = Share_content;
		this.Share_image_url = Share_image_url;
		registWX();
	}

	public void registWX() {
		api = WXAPIFactory.createWXAPI(context, Constants.WXAPP_ID, true);
		api.registerApp(Constants.WXAPP_ID);
	}

	public void share2WX() {
		if (!api.isWXAppInstalled()) {
			Toast.makeText(context, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
			return;
		}
		WXWebpageObject webpage = new WXWebpageObject();
		if (shareUrl != "") {
			webpage.webpageUrl = shareUrl;
		} else {
			webpage.webpageUrl = "http://apk.edaixi.com";
		}
		final WXMediaMessage msg = new WXMediaMessage(webpage);

		if (Share_title != "") {
			msg.title = Share_title;
		} else {
			msg.title = "洗衣就用e袋洗，送你20元代金券";
		}
		if (Share_content != "") {
			msg.description = Share_content;
		} else {
			msg.description = "推荐一款洗衣神器—e袋洗！洗衣好便宜，夏季洗衣9元起！新用户下单立减20元，上门服务！洗衣就用e袋洗！(APP下载链接)";
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				Bitmap thumb;
				if (Share_image_url != "") {
					try {
						thumb = BitmapFactory
								.decodeStream((InputStream) new URL(
										Share_image_url).getContent());
					} catch (Exception e) {
						thumb = BitmapFactory.decodeResource(
								context.getResources(),
								R.drawable.wx_share_logo);
						e.printStackTrace();
					}
					msg.setThumbImage(thumb);
				} else {
					thumb = BitmapFactory.decodeResource(
							context.getResources(), R.drawable.ic_launcher);
					msg.setThumbImage(thumb);
				}
			}
		}).start();

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = flag;
		api.sendReq(req);
	}

}
