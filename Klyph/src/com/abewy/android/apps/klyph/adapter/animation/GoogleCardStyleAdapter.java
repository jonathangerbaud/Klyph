package com.abewy.android.apps.klyph.adapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;

public class GoogleCardStyleAdapter extends KlyphAnimationAdapter
{
	private static final int DELAY = 200;
	private static final int DURATION = 300;
	
	private final long	mAnimationDelayMillis;
	private final long	mAnimationDurationMillis;

	public GoogleCardStyleAdapter(BaseAdapter baseAdapter)
	{
		this(baseAdapter, DEFAULTANIMATIONDELAYMILLIS, DEFAULTANIMATIONDURATIONMILLIS);
	}

	public GoogleCardStyleAdapter(BaseAdapter baseAdapter, long animationDelayMillis)
	{
		this(baseAdapter, animationDelayMillis, DEFAULTANIMATIONDURATIONMILLIS);
	}

	public GoogleCardStyleAdapter(BaseAdapter baseAdapter, long animationDelayMillis, long animationDurationMillis)
	{
		super(baseAdapter);
		mAnimationDelayMillis = animationDelayMillis;
		mAnimationDurationMillis = animationDurationMillis;
	}

	@Override
	protected long getAnimationDelayMillis()
	{
		return mAnimationDelayMillis;
	}

	@Override
	protected long getAnimationDurationMillis()
	{
		return mAnimationDurationMillis;
	}

	@Override
	public Animator[] getAnimators(ViewGroup parent, View view)
	{
		ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 300, 0);
		translationY.setInterpolator(new DecelerateInterpolator());
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.95f, 1f);
		scaleX.setInterpolator(new DecelerateInterpolator());
		//ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", mScaleFrom, 1f);
		return new ObjectAnimator[] { translationY, scaleX/*, scaleY*/ };
	}

}
