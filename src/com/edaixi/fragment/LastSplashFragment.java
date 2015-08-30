package com.edaixi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.edaixi.activity.R;
import com.edaixi.activity.SplashActivity;

/**
 * Splash页面中最后一个Fragment，此Fragment为一个特殊的Fragment
 */
public class LastSplashFragment extends Fragment {

	private static final String IMG_RES_ID = "ImgResId";
	private static final String IMG_BAK_ID = "ImgBakId";

	public static Fragment newInstance(int mImgResId, int mImgBakID) {
		Fragment mFrag = new LastSplashFragment();
		Bundle mBundle = new Bundle();
		mBundle.putInt(IMG_BAK_ID, mImgBakID);
		mBundle.putInt(IMG_RES_ID, mImgResId);

		mFrag.setArguments(mBundle);
		return mFrag;
	}

	private int mImgResID;
	private int mImgBakID;
	private ImageView mImageView = null;

	/**
	 * 只要这个Fragment类对象没有被回收即可复用
	 */
	private View mRootView = null;

	private CommonCallBackListener mCommonListener = null;

	public LastSplashFragment() {
		mImgResID = 0;
		mImgBakID = 0;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle mBundle = getArguments();
		mImgResID = mBundle.getInt(IMG_RES_ID, 0);
		mImgBakID = mBundle.getInt(IMG_BAK_ID, 0);
		if (mImgResID == 0 || mImgBakID == 0) {
			throw new RuntimeException("the image res id is not legal!");
		}

		mCommonListener = new CommonCallBackListener();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_last_splash,
					container, false);

			mImageView = (ImageView) mRootView
					.findViewById(R.id.fragment_last_splash_img);
			mRootView.findViewById(R.id.fragment_last_splash_startuse)
					.setOnClickListener(mCommonListener);
		} else {
			ViewGroup mParentView = (ViewGroup) mRootView.getParent();
			if (mParentView != null) {
				mParentView.removeView(mRootView);
			}
		}
		mImageView.setBackgroundResource(mImgBakID);
		mImageView.setImageResource(mImgResID);
		return mRootView;
	}

	private class CommonCallBackListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.fragment_last_splash_startuse:
				((SplashActivity) getActivity()).skipDestActivity();
				break;
			default:
				break;
			}
		}

	}

}
