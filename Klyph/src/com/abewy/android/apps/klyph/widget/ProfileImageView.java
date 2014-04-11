package com.abewy.android.apps.klyph.widget;

import info.evelio.drawable.RoundedAvatarDrawable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.squareup.picasso.PicassoDrawable;

public class ProfileImageView extends BezelImageView
{
	public ProfileImageView(Context context)
	{
		super(context);
		initDrawables(context);
	}

	public ProfileImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initDrawables(context);
	}

	public ProfileImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initDrawables(context);
	}

	private void initDrawables(Context context)
	{
		final int themeColor = AttrUtil.getColor(context, R.attr.themeColor);
		final int borderWidth = context.getResources().getDimensionPixelSize(R.dimen.profile_image_border_size);

		setBorderDrawable(new BorderDrawable(KlyphPreferences.isRoundedPictureEnabled() ? BorderDrawable.OVAL : BorderDrawable.RECT, themeColor,
				borderWidth));
	}

	public void disableBorder()
	{
		setBorderDrawable(null);
	}

	@Override
	public void setImageBitmap(Bitmap bitmap)
	{
		if (KlyphPreferences.isRoundedPictureEnabled())
			super.setImageDrawable(new RoundedAvatarDrawable(bitmap));
		else
			super.setImageBitmap(bitmap);
	}

	@Override
	public void setImageDrawable(Drawable drawable)
	{
		if (KlyphPreferences.isRoundedPictureEnabled() && drawable instanceof PicassoDrawable)
		{
			super.setImageDrawable(new RoundedAvatarDrawable(((PicassoDrawable) drawable).getImage().getBitmap()));
		}
		else
		{
			super.setImageDrawable(drawable);
		}
	}

	private static class BorderDrawable extends StateListDrawable
	{
		public static final String	RECT	= "rect";
		public static final String	OVAL	= "oval";

		public BorderDrawable(String shapeType, final int borderColor, final int borderWidth)
		{
			super();

			final Shape shape = shapeType.equals(RECT) ? new RectShape() : new OvalShape();

			final ShapeDrawable transparentShape = new ShapeDrawable(shape);
			transparentShape.getPaint().setColor(0x00000000);// Transparent

			final GradientDrawable shapeDrawable = new GradientDrawable();
			shapeDrawable.setShape(shapeType.equals(RECT) ? GradientDrawable.RECTANGLE : GradientDrawable.OVAL);
			shapeDrawable.setStroke(borderWidth, borderColor);

			addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused, -android.R.attr.state_pressed }, shapeDrawable);
			addState(new int[] { android.R.attr.state_enabled, -android.R.attr.state_focused, android.R.attr.state_pressed }, shapeDrawable);
			addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused, android.R.attr.state_pressed }, shapeDrawable);
			addState(new int[] {}, transparentShape);
		}
	}
}
