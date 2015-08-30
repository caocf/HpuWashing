package com.edaixi.adapter;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.edaixi.activity.LoginActivity;
import com.edaixi.activity.R;
import com.edaixi.activity.WebActivity;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.BannerlistBean;
import com.edaixi.modle.InappUrlbean;
import com.edaixi.util.GetClassUtil;
import com.edaixi.util.SaveUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.tendcloud.tenddata.TCAgent;

public class Myfuncadpter extends BaseAdapter {

	private Viewholer holder;
	private Context mcontext;
	public ArrayList<BannerlistBean> mfunditonlist;
	private boolean isMiddle;

	public Myfuncadpter(Context context,
			ArrayList<BannerlistBean> funditonlist, boolean isMiddle) {

		super();
		this.mfunditonlist = funditonlist;
		this.mcontext = context;
		this.isMiddle = isMiddle;

	}

	@Override
	public int getCount() {
		int size;
		if (isMiddle) {
			size = mfunditonlist.size() == 0 ? 2 : mfunditonlist.size();
		} else {
			size = mfunditonlist.size() == 0 ? 3 : mfunditonlist.size();
		}
		return size;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		holder = new Viewholer();
		convertView = (RelativeLayout) LayoutInflater.from(mcontext).inflate(
				R.layout.main_grid_layout, null);
		holder.grid_img = (ImageView) convertView.findViewById(R.id.grid_img);
		if (mfunditonlist.size() == 0) {
			return convertView;
		} else {
			BitmapUtils utils = new BitmapUtils(mcontext);
			utils.display(holder.grid_img, mfunditonlist.get(position)
					.getImage_url());
			
			//----------加载在线图片，重新绘制宽高--------------
			
			
			//----------加载在线图片，重新绘制宽高--------------
			
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					TCAgent.onEvent(mcontext,
							"首页Menu_" + mfunditonlist.get(position).getTitle());
					Intent intent = new Intent();
					if (mfunditonlist.get(position) != null) {
						if (mfunditonlist.get(position).getUrl_type()
								.equals("web")) {
							intent.putExtra("bannerlistbean",
									mfunditonlist.get(position));
							intent.setClass(mcontext, WebActivity.class);
							mcontext.startActivity(intent);
						} else if (mfunditonlist.get(position).getUrl_type()
								.equals("in_app")) {
							SaveUtils saveUtils = new SaveUtils(mcontext);
							if (!saveUtils.getBoolSP(KeepingData.LOGINED)) {
								intent.setClass(mcontext, LoginActivity.class);
								mcontext.startActivity(intent);
							} else {
								Gson gson = new Gson();
								Intent intentToActivity = new Intent();
								InappUrlbean inappurlbean = gson.fromJson(
										mfunditonlist.get(position).getUrl(),
										InappUrlbean.class);
								intentToActivity.setClass(mcontext,
										GetClassUtil.getToclsss(inappurlbean));
								intentToActivity.putExtra("bannerlistbean",
										mfunditonlist.get(position));
								mcontext.startActivity(intentToActivity);
							}
						}
					}
				}

			});
		}
		return convertView;
	}
}

class Viewholer {
	public ImageView grid_img;
}
