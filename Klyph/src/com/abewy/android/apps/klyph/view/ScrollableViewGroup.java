package com.abewy.android.apps.klyph.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.R.id;

public abstract class ScrollableViewGroup extends ViewGroup
{
  private static final Interpolator sInterpolator = new Interpolator()
  {
    public final float getInterpolation(float paramAnonymousFloat)
    {
      float f = paramAnonymousFloat - 1.0F;
      return 1.0F + f * (f * (f * (f * f)));
    }
  };
  private float mFlingVelocity = 0.0F;
  private boolean  mFlingable = true;
  private boolean  mIsBeingDragged = false;
  private float[] mLastPosition = { 0.0F, 0.0F };
  private final int[] mLimits = { -2147483647, 2147483647 };
  private int mMaximumVelocity;
  private int mMinimumVelocity;
  private OnScrollChangedListener mOnScrollChangeListener;
  private boolean  mReceivedDown = false;
  private int mScrollDirection = 0;
  private boolean  mScrollEnabled = true;
  protected Scroller mScroller;
  private int mTouchSlop;
  private VelocityTracker mVelocityTracker;
  private boolean  mVertical = true;

  public ScrollableViewGroup(Context paramContext)
  {
    super(paramContext);
    Context localContext = getContext();
    setFocusable(false);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
    this.mTouchSlop = localViewConfiguration.getScaledTouchSlop();
    this.mMinimumVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
    this.mMaximumVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.mScroller = new Scroller(localContext, sInterpolator);
  }

  public ScrollableViewGroup(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Context localContext = getContext();
    setFocusable(false);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
    this.mTouchSlop = localViewConfiguration.getScaledTouchSlop();
    this.mMinimumVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
    this.mMaximumVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.mScroller = new Scroller(localContext, sInterpolator);
  }

  public ScrollableViewGroup(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    Context localContext = getContext();
    setFocusable(false);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
    this.mTouchSlop = localViewConfiguration.getScaledTouchSlop();
    this.mMinimumVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
    this.mMaximumVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.mScroller = new Scroller(localContext, sInterpolator);
  }

  private int clampToScrollLimits(int paramInt)
  {
    if (paramInt < this.mLimits[0])
      paramInt = this.mLimits[0];
    while (paramInt <= this.mLimits[1])
      return paramInt;
    return this.mLimits[1];
  }

  private boolean  shouldStartDrag(MotionEvent motionevent)
  {
      boolean  flag = false;
		
      if (mScrollEnabled)
      {

          if (!mIsBeingDragged)
          {
              return false;
          }
				
          mIsBeingDragged = false;
      }
		
		switch (motionevent.getAction())
		{
			case 0:
			{
				return flag;
			}
			case 1:
			{
				updatePosition(motionevent);
				if (!mScroller.isFinished())
				{
					startDrag();
					flag = true;
				} 
				else
				{
					mReceivedDown = true;
				}
				
				return flag;
			}
			case 2:
			{
				float f2;
				float f5;
				float f6;
				float f = motionevent.getX();
				float f1 = mLastPosition[0];
				f2 = f - f1;
				float f3 = motionevent.getY();
				float f4 = mLastPosition[1];
				f5 = f3 - f4;
				f6 = mTouchSlop;
				
				boolean  flag1 = false;
				
				if (f2 > f6 || f2 < -mTouchSlop)
				{
					flag1 = true;
				}
				
				boolean  flag2 = false;
				if (f5 > mTouchSlop || f5 < -mTouchSlop)
				{
					flag2 = true;
				}
				
				if (mVertical)
				{
					if (flag2 && !flag1)
					{
						flag1 = true;
					} else
					{
						flag1 = false;
					}
				} 
				else if (flag1 && !flag2)
				{
					flag1 = true;
				} 
				else
				{
					flag1 = false;
				}
				
				if (flag1)
				{
					updatePosition(motionevent);
					startDrag();
					flag = true;
				}
				
				return flag;
			}
		}
		
		return flag;
  }

  private void startDrag()
  {
    this.mIsBeingDragged = true;
    this.mFlingVelocity = 0.0F;
    this.mScrollDirection = 0;
    this.mScroller.abortAnimation();
  }

  public void addView(View paramView)
  {
    View localView = paramView.findViewById(R.id.list_layout_parent);
    if (localView != null)
    {
      int i = ((Integer)localView.getTag()).intValue();
      int j = getChildCount();
      for (int k = 0; ; k++)
      {
        int m = 0;
        if (k < j)
        {
          if (((Integer)getChildAt(k).findViewById(R.id.list_layout_parent).getTag()).intValue() > i)
          {
            addView(paramView, k);
            m = 1;
          }
        }
        else
        {
          if (m == 0)
            super.addView(paramView);
          return;
        }
      }
    }
    super.addView(paramView);
  }

  public void computeScroll()
  {
	  if (mScroller.computeScrollOffset())
      {
          int i;
          int j;
          if (mVertical)
          {
              i = mScroller.getCurrY();
          }
          else
          {
              i = mScroller.getCurrX();
          }
          
          scrollTo(i);
          invalidate();
          
          if (mVertical)
          {
              j = mScroller.getFinalY();
          } 
          else
          {
              j = mScroller.getFinalX();
          }
          
          if (i == j)
          {
              mScroller.abortAnimation();
          }
          
          if (mFlingVelocity != 0F)
          {
              int k;
              if (mFlingVelocity > 0F)
              {
                  k = 1;
              } 
              else
              {
                  k = -1;
              }
              
              mFlingVelocity = 0F;
              onScrollFinished(k);
          }
      }
  }

