package com.viewpagerindicator;


import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class CustomDurationScroller extends Scroller
{
  private double scrollFactor = 5.0D;

  public CustomDurationScroller(Context paramContext)
  {
    super(paramContext);
  }

  public CustomDurationScroller(Context paramContext, Interpolator paramInterpolator)
  {
    super(paramContext, paramInterpolator);
  }

  public void setScrollDurationFactor(double paramDouble)
  {
    this.scrollFactor = paramDouble;
  }

  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    super.startScroll(paramInt1, paramInt2, paramInt3, paramInt4, (int)(paramInt5 * this.scrollFactor));
  }
}
