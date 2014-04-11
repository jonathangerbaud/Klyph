package com.abewy.android.apps.klyph.util;

import java.util.LinkedHashMap;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.abewy.android.apps.klyph.R;

public class EmojiUtil
{
	public static final LinkedHashMap<String, Integer>	EMOJIS;

	static
	{
		EMOJIS = new LinkedHashMap<String, Integer>();
		EMOJIS.put("O:)", R.drawable.ic_emoji_angel);
		EMOJIS.put("3:)", R.drawable.ic_emoji_devil);
		EMOJIS.put(":)", R.drawable.ic_emoji_happy);
		EMOJIS.put(">:(", R.drawable.ic_emoji_grumpy);
		EMOJIS.put(":(", R.drawable.ic_emoji_sad);
		EMOJIS.put(":P", R.drawable.ic_emoji_tongue);
		EMOJIS.put("=D", R.drawable.ic_emoji_grin);
		EMOJIS.put(">:o", R.drawable.ic_emoji_upset);
		EMOJIS.put(":o", R.drawable.ic_emoji_gasp);
		EMOJIS.put(";)", R.drawable.ic_emoji_wink); //
		//EMOJIS.put(":v", R.drawable.emo_pacman);
		
		EMOJIS.put(":/", R.drawable.ic_emoji_unsure);
		EMOJIS.put(":'(", R.drawable.ic_emoji_cry);
		EMOJIS.put("^_^", R.drawable.ic_emoji_kiki);
		EMOJIS.put("8)", R.drawable.ic_emoji_geek);
		EMOJIS.put("B|", R.drawable.ic_emoji_cool);//
		EMOJIS.put("<3", R.drawable.ic_emoji_heart);
		
		EMOJIS.put("-_-", R.drawable.ic_emoji_squint);
		EMOJIS.put("o.O", R.drawable.ic_emoji_confuse);
		EMOJIS.put(":3", R.drawable.ic_emoji_cute);
		EMOJIS.put("(y)", R.drawable.ic_emoji_like);
		EMOJIS.put(":*", R.drawable.ic_emoji_kiss);
		EMOJIS.put(":$", R.drawable.ic_emoji_embarrassed);
	}
	
	public static void convertTextToEmoji(TextView textView)
	{
		convertTextToEmoji(textView, true);
	}

	public static void convertTextToEmoji(TextView textView, boolean addLinks)
	{
		textView.setTextKeepState(getSpannableForText(textView.getContext(), textView.getText().toString(), addLinks), BufferType.SPANNABLE);
	}
	
	public static Spannable getSpannableForText(Context context, String text)
	{
		return getSpannableForText(context, text, true);
	}

	public static Spannable getSpannableForText(Context context, String text, boolean addLinks)
	{
		SpannableStringBuilder ssb = new SpannableStringBuilder(text);
		
		Linkify.addLinks(ssb, Linkify.WEB_URLS);

		for (String key : EMOJIS.keySet())
		{
			int index = text.indexOf(key);
			while (index != -1)
			{
				if (ssb.getSpans(index, index + 1, Object.class).length == 0/* && ssb.getSpans(index, index + 1, URLSpan.class).length == 0*/)
					ssb.setSpan(new ImageSpan(context, EMOJIS.get(key)), index, index + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				
				index = text.indexOf(key, index + key.length());
			}
		}
		
		if (!addLinks)
		{
			URLSpan[] spans = ssb.getSpans(0, text.length() - 1, URLSpan.class);
			for (URLSpan urlSpan : spans)
			{
				ssb.removeSpan(urlSpan);
			}
		}

		return ssb;
	}
}
