package com.edaixi.fragment;

import java.util.HashMap;
import com.edaixi.util.MyhttpUtils;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {
	protected int mTitleId;
	private Context context;
	private MyhttpUtils utils;

	public BaseFragment() {
		super();
		utils = new MyhttpUtils(getContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	protected Context getContext() {
		return context;
	}

	protected void init(FragmentActivity fragmentActivity) {
		context = fragmentActivity;
	};

	public void dismiss() {
		getFragmentManager().popBackStack();
	}

	protected void cancelLoad() {

	}

	public void onLoadMore() {
	}

	public void onRefresh() {
	}

	/*
	 * 获取数据
	 */
	public void getdate(Context context, HashMap<String, String> params,
			String path, Handler handler, int TUREMESSAGER, int ERRORMESSAGER,
			boolean isshow, boolean iscache) {
		String url = utils.getUrl(params, path, context);
		utils.getdate(getContext(), handler, TUREMESSAGER, ERRORMESSAGER,
				params, url, isshow, iscache);
	}

	/*
	 * 获取数据
	 */
	public void postdate(Context context, HashMap<String, String> params,
			String path, Handler handler, int TUREMESSAGER, int ERRORMESSAGER,
			boolean isshow) {
		String url = utils.getUrl(params, path, context);
		utils.postdate(context, handler, TUREMESSAGER, ERRORMESSAGER, params,
				url, isshow);
	}

}