  public final int getScroll()
  {
    if (this.mVertical)
      return getScrollY();
    return getScrollX();
  }

  public boolean  onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    return shouldStartDrag(paramMotionEvent);
  }

  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (this.mOnScrollChangeListener != null)
      this.mOnScrollChangeListener.onVerticalScrollChanged(paramInt2);
  }

  protected void onScrollFinished(int paramInt)
  {
  }

  public boolean  onTouchEvent(MotionEvent motionevent)
  {
      int i;
      float f = 0;
      i = motionevent.getAction();
      if (mFlingable)
      {
          if (mVelocityTracker == null)
          {
              VelocityTracker velocitytracker = VelocityTracker.obtain();
              mVelocityTracker = velocitytracker;
          }
          mVelocityTracker.addMovement(motionevent);
      }
		
      if (mIsBeingDragged)
		{
			switch (i)
			{
				case 1:
				{
					return true;
				}
				case 2:
				{
					VelocityTracker velocitytracker1;
					if (i == 3)
					{
						f = 1.401298E-45F;
					} else
					{
						f = 0F;
					}
					mIsBeingDragged = false;
					if (f != 0 || !mFlingable || getChildCount() <= 0)
					{
						int j3 = mScrollDirection;
						onScrollFinished(j3);
						if (mFlingable && mVelocityTracker != null)
						{
							mVelocityTracker.recycle();
							mVelocityTracker = null;
						}
						
						mReceivedDown = false;
					}
					else 
					{
						velocitytracker1 = mVelocityTracker;
						velocitytracker1.computeCurrentVelocity(1000, mScrollDirection);
						
						if (mVertical)
						{
							f = mVelocityTracker.getYVelocity();
						} else
						{
							f = mVelocityTracker.getXVelocity();
						}
						
						if (f > mMinimumVelocity || f >= -mMinimumVelocity)
						{
							float f7 = -f;
							mFlingVelocity = f7;
							if (mVertical)
							{
								mScroller.fling(getScrollX(), getScrollY(), 0, (int) -f, 0, 0, mLimits[0], mLimits[1]);
							} 
							else
							{
								mScroller.fling(getScrollX(), getScrollY(), (int) -f, 0, mLimits[0], mLimits[1], 0, 0);
							}
							invalidate();
						}
						else
						{
							onScrollFinished(mScrollDirection);
							if (mFlingable && mVelocityTracker != null)
							{
								mVelocityTracker.recycle();
								mVelocityTracker = null;
							}
							
							mReceivedDown = false;
						}
					}
					return true;
				}
				case 3:
				{
					float f1 = 0;
					
					if (mVertical)
					{
						f = 1;
					}
					
					f1 = mLastPosition[(int)f];
					updatePosition(motionevent);
					
					float f2 = mLastPosition[(int)f];
					float f3 = f1 - f2;
					
					if (f3 < -1F)
					{
						mScrollDirection = -1;
					} 
					else if (f3 > 1F)
					{
						mScrollDirection = 1;
					}
					
					int k = (int)f3;
					scrollTo(getScroll() + k);
					return true;
				}
			}
			
		}
		else
		{
			boolean  b = false;
			if (shouldStartDrag(motionevent))
			{
				b = true;
			} 
			else if (i == 1 && mReceivedDown)
			{
				mReceivedDown = false;
				b = performClick();
			} else
			{
				b = true;
			}
			
			return b;
		}
      
      return false;
  }

  public final void reset(int paramInt)
  {
    int i = clampToScrollLimits(0) - getScroll();
    if (this.mVertical)
    {
      mScroller.startScroll(0, getScrollY(), 0, i, 0);
    }
    else
    {
    	mScroller.startScroll(getScrollX(), 0, i, 0, 0);
    }
    
      invalidate();
  }

  protected final void scrollTo(int paramInt)
  {
    if (this.mVertical)
    {
      scrollTo(0, clampToScrollLimits(paramInt));
      return;
    }
    scrollTo(clampToScrollLimits(paramInt), 0);
  }

  public void setFlingable(boolean  paramboolean)
  {
    this.mFlingable = paramboolean;
  }

  public void setOnScrollChangedListener(OnScrollChangedListener paramOnScrollChangedListener)
  {
    this.mOnScrollChangeListener = paramOnScrollChangedListener;
  }

  public void setScrollEnabled(boolean  paramboolean)
  {
    this.mScrollEnabled = paramboolean;
  }

  public void setScrollLimits(int paramInt1, int paramInt2)
  {
    this.mLimits[0] = paramInt1;
    this.mLimits[1] = paramInt2;
  }

  public void setVertical(boolean  paramboolean)
  {
    this.mVertical = paramboolean;
  }

  public boolean  showContextMenuForChild(View paramView)
  {
    requestDisallowInterceptTouchEvent(true);
    return super.showContextMenuForChild(paramView);
  }

  public final void smoothScrollTo(int paramInt)
  {
    smoothScrollTo(paramInt, 500);
  }

  public void smoothScrollTo(int paramInt1, int paramInt2)
  {
    int i = clampToScrollLimits(paramInt1) - getScroll();
    if (this.mVertical)
    {
    	mScroller.startScroll(0, getScrollY(), 0, i, paramInt2);
    }
    else
    {
    	mScroller.startScroll(getScrollX(), 0, i, 0, paramInt2);
    }
    
    invalidate();
  }

  protected final void updatePosition(MotionEvent paramMotionEvent)
  {
    this.mLastPosition[0] = paramMotionEvent.getX();
    this.mLastPosition[1] = paramMotionEvent.getY();
  }

  public static abstract interface OnScrollChangedListener
  {
    public abstract void onVerticalScrollChanged(int paramInt);
  }
}