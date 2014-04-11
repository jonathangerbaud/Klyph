package com.abewy.android.apps.klyph.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.time.FastDateFormat;
import android.content.Context;
import android.content.res.Resources;
import com.abewy.android.apps.klyph.R;

public class DateUtil
{
	private static final Calendar CALENDAR = Calendar.getInstance();
	
	private static SimpleDateFormat getDateFormat()
	{
		SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL);
		String pattern = dateFormat.toLocalizedPattern();

		pattern = pattern.replace("y", "");
		pattern = pattern.replace("E", "");
		pattern = pattern.replace(",", "");
		pattern = pattern.replace("  ", " ");
		pattern = pattern.trim();

		dateFormat.applyLocalizedPattern(pattern);

		return dateFormat;
	}

	private static String getFormattedDate(Date date)
	{
		SimpleDateFormat dateFormat = getDateFormat();

		return dateFormat.format(date);
	}

	private static String getFormattedFullDate(Date date)
	{
		SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL);
		String pattern = dateFormat.toLocalizedPattern();

		pattern = pattern.replace("E", "");
		pattern = pattern.replace(",", "");
		pattern = pattern.replace("  ", " ");
		pattern = pattern.trim();
		
		dateFormat.applyLocalizedPattern(pattern);

		return dateFormat.format(date);
	}

	private static String getFormattedTime(Date date)
	{
		SimpleDateFormat timeFormat = (SimpleDateFormat) SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);

		return timeFormat.format(date);
	}

	public static String getFormattedDateTime(String unixDate)
	{
		Date date;

		try
		{
			long when = Long.parseLong(unixDate);
			date = new Date(when * 1000);
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			return "";
		}

		return new StringBuilder(getFormattedDate(date)).append(" ").append(getFormattedTime(date)).toString();
	}

	public static String getFormattedDateTimeWithYear(String unixDate)
	{
		Date date;

		try
		{
			long when = Long.parseLong(unixDate);
			date = new Date(when * 1000);
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			return "";
		}

		return new StringBuilder(getFormattedFullDate(date)).append(" ").append(getFormattedTime(date)).toString();
	}

	public static String timeAgoInWords(Context context, String time)
	{
		long when = 0;
		Date localDate;

		Resources res = context.getResources();

		try
		{
			when = Long.parseLong(time);
			localDate = new Date(when * 1000);
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			return "";
		}

		Date now = new Date();
		long difference = now.getTime() - localDate.getTime();
		long distanceInMin = difference / 60000;

		if (distanceInMin <= 1)
		{
			return res.getString(R.string.one_minute_ago);
		}
		else if (distanceInMin <= 59)
		{
			return String.format(res.getString(R.string.minutes_ago), distanceInMin);
		}
		else
		{
			CALENDAR.setTime(new Date());
			
			int nowDay = CALENDAR.get(Calendar.DATE);
			CALENDAR.setTime(localDate);
			int localDay = CALENDAR.get(Calendar.DATE);

			if (distanceInMin < 60 * 24 && nowDay == localDay)
			{
				return context.getString(R.string.stream_today_date, getFormattedTime(localDate));
			}
			if (distanceInMin < 60 * 24 * 2 && nowDay == localDay + 1)
			{
				return context.getString(R.string.stream_yesterday_date, getFormattedTime(localDate));
			}
			else if (distanceInMin < 60 * 24 * 7)
			{
				SimpleDateFormat sdf = new SimpleDateFormat();
				sdf.applyPattern("EEEE");
				return context.getString(R.string.stream_date, sdf.format(localDate), getFormattedTime(localDate));
			}
			else
			{
				return context
						.getString(R.string.stream_date, getFormattedDate(localDate), getFormattedTime(localDate));
			}
		}
	}

	public static String getUnixTimeFromDate(String date)
	{
		if (date != null)
		{
			Date d;

			try
			{
				if (date.length() == 10)
					d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
				else
					d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(date);
			}
			catch (ParseException e)
			{
				e.printStackTrace();
				return "";
			}

			return String.valueOf(d.getTime() / 1000);
		}

		return "";
	}

	public static String getDateTime(String date, boolean dateFormat)
	{
		if (dateFormat == true)
		{
			try
			{
				Date d;

				if (date.length() == 10)
					d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
				else
					d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(date);

				StringBuilder dateString = new StringBuilder(SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(d));

				if (date.length() > 10)
				{
					dateString.append(" ");
					dateString.append(SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(d));
				}

				return dateString.toString();
			}
			catch (ParseException e)
			{
				return "";
			}
		}

		return getDateTime(date);
	}

	public static String getDateTime(String date)
	{
		try
		{
			Date d = new Date(Long.valueOf(date) * 1000);
			StringBuilder dateString = new StringBuilder(SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(d));
			dateString.append(" ");
			dateString.append(SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(d));
			return dateString.toString();
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	public static String getDate(String date)
	{
		try
		{
			Date d = new Date(Long.valueOf(date) * 1000);
			return SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(d);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}

	public static String getTime(String date)
	{
		try
		{
			Date d = new Date(Long.valueOf(date) * 1000);
			return SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(d);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}
	
	public static boolean areSameDay(String date1, String date2)
	{
		return areSameDay(date1, date2, false);
	}

	public static boolean areSameDay(String date1, String date2, boolean areDates)
	{
		if (areDates)
		{
			date1 = getUnixTimeFromDate(date1);
			date2 = getUnixTimeFromDate(date2);
		}
		
		date1 = getDate(date1);
		date2 = getDate(date2);

		if (date1 != null)
		{
			return date1.equals(date2);
		}
		
		return false;
	}
	
	public static String getShortDate(String unixDate)
	{
		Date date = new Date(Long.parseLong(unixDate)*1000);
		Date now = new Date();
		
		
		String pattern = "";
		
		// If date < 7 days
		if (now.getTime() - date.getTime() < 7 * 24 * 60 * 60 * 1000)
		{
			pattern = "E";
		}
		else
		{
			FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
			pattern = dateFormat.getPattern();
			pattern = pattern.replace("y", "");
			pattern = pattern.replace("年", "");//y in chinese
			pattern = pattern.replace(",", "");
			pattern = pattern.replace("  ", " ");
			pattern = pattern.trim();
			
			if (pattern.indexOf("/") == 0 || pattern.indexOf("-") == 0 || pattern.indexOf(".") == 0)
			{
				pattern = pattern.substring(1);
			}
		}
		
		FastDateFormat dateFormat = FastDateFormat.getInstance(pattern);
		
		return dateFormat.format(date);
	}
	
	public static String getShortDateTime(String unixDate)
	{
		Date date = new Date(Long.parseLong(unixDate)*1000);
		Calendar c1 = CALENDAR;
		c1.setTime(new Date());
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date);
		
		
		FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
		String pattern = dateFormat.getPattern();
		
		// If not same year
		if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
		{
			pattern = pattern.replace("y", "");
			
			if (pattern.indexOf("/") == 0 || pattern.indexOf("-") == 0 || pattern.indexOf(".") == 0  || pattern.indexOf("年") == 0)
			{
				pattern = pattern.substring(1);
			}
			
/*			pattern = pattern.replace("EEEE", "EEE");
			pattern = pattern.replace("MMMM", "");
			pattern = pattern.replace("d", "");
		}
		else
		{
			pattern = pattern.replace("MMMM", "MMM");
			pattern = pattern.replace("EEEE", "");*/
		}
		
		pattern = pattern.replace("  ", " ");
		pattern = pattern.trim();
		
		dateFormat = FastDateFormat.getInstance(pattern);
		
		return dateFormat.format(date);
	}
}
