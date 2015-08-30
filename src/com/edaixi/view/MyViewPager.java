package com.edaixi.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager implements OnGestureListener {

	private boolean scrollble = true;

	/** 手势滑动处理类 **/
	private GestureDetector mDetector;
	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();

	OnSingleTouchListener onSingleTouchListener;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		GestureDetector detector = new GestureDetector(context, this);
		mDetector = detector;
	}

	public GestureDetector getGestureDetector() {
		return mDetector;
	}

	public boolean isScrollble() {
		return scrollble;
	}

	public void setScrollble(boolean scrollble) {
		this.scrollble = scrollble;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (!scrollble) {
			return true;
		}
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (!scrollble) {
			return true;
		}
		curP.x = arg0.getX();
		curP.y = arg0.getY();
		getParent().requestDisallowInterceptTouchEvent(true);
		if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
			downP.x = arg0.getX();
			downP.y = arg0.getY();
		}

		if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		if (arg0.getAction() == MotionEvent.ACTION_UP) {
			// 在up时判断是否按下和松手的坐标为一个点
			// 如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
			if ((downP.x == curP.x) && (downP.y == curP.y)) {
				onSingleTouch();
				return true;
			}
			// if ((downP.x - curP.x) < 3 && (downP.y - curP.y) < 3) {
			// onSingleTouch();
			// return true;
			// }
		}

		return super.onTouchEvent(arg0);
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		super.setOnPageChangeListener(listener);
	}

	/**
	 * 单击 　　
	 */
	public void onSingleTouch() {
		if (onSingleTouchListener != null) {
			onSingleTouchListener.onSingleTouch();
		}
	}

	/**
	 * 　　 * 创建点击事件接口 　　 * 　　
	 */
	public interface OnSingleTouchListener {
		public void onSingleTouch();
	}

	public void setOnSingleTouchListener(
			OnSingleTouchListener onSingleTouchListener) {
		this.onSingleTouchListener = onSingleTouchListener;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

}
