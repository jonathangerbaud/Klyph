package com.abewy.android.extended.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * An ImageView which height will be calculated relative to its width
 * keeping the image aspect ratio
 */
public class RatioImageView extends ImageView
{
	public static final int	WIDTH	= 1;
	public static final int	HEIGHT	= 2;
	
	private float ratio = 1.0f;
	private int primarySide = WIDTH;
	
	public RatioImageView(Context context)
    {
        super(context);
        setScaleType(ScaleType.FIT_XY);
    }

    public RatioImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setScaleType(ScaleType.FIT_XY);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setScaleType(ScaleType.FIT_XY);
    }
    
   
    /**
     * Set the ratio of the image to be loaded
     * Ratio must be height / width
     * 
     * @param ratio The image size ratio
     */
    public void setImageRatio(float ratio)
    {
    	this.ratio = ratio;
    }
    
    /**
     * Set the size of the image to be loaded
     * 
     * @param width Width of the image
     * @param height Height of the image
     */
    public void setImageSize(int width, int height)
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
