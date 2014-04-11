package com.abewy.android.extended.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * A FrameLayout which height will be calculated relative to its width
 * keeping the aspect ratio
 */
public class RatioFrameLayout extends FrameLayout
{
	public static final int	WIDTH	= 1;
	public static final int	HEIGHT	= 2;
	
	private float ratio = 1.0f;
	private int primarySide = WIDTH;
	
	public RatioFrameLayout(Context context)
    {
        super(context);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
   
    /**
     * Set the ratio of the image to be loaded
     * Ratio must be height / width
     * 
     * @param ratio The image size ratio
     */
    public void setRatio(float ratio)
    {
    	this.ratio = ratio;
    }
    
    /**
     * Set the size of the image to be loaded
     * 
     * @param width Width of the image
     * @param height Height of the image
     */
    public void setSize(int width, int height)
    {
    	ratio = (float) height / width;
    }
    
    public void setPrimarySide(int side)
    {
    	primarySide = side;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        if (primarySide == WIDTH)
        	setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth() * ratio));
        else
        	setMeasuredDimension((int) (getMeasuredHeight() * (1/ratio)), getMeasuredHeight());
        	
    }
}
