package com.abewy.android.apps.klyph.view;

import com.abewy.android.apps.klyph.R;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class DismissibleListItemView extends SlidingPanelLayout
{
	private final int				mDismissScrollDeltaThreshold;
	private int						mDismissWidthThreshold;
	private boolean 					mDismissed							= false;
	private boolean 					mDismissible;
	private TransalatableTextView	mLeftBackgroundView;
	private ListItemActionListener	mListItemActionListener;
	private int						mRead								= 0;
	private TransalatableTextView	mRightBackgroundView;
	private int						mScrollDelta;
	private Runnable				mScrollToInitialPositionRunnable	= new Runnable() {
																			public final void run()
																			{
																				DismissibleListItemView.this.smoothScrollTo(0);
																			}
																		};
	private Runnable				mScrollToLeftRunnable				= new Runnable() {
																			public final void run()
																			{
																				DismissibleListItemView.this
																						.smoothScrollTo(DismissibleListItemView.this.mWidth);
																				DismissibleListItemView.this.dismiss();
																			}
																		};
	private Runnable				mScrollToRightRunnable				= new Runnable() {
																			public final void run()
																			{
																				DismissibleListItemView.this
																						.smoothScrollTo(-DismissibleListItemView.this.mWidth);
																				DismissibleListItemView.this.dismiss();
																			}
																		};
	private int						mWidth;

	public DismissibleListItemView(Context paramContext)
	{
		this(paramContext, null);
	}

	public DismissibleListItemView(Context paramContext, AttributeSet paramAttributeSet)
	{
		this(paramContext, paramAttributeSet, 0);
	}

	public DismissibleListItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
	{
		super(paramContext, paramAttributeSet, paramInt);
		setVertical(false);
		setScrollEnabled(true);
		this.mDismissScrollDeltaThreshold = paramContext.getResources().getDimensionPixelSize(R.dimen.notification_dismiss_scroll_delta_threshold);
	}

	private void onSwipeComplete(int paramInt)
	{
		if ((paramInt < -this.mDismissWidthThreshold)
			|| ((paramInt < 0) && (this.mScrollDelta < 0) && (-this.mScrollDelta > this.mDismissScrollDeltaThreshold)))
		{
			post(this.mScrollToRightRunnable);
			return;
		}
		if ((paramInt > this.mDismissWidthThreshold)
			|| ((paramInt > 0) && (this.mScrollDelta > 0) && (this.mScrollDelta > this.mDismissScrollDeltaThreshold)))
		{
			post(this.mScrollToLeftRunnable);
			return;
		}
		post(this.mScrollToInitialPositionRunnable);
	}

	private static void updateBackgroundView(TransalatableTextView transalatabletextview, int i, boolean  flag)
	{
		if (flag)
		{
			i = -i;
		}
		if (transalatabletextview != null)
		{
			if (i > 0)
			{
				transalatabletextview.setVisibility(0);
				int j = transalatabletextview.getMeasuredWidth();
				float f;
				if (i >= j)
				{
					f = 0F;
				}
				else
				{
					float f1 = i;
					float f2 = j;
					f = f1 - f2;
				}
				if (!flag)
				{
					f = -f;
				}
				transalatabletextview.setTranslate(f, 0F);
			}
			else
			{
				transalatabletextview.setVisibility(8);
			}
		}
	}

	public final void dismiss()
	{
		if (!this.mDismissed)
		{
			this.mDismissed = true;
			if (this.mListItemActionListener != null)
				this.mListItemActionListener.onDismiss();
		}
	}

	public final void init(TransalatableTextView paramTransalatableTextView1, TransalatableTextView paramTransalatableTextView2, boolean  paramboolean)
	{
		this.mDismissible = paramboolean;
		this.mDismissed = false;
		this.mLeftBackgroundView = paramTransalatableTextView1;
		this.mRightBackgroundView = paramTransalatableTextView2;
		if (this.mLeftBackgroundView != null)
			this.mLeftBackgroundView.clearAnimation();
		if (this.mRightBackgroundView != null)
			this.mRightBackgroundView.clearAnimation();
		reset(0);
	}

	protected void onLayout(boolean  paramboolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
	{
		this.mPanel.layout(0, 0, paramInt3 - paramInt1, paramInt4 - paramInt2);
	}

	protected void onMeasure(int paramInt1, int paramInt2)
	{
		this.mWidth = View.MeasureSpec.getSize(paramInt1);
		this.mDismissWidthThreshold = ((int) (0.5F * this.mWidth));
		int i = View.MeasureSpec.getSize(paramInt2);
		int j = View.MeasureSpec.getMode(paramInt2);
		this.mPanel.measure(View.MeasureSpec.makeMeasureSpec(this.mWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(i, j));
		super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(this.mPanel.getMeasuredHeight(), 1073741824));
		setScrollLimits(-this.mWidth, this.mWidth);
	}

	protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
	{
		super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
		this.mScrollDelta = (paramInt1 - paramInt3);
		if (Math.abs(paramInt1) == this.mWidth)
			dismiss();
		updateBackgroundView(this.mLeftBackgroundView, paramInt1, true);
		updateBackgroundView(this.mRightBackgroundView, paramInt1, false);
	}

	public boolean  onTouchEvent(MotionEvent paramMotionEvent)
	{
		boolean  flag = super.onTouchEvent(paramMotionEvent);
		if ((!this.mDismissible) || (this.mRead == 1))
			return false;

		switch (paramMotionEvent.getAction())
		{
			case 2:
			default:
			case 1:
			case 3:
		}
		
		/*int i = getScroll();
		if ((i == 0) && (this.mListItemActionListener != null))
		{
			// this.mPanel.setBackgroundResource(R.color.generic_selected_item);
			this.mPanel.postDelayed(new Runnable() {
				public final void run()
				{
					DismissibleListItemView.this.setRead(1);
				}
			}, 300L);
			this.mListItemActionListener.onClick();
		}
		else
		{
			onSwipeComplete(i);
		}*/

		//onSwipeComplete(getScroll());
		return flag;
	}

	public void setDismissListener(ListItemActionListener paramListItemActionListener)
	{
		this.mListItemActionListener = paramListItemActionListener;
	}

	public void setRead(int i)
	{
		mRead = i;
		View view = mPanel;
		int flag;
		if (mRead == 0)
		{
			// flag = R.drawable.notification_item_background_unread;
		}
		else
		{
			// flag = R.drawable.notification_item_background_read;
		}
		// view.setBackgroundResource(flag);

		boolean  f = false;
		if (mRead != 1 && mDismissible)
		{
			f = true;
		}
		else
		{
			f = false;
		}
		setScrollEnabled(f);
	}

	public static abstract interface ListItemActionListener
	{
		public abstract void onClick();

		public abstract void onDismiss();
	}
}