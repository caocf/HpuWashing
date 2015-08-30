package com.edaixi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edaixi.activity.R;

/**
 * Splash页面上的普通Fragment
 */
public class NormalSplashFragment extends Fragment {

	private static final String IMG_RES_ID = "ImgResId";
	private static final String IMG_BAK_ID = "ImgBakId";

	public static Fragment newInstance(int mImgResId, int mImgbakId) {
		Fragment mFrag = new NormalSplashFragment();
		Bundle mBundle = new Bundle();
		mBundle.putInt(IMG_RES_ID, mImgResId);
		mBundle.putInt(IMG_BAK_ID, mImgbakId);
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

	public NormalSplashFragment() {
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_normal_splash,
					container, false);

			mImageView = (ImageView) mRootView
					.findViewById(R.id.fragment_normal_splash_img);
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

}
