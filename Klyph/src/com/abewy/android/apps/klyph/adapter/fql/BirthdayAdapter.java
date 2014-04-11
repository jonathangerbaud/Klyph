package com.abewy.android.apps.klyph.adapter.fql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.PicturePrimarySecondaryTextHolder;
import com.abewy.android.apps.klyph.core.fql.Friend;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.R;

public class BirthdayAdapter extends KlyphAdapter
{
	public BirthdayAdapter()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		//return R.layout.item_picture_primary_secondary_text;
		return R.layout.item_grid_picture_primary_secondary_text;
	}

	@Override
	protected void attachHolder(View view)
	{
		ImageView userPicture = (ImageView) view.findViewById(R.id.picture);
		TextView userName = (TextView) view.findViewById(R.id.primary_text);
		TextView userBirthday = (TextView) view.findViewById(R.id.secondary_text);
		
		setHolder(view, new PicturePrimarySecondaryTextHolder(userPicture, userName, userBirthday));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);
		
		PicturePrimarySecondaryTextHolder holder = (PicturePrimarySecondaryTextHolder) getHolder(view);
		
		//holder.getPicture().setImageDrawable(null);
		
		Friend friend = (Friend) data;

		holder.getPrimaryText().setText(friend.getName());
		
		String birthday = friend.getBirthday();

		Date birthdayDate = null;
		String format = "MM/dd/y";
		
		try
		{
			birthdayDate = new SimpleDateFormat(format).parse(friend.getBirthday_date());
		}
		catch (ParseException e)
		{
			//e.printStackTrace();
		}
		
		if (birthdayDate != null)
		{
			Calendar dobDate = Calendar.getInstance();
			dobDate.setTime(birthdayDate);
			int year = dobDate.get(Calendar.YEAR);
			int month = dobDate.get(Calendar.MONTH);
			int day = dobDate.get(Calendar.DAY_OF_MONTH);

			Calendar today = Calendar.getInstance();
			today.set(Calendar.DAY_OF_MONTH, day);
			today.set(Calendar.MONTH, month);
			
			int curYear = today.get(Calendar.YEAR);
			int curMonth = today.get(Calendar.MONTH);
			int curDay = today.get(Calendar.DAY_OF_MONTH);
			
			
			int age = curYear - year;
			if (curMonth < month || (month == curMonth && curDay < day)) {
			    age--;
			}
						
			birthday += " - " + getContext(holder.getPrimaryText()).getString(R.string.birthday_age, age);
		}
		
		holder.getSecondaryText().setText(birthday);

		loadImage(holder.getPicture(), friend.getPic(), AttrUtil.getResourceId(getContext(holder.getPrimaryText()), R.attr.squarePlaceHolderIcon), data);
	}
}
