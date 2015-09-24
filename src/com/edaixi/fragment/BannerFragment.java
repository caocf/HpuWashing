package com.edaixi.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

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
import com.zxinsight.MWImageView;

public final class BannerFragment extends Fragment {

	private MWImageView homeBg;
	private int mPosition;
	private static String POSITION = "position";
	private ArrayList<BannerlistBean> magicWindowBeans;
	private ArrayList<BannerlistBean> bannerlist;
	private SaveUtils saveUtils;

	public static final BannerFragment newInstance(int position) {
		BannerFragment f = new BannerFragment();
		Bundle bdl = new Bundle();
		bdl.putInt(POSITION, position);
		f.setArguments(bdl);
		return f;
	}

	public static final BannerFragment newInstance(int position,
			ArrayList<BannerlistBean> magicWindowBeans,
			ArrayList<BannerlistBean> bannerlist) {
		BannerFragment f = new BannerFragment();
		Bundle bdl = new Bundle();
		bdl.putInt(POSITION, position);
		bdl.putSerializable("MagicBeanObject", magicWindowBeans);
		bdl.putSerializable("BannerBeanObject", bannerlist);
		f.setArguments(bdl);
		return f;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.item_home_banner, container, false);
		homeBg = (MWImageView) v.findViewById(R.id.homeBanner);
		Bundle bundle = getArguments();
		mPosition = bundle.getInt(POSITION);
		magicWindowBeans = (ArrayList<BannerlistBean>) bundle
				.getSerializable("MagicBeanObject");
		bannerlist = (ArrayList<BannerlistBean>) bundle
				.getSerializable("BannerBeanObject");
		BitmapUtils utils = new BitmapUtils(getActivity());
		saveUtils = new SaveUtils(getActivity());

		if (bannerlist != null && magicWindowBeans != null) {
			if (magicWindowBeans.size() > 0
					&& mPosition < magicWindowBeans.size()) {
				homeBg.bindEvent(magicWindowBeans.get(mPosition)
						.getMagicIndexKey());
			} else {
				utils.display(homeBg,
						bannerlist.get(mPosition - magicWindowBeans.size())
								.getImage_url());
				setBannerClick();
			}
		} else if (bannerlist != null && magicWindowBeans == null) {
			if (mPosition < magicWindowBeans.size()) {
				homeBg.bindEvent(magicWindowBeans.get(mPosition)
						.getMagicIndexKey());
			}
		} else if (bannerlist == null && magicWindowBeans != null) {
			utils.display(homeBg,
					bannerlist.get(mPosition - magicWindowBeans.size())
							.getImage_url());
			setBannerClick();
		}
		return v;
	}

	public void setBannerClick() {
		homeBg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TCAgent.onEvent(
						getActivity(),
						"首页Banner_"
								+ bannerlist.get(
										mPosition - magicWindowBeans.size())
										.getTitle());

				Intent intent = new Intent();
				if (bannerlist.get(mPosition - magicWindowBeans.size())
						.getUrl_type().equals("web")) {
					intent.putExtra("bannerlistbean",
							bannerlist.get(mPosition - magicWindowBeans.size()));
					intent.setClass(getActivity(), WebActivity.class);
					startActivity(intent);
				} else if (bannerlist.get(mPosition - magicWindowBeans.size())
						.getUrl_type().equals("in_app")) {
					if (!saveUtils.getBoolSP(KeepingData.LOGINED)) {
						intent.setClass(getActivity(), LoginActivity.class);
						startActivity(intent);
					} else {
						Intent intentToActivity = new Intent();
						Gson gson = new Gson();
						InappUrlbean inappurlbean = gson.fromJson(bannerlist
								.get(mPosition - magicWindowBeans.size())
								.getUrl(), InappUrlbean.class);
						intentToActivity.setClass(getActivity(),
								GetClassUtil.getToclsss(inappurlbean));
						intentToActivity.putExtra("orderId",
								inappurlbean.getId());
						startActivity(intentToActivity);
					}
				}
			}
		});
	}
}
