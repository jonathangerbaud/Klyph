package com.abewy.android.apps.klyph.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 
 * @author paul.blundell
 * 
 */
public class AnimationHelper
{

	private static final int	ANIMATION_LENGTH	= 500;
	private static final float	ALPHA_100			= 1.0f;
	private static final float	ALPHA_0				= 0.0f;
	private static final float	SCALE_1				= 1.0f;
	private static final float	SCALE_0				= 0.0f;
	
	/**
	 * @return A fade out animation from 100% - 0% taking half a second
	 */
	public static Animation createHeightOutAnimation()
	{
		Animation heightOut = new ScaleAnimation(SCALE_1, SCALE_1, SCALE_1, SCALE_0);
		heightOut.setDuration(ANIMATION_LENGTH);
		return heightOut;
	}
	
	/**
	 * @return A translate animation 
	 */
	public static Animation createTranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
	{
		Animation fadeout = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
		fadeout.setDuration(ANIMATION_LENGTH);
		return fadeout;
	}
	
	/**
	 * @return A fade out animation from 100% - 0% taking half a second
	 */
	public static Animation createFadeoutAnimation()
	{
		Animation fadeout = new AlphaAnimation(ALPHA_100, ALPHA_0);
		fadeout.setDuration(ANIMATION_LENGTH);
		return fadeout;
	}

	/**
	 * @return A fade in animation from 0% - 100% taking half a second
	 */
	public static Animation createFadeInAnimation()
	{
		Animation animation = new AlphaAnimation(ALPHA_0, ALPHA_100);
		animation.setDuration(ANIMATION_LENGTH);
		return animation;
	}

}
