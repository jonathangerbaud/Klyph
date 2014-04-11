package com.abewy.android.apps.klyph.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

public abstract class SlidingPanelLayout extends ScrollableViewGroup
{
  protected boolean  mOpen;
  protected View mPanel;
  private Drawable mShadow;
  private int mShadowWidth;

  public SlidingPanelLayout(Context paramContext)
  {
    super(paramContext);
    setBackgroundColor(0);
    setScrollEnabled(true);
    setVertical(false);
  }

  public SlidingPanelLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setBackgroundColor(0);
    setScrollEnabled(true);
    setVertical(false);
  }

  public SlidingPanelLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    setBackgroundColor(0);
    setScrollEnabled(true);
    setVertical(false);
  }

  protected void dispatchDraw(Canvas paramCanvas)
  {
    super.dispatchDraw(paramCanvas);
    if ((this.mOpen) && (getScrollX() < 0))
    {
      //this.mShadow.setBounds(-this.mShadowWidth, 0, 0, getHeight());
      //this.mShadow.draw(paramCanvas);
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    if (getChildCount() != 1)
      throw new IllegalStateException(getClass().getName() + " should have exactly one child");
    this.mPanel = getChildAt(0);
    Resources localResources = getResources();
    //this.mShadowWidth = localResources.getDimensionPixelSize(R.dimen.host_shadow_width);
    //this.mShadow = localResources.getDrawable(R.drawable.navigation_shadow);
  }

  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(localSavedState.getSuperState());
    setOpen(localSavedState.open);
    setScrollEnabled(this.mOpen);
  }

  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    localSavedState.open = this.mOpen;
    return localSavedState;
  }

  public void setOpen(boolean  paramboolean)
  {
    this.mOpen = paramboolean;
  }

  static class SavedState extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>()
    {
    	public SavedState createFromParcel(Parcel in)
		{
			return new SavedState(in);
		}

		public SavedState[] newArray(int size)
		{
			return new SavedState[size];
		}
    };
    boolean  open;

    private SavedState(Parcel paramParcel)
    {
      super(paramParcel);
      if (paramParcel.readInt() != 0);
      for (boolean  bool = true; ; bool = false)
      {
        this.open = bool;
        return;
      }
    }

    SavedState(Parcelable paramParcelable)
    {
      super(paramParcelable);
    }

    public String toString()
    {
      String str = Integer.toHexString(System.identityHashCode(this));
      return "SlidingPanelLayout.SavedState{" + str + " open=" + this.open + "}";
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      if (this.open);
      for (int i = 1; ; i = 0)
      {
        paramParcel.writeInt(i);
        return;
      }
    }
  }
}