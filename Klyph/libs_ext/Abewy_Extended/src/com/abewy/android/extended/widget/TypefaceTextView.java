/**
* @author Jonathan
* 
*  This is a merge of two libraries
*  https://github.com/ragunathjawahar/android-typeface-textview
*  https://gist.github.com/twaddington/3739637
*  
* Copyright (C) 2013 Mobs and Geeks
* Copyright 2012 Simple Finance Corporation (https://www.simple.com)
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
* except in compliance with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software distributed under the
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
* either express or implied. See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.abewy.android.extended.widget;

import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import com.abewy.android.extended.R;

public class TypefaceTextView extends TextView
{
	/*
	* Caches typefaces based on their file path and name, so that they don't have to be created
	* every time when they are referenced.
	*/
	    private static Map<String, Typeface> mTypefaces;

	    public TypefaceTextView(final Context context) {
	        this(context, null);
	    }

	    public TypefaceTextView(final Context context, final AttributeSet attrs) {
	        this(context, attrs, 0);
	    }

	    public TypefaceTextView(final Context context, final AttributeSet attrs, final int defStyle) {
	        super(context, attrs, defStyle);
	        if (mTypefaces == null) {
	            mTypefaces = new HashMap<String, Typeface>();
	        }

	        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);
	        if (array != null) {
	            final String typefaceAssetPath = array.getString(
	            		R.styleable.TypefaceTextView_fontFamily);
	            
	            if (typefaceAssetPath != null) {
	                Typeface typeface = null;

	                if (mTypefaces.containsKey(typefaceAssetPath)) {
	                    typeface = mTypefaces.get(typefaceAssetPath);
	                } else {
	                    AssetManager assets = context.getAssets();
	                    typeface = Typeface.createFromAsset(assets, typefaceAssetPath + ".ttf");
	                    mTypefaces.put(typefaceAssetPath, typeface);
	                }

	                setTypeface(typeface);
	            }
	            array.recycle();
	        }
	    }
}
