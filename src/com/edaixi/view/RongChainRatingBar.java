package com.edaixi.view;

import com.edaixi.activity.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RongChainRatingBar extends RelativeLayout implements
		OnClickListener {

	TextView titleText;
	ImageView iv_1;
	ImageView iv_2;
	ImageView iv_3;
	ImageView iv_4;
	ImageView iv_5;
	Context context;
	int currentNum = 0;
	boolean isFace = false;
	OnFaceIndexListener yourFaceListener;

	public RongChainRatingBar(Context context) {
		super(context);
	}

	public RongChainRatingBar(Context context, boolean isFace) {
		super(context);
		this.isFace = isFace;
	}

	public RongChainRatingBar(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public RongChainRatingBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.rongchain_custom_rating,
				this);
		titleText = (TextView) findViewById(R.id.title_text);
		iv_1 = (ImageView) findViewById(R.id.iv_1);
		iv_1.setOnClickListener(this);
		iv_2 = (ImageView) findViewById(R.id.iv_2);
		iv_2.setOnClickListener(this);
		iv_3 = (ImageView) findViewById(R.id.iv_3);
		iv_3.setOnClickListener(this);
		iv_4 = (ImageView) findViewById(R.id.iv_4);
		iv_4.setOnClickListener(this);
		iv_5 = (ImageView) findViewById(R.id.iv_5);
		iv_5.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_1:
			setFaceImageSource(1);
			break;
		case R.id.iv_2:
			setFaceImageSource(2);
			break;
		case R.id.iv_3:
			setFaceImageSource(3);
			break;
		case R.id.iv_4:
			setFaceImageSource(4);
			break;
		case R.id.iv_5:
			setFaceImageSource(5);
			break;
		}
	}

	// 根据标志索引设置表情背景
	public void setFaceImageSource(int indexFace) {
		if (!isFace) {
			yourFaceListener.getFaceIndex(indexFace);
		}
		switch (indexFace) {
		case 1:
			currentNum = 1;
			if (isFace) {
				iv_1.setImageResource(R.drawable.icon_face01_pitchon);
				iv_2.setImageResource(R.drawable.icon_face02_pitchoff);
				iv_3.setImageResource(R.drawable.icon_face03_pitchoff);
				iv_4.setImageResource(R.drawable.icon_face04_pitchoff);
				iv_5.setImageResource(R.drawable.icon_face05_pitchoff);
			} else {
				iv_1.setImageResource(R.drawable.stare_press);
				iv_2.setImageResource(R.drawable.stare_default);
				iv_3.setImageResource(R.drawable.stare_default);
				iv_4.setImageResource(R.drawable.stare_default);
				iv_5.setImageResource(R.drawable.stare_default);
			}
			break;
		case 2:
			currentNum = 2;
			if (isFace) {
				iv_1.setImageResource(R.drawable.icon_face02_pitchon);
				iv_2.setImageResource(R.drawable.icon_face02_pitchon);
				iv_3.setImageResource(R.drawable.icon_face03_pitchoff);
				iv_4.setImageResource(R.drawable.icon_face04_pitchoff);
				iv_5.setImageResource(R.drawable.icon_face05_pitchoff);
			} else {
				iv_1.setImageResource(R.drawable.stare_press);
				iv_2.setImageResource(R.drawable.stare_press);
				iv_3.setImageResource(R.drawable.stare_default);
				iv_4.setImageResource(R.drawable.stare_default);
				iv_5.setImageResource(R.drawable.stare_default);
			}
			break;
		case 3:
			currentNum = 3;
			if (isFace) {
				iv_1.setImageResource(R.drawable.icon_face03_picthon);
				iv_2.setImageResource(R.drawable.icon_face03_picthon);
				iv_3.setImageResource(R.drawable.icon_face03_picthon);
				iv_4.setImageResource(R.drawable.icon_face04_pitchoff);
				iv_5.setImageResource(R.drawable.icon_face05_pitchoff);
			} else {
				iv_1.setImageResource(R.drawable.stare_press);
				iv_2.setImageResource(R.drawable.stare_press);
				iv_3.setImageResource(R.drawable.stare_press);
				iv_4.setImageResource(R.drawable.stare_default);
				iv_5.setImageResource(R.drawable.stare_default);
			}
			break;
		case 4:
			currentNum = 4;
			if (isFace) {
				iv_1.setImageResource(R.drawable.icon_face04_pitchon);
				iv_2.setImageResource(R.drawable.icon_face04_pitchon);
				iv_3.setImageResource(R.drawable.icon_face04_pitchon);
				iv_4.setImageResource(R.drawable.icon_face04_pitchon);
				iv_5.setImageResource(R.drawable.icon_face05_pitchoff);
			} else {
				iv_1.setImageResource(R.drawable.stare_press);
				iv_2.setImageResource(R.drawable.stare_press);
				iv_3.setImageResource(R.drawable.stare_press);
				iv_4.setImageResource(R.drawable.stare_press);
				iv_5.setImageResource(R.drawable.stare_default);
			}
			break;
		case 5:
			currentNum = 5;
			if (isFace) {
				iv_1.setImageResource(R.drawable.icon_face05_pitchon);
				iv_2.setImageResource(R.drawable.icon_face05_pitchon);
				iv_3.setImageResource(R.drawable.icon_face05_pitchon);
				iv_4.setImageResource(R.drawable.icon_face05_pitchon);
				iv_5.setImageResource(R.drawable.icon_face05_pitchon);
			} else {
				iv_1.setImageResource(R.drawable.stare_press);
				iv_2.setImageResource(R.drawable.stare_press);
				iv_3.setImageResource(R.drawable.stare_press);
				iv_4.setImageResource(R.drawable.stare_press);
				iv_5.setImageResource(R.drawable.stare_press);
			}
			break;

		default:
			break;
		}
	}

	// 初始化表情为5星默认
	public void selectAllFaceDefault() {
		currentNum = 0;
		if (isFace) {
			iv_1.setImageResource(R.drawable.icon_face01_pitchoff);
			iv_2.setImageResource(R.drawable.icon_face02_pitchoff);
			iv_3.setImageResource(R.drawable.icon_face03_pitchoff);
			iv_4.setImageResource(R.drawable.icon_face04_pitchoff);
			iv_5.setImageResource(R.drawable.icon_face05_pitchoff);
		} else {
			iv_1.setImageResource(R.drawable.stare_default);
			iv_2.setImageResource(R.drawable.stare_default);
			iv_3.setImageResource(R.drawable.stare_default);
			iv_4.setImageResource(R.drawable.stare_default);
			iv_5.setImageResource(R.drawable.stare_default);
		}
	}

	// 初始化表情为5星
	public void selectAllFace() {
		currentNum = 5;
		if (isFace) {

			iv_1.setImageResource(R.drawable.icon_face01_pitchon);
			iv_2.setImageResource(R.drawable.icon_face05_pitchon);
			iv_3.setImageResource(R.drawable.icon_face05_pitchon);
			iv_4.setImageResource(R.drawable.icon_face05_pitchon);
			iv_5.setImageResource(R.drawable.icon_face05_pitchon);
		} else {
			iv_1.setImageResource(R.drawable.stare_press);
			iv_2.setImageResource(R.drawable.stare_press);
			iv_3.setImageResource(R.drawable.stare_press);
			iv_4.setImageResource(R.drawable.stare_press);
			iv_5.setImageResource(R.drawable.stare_press);
		}
	}

	// 设置星星的类型
	public void setRatingType(Boolean isFace) {
		this.isFace = isFace;
	}

	// 设置评价表情的标题
	public void setRatingTitle(String ratingTitle) {
		titleText.setText(ratingTitle);
	}

	// 获取当前表情的索引
	public int getFaceIndex() {
		return currentNum;
	}

	// 获取当前表情的索引
	public void setOffClickable() {
		iv_1.setClickable(false);
		iv_2.setClickable(false);
		iv_3.setClickable(false);
		iv_4.setClickable(false);
		iv_5.setClickable(false);
	}

	public interface OnFaceIndexListener {

		public void getFaceIndex(int faceIndex);

	}

	// 设置回调接口(监听器)的方法
	public void setYourListener(OnFaceIndexListener yourFaceListener) {
		this.yourFaceListener = yourFaceListener;
	}
}
