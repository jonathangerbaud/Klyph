package com.abewy.android.apps.klyph.app;

import java.util.List;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.KlyphApplication;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.core.request.BaseAsyncRequest;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.fragment.IKlyphFragment;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.util.KlyphUtil;
import com.abewy.net.ConnectionState;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public abstract class ProfileActivity extends TitledViewPagerActivity implements OnScrollListener
{
	// Newsstand effect
	private View						mHeader;
	private ImageView					mHeaderPicture;
	private ImageView					mHeaderLogo;
	private View						headerNameButton;
	private PageIndicator				pageIndicator;
	private int							mMinHeaderTranslation;
	private int							mMinHeaderNameButtonTranslation;
	private int							mMinPageIndicatorTranslation;
	private int							mActionBarTitleColor;
	private AlphaForegroundColorSpan	mAlphaForegroundColorSpan;
	private SpannableString				mSpannableString;
	private RectF						mRect1	= new RectF();
	private RectF						mRect2	= new RectF();
	private DecelerateInterpolator		mSmoothInterpolator;
	private float						interpolation;
	private int							fakeHeaderHeight;
	private int							mActionBarHeight;
	private int							listPadding;
	private View						errorView;
	private TextView					errorTextView;
	private Button						errorButton;
	private boolean						isLoading;
	private boolean						isDestroyed = false;

	private String						id;

	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

		final float ratio = getRatio();
		final int coverImageHeaderHeight = (int) (KlyphDevice.getDeviceWidth() * ratio);

		super.onCreate(savedInstanceState);

		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_background_transparent_gradient));

		// Let the Application class know that the first launch is complete
		// If we come from a notification, then do not show the ads
		// When going back to main activity
		KlyphApplication.getInstance().launchComplete();

		id = getIntent().getStringExtra(getBundleIdParameter());
		String name = getIntent().getStringExtra(getBundleNameParameter());

		TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.title_indicator);
		TabPageIndicator tabIndicator = (TabPageIndicator) findViewById(R.id.tab_indicator);

		if (KlyphPreferences.showTabPageIndicator())
			titleIndicator.setVisibility(View.GONE);
		else
			tabIndicator.setVisibility(View.GONE);

		mHeader = findViewById(R.id.header);
		mHeaderPicture = (ImageView) findViewById(R.id.header_picture);
		mHeaderLogo = (ImageView) findViewById(R.id.header_logo);
		headerNameButton = findViewById(R.id.header_name_button);
		pageIndicator = getPageIndicator();

		errorView = findViewById(R.id.error_layout);
		errorTextView = (TextView) findViewById(R.id.error_text);
		errorButton = (Button) findViewById(R.id.retry_button);

		ImageLoader.display((ImageView) mHeaderPicture, ImageLoader.FAKE_URI, true, R.drawable.cover_place_holder);
		
		if (mHeaderLogo != null)
			ImageLoader.display((ImageView) mHeaderLogo, ImageLoader.FAKE_URI, true, KlyphUtil.getProfilePlaceHolder(this));

		initComponents();

		mSmoothInterpolator = new DecelerateInterpolator();
		mMinHeaderTranslation = -coverImageHeaderHeight + getActionBarHeight();

		mActionBarTitleColor = 0xFFFFFF;

		mSpannableString = new SpannableString(name);
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(mActionBarTitleColor);

		setupActionBar();

		final View content = findViewById(android.R.id.content);

		if (hasCachedData(savedInstanceState))
		{
			content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onGlobalLayout()
				{
					content.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					loadCachedData(savedInstanceState);
				}
			});
		}
		else
		{
			loadData();
		}
	}

	private void loadCachedData(Bundle savedInstanceState)
	{
		List<GraphObject> data = getCachedDataFromInstanceState(savedInstanceState);
		onRequestSuccess(data);
		onScroll(null, 0, 0, 0);
		getPageIndicator().setCurrentItem(savedInstanceState.getInt("position"));
	}

	private void loadData()
	{
		isLoading = true;
		invalidateOptionsMenu();
		new AsyncRequest(getQuery(), id, getQueryParam(), new BaseAsyncRequest.Callback() {

			@Override
			public void onComplete(final Response response)
			{
				isLoading = false;
				
				if (isDestroyed)
					return;

				runOnUiThread(new Runnable() {

					@Override
					public void run()
					{
						if (response.getError() == null)
						{
							onRequestSuccess(response.getGraphObjectList());
						}
						else
						{
							onRequestError(response.getError());
						}

						invalidateOptionsMenu();
					}
				});
			}
		}).execute();
	}

	@Override
	protected int getCustomTheme()
	{
		return KlyphPreferences.getProfileTheme();
	}

	private void onRequestSuccess(List<GraphObject> result)
	{
		computeAndSetComponentsHeights();

		if (result.size() > 0)
			initComponentsOnRequestSucces(result);

		getActionBar().show();
		mHeader.setVisibility(View.VISIBLE);

		if (KlyphDevice.isPortraitMode())
			headerNameButton.setVisibility(View.VISIBLE);

		showPageIndicator();
		showViewPager();
		
		if (KlyphPreferences.areBannerAdsEnabled())
			initAds();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if (isLoading)
		{
			MenuItem refreshItem = menu.add(Menu.NONE, R.id.menu_refresh, 1, R.string.refresh);
			refreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			refreshItem.setActionView(R.layout.actionbar_item_refresh);
		}

		return super.onCreateOptionsMenu(menu);
	}

	private void onRequestError(RequestError error)
	{
		int errorText = R.string.request_error;

		if (!ConnectionState.getInstance(this).isOnline())
		{
			errorText = R.string.request_connexion_error;
		}

		errorTextView.setText(errorText);

		errorButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				errorView.setVisibility(View.GONE);
				loadData();
			}
		});

		errorView.setVisibility(View.VISIBLE);
	}

	@Override
	protected int getInitialPosition()
	{
		return -1;
	}


	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		saveCachedDataToInstanceState(outState);
		outState.putInt("position", getViewPager().getCurrentItem());
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		int scrollY = getScrollY();
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			scrollY += fakeHeaderHeight;
		}

		// sticky actionbar
		mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
		headerNameButton.setTranslationY(Math.max(-scrollY, mMinHeaderNameButtonTranslation));
		
		float ratio = clamp(headerNameButton.getTranslationY() / mMinHeaderNameButtonTranslation, 0.0f, 1.0f);
		setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
		
		if (KlyphDevice.isPortraitMode())
		{
			((View) getPageIndicator()).setTranslationY(Math.max(-scrollY, mMinPageIndicatorTranslation));
			ratio = clamp(((View) getPageIndicator()).getTranslationY() / mMinPageIndicatorTranslation, 0.0f, 1.0f);
			setPageIndicatorAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
		}
		else
		{
			setPageIndicatorAlpha(1.0f); 
		}
		
		float tint = clamp(mHeader.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);
		setHeaderImageTint(tint);


		// move & scale

		if (mHeaderLogo != null)
		{
			interpolation = mSmoothInterpolator.getInterpolation(ratio);

			View actionBarIconView = getActionBarIconView();

			getOnScreenRect(mRect1, mHeaderLogo);
			getOnScreenRect(mRect2, actionBarIconView);

			float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
			float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
			float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
			float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

			mHeaderLogo.setTranslationX(translationX);
			mHeaderLogo.setTranslationY(translationY - mHeader.getTranslationY());
			mHeaderLogo.setScaleX(scaleX);
			mHeaderLogo.setScaleY(scaleY);
		}
	}

	public int getScrollY()
	{
		IKlyphFragment f = (IKlyphFragment) ((FragmentPagerAdapter) getPagerAdapter()).getItem(getViewPager().getCurrentItem());

		if (f == null || f.getListView() == null)
			return 0;

		listPadding = f.getListView().getPaddingTop();

		View c = f.getListView().getChildAt(0);
		if (c == null)
		{
			return 0;
		}

		int firstVisiblePosition = f.getListView().getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1)
		{
			headerHeight = fakeHeaderHeight;
		}
		// Log.d("UserActivity", "getScrollY: " + top + " " + headerHeight);
		return -top + firstVisiblePosition * c.getHeight() + headerHeight + listPadding;
	}

	public static float clamp(float value, float max, float min)
	{
		return Math.max(Math.min(value, min), max);
	}

	private RectF getOnScreenRect(RectF rect, View view)
	{
		rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
		return rect;
	}

	private ImageView getActionBarIconView()
	{
		return (ImageView) findViewById(android.R.id.home);
	}

	private void setupActionBar()
	{
		getActionBar().setIcon(R.drawable.ic_transparent);
		getActionBar().setLogo(R.drawable.ic_transparent);
		getActionBar().setTitle("");
	}

	private void setTitleAlpha(float alpha)
	{
		mAlphaForegroundColorSpan.setAlpha(alpha);
		mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getActionBar().setTitle(mSpannableString);
	}
	
	private void setHeaderImageTint(float tint)
	{
		Log.d("ProfileActivity", "setHeaderImageTint: " + tint);
		mHeaderPicture.setColorFilter(Color.argb((int) (tint * 0x66), 0, 0, 0), Mode.SRC_ATOP); 
	}

	private void setPageIndicatorAlpha(float alpha)
	{
		float a = alpha * 255;
		((View) getPageIndicator()).getBackground().setAlpha(255 - (int) a);
	}

	public int getActionBarHeight()
	{
		if (mActionBarHeight != 0)
		{
			return mActionBarHeight;
		}

		TypedValue mTypedValue = new TypedValue();
		getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
		mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());

		return mActionBarHeight;
	}

	public static class AlphaForegroundColorSpan extends ForegroundColorSpan
	{

		private float	mAlpha;

		public AlphaForegroundColorSpan(int color)
		{
			super(color);
		}

		@Override
		public void updateDrawState(TextPaint ds)
		{
			ds.setColor(getAlphaColor());
		}

		public void setAlpha(float alpha)
		{
			mAlpha = alpha;
		}

		public float getAlpha()
		{
			return mAlpha;
		}

		private int getAlphaColor()
		{
			int foregroundColor = getForegroundColor();
			return Color.argb((int) (mAlpha * 255), Color.red(foregroundColor), Color.green(foregroundColor), Color.blue(foregroundColor));
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{

	}

	protected void computeAndSetComponentsHeights()
	{
		final float ratio = getRatio();
		int headerHeight = (int) (KlyphDevice.getDeviceWidth() * ratio);

		final int headerNameButtonHeight = headerNameButton.getHeight();
		final int pageIndicatorHeight = ((View) pageIndicator).getHeight();
		final int actionBarHeight = getActionBarHeight();

		fakeHeaderHeight = headerHeight + headerNameButtonHeight + pageIndicatorHeight;

		mMinHeaderTranslation = -headerHeight + actionBarHeight + pageIndicatorHeight;
		mMinHeaderNameButtonTranslation = -(headerHeight + headerNameButtonHeight) + actionBarHeight;
		mMinPageIndicatorTranslation = -fakeHeaderHeight + actionBarHeight + pageIndicatorHeight;

		/*Log.d("UserActivity", "computeAndSetComponentsHeights: headerHeight" + headerHeight);
		// Log.d("UserActivity", "computeAndSetComponentsHeights: headerLogoHeight" + headerLogoHeight);
		Log.d("UserActivity", "computeAndSetComponentsHeights: headerNameButtonHeight" + headerNameButtonHeight);
		Log.d("UserActivity", "computeAndSetComponentsHeights: pageIndicatorHeight" + pageIndicatorHeight);
		Log.d("UserActivity", "computeAndSetComponentsHeights: actionBarHeight" + actionBarHeight);
		Log.d("UserActivity", "computeAndSetComponentsHeights: mMinHeaderTranslation" + mMinHeaderTranslation);
		Log.d("UserActivity", "computeAndSetComponentsHeights: mMinHeaderNameButtonTranslation" + mMinHeaderNameButtonTranslation);
		Log.d("UserActivity", "computeAndSetComponentsHeights: mMinPageIndicatorTranslation" + mMinPageIndicatorTranslation);
		Log.d("UserActivity", "computeAndSetComponentsHeights: fakeHeaderHeight" + fakeHeaderHeight);
		Log.d("UserActivity", "computeAndSetComponentsHeights: listGridHeaderHeight" + listGridHeaderHeight);*/

		if (mHeaderLogo != null)
		{
			final int headerLogoHeight = mHeaderLogo.getHeight();
			android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) mHeaderLogo.getLayoutParams();
			params.topMargin = (headerHeight) - (int) (headerLogoHeight / 2);
			mHeaderLogo.setLayoutParams(params);
		}

		android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) headerNameButton.getLayoutParams();
		params.topMargin = headerHeight;
		headerNameButton.setLayoutParams(params);

		params = (android.widget.FrameLayout.LayoutParams) ((View) pageIndicator).getLayoutParams();
		
		if (KlyphDevice.isPortraitMode())
		{
			params.topMargin = headerHeight + headerNameButtonHeight;
		}
		else
		{
			params.topMargin = actionBarHeight;
		}
			
		((View) pageIndicator).setLayoutParams(params);
	}

	// Methods to override
	protected abstract float getRatio();

	protected abstract int getQuery();
	
	protected abstract String getQueryParam();

	/**
	 * That's where you make some view = findViewById(R.id.something) stuff
	 */
	protected abstract void initComponents();

	protected abstract String getBundleIdParameter();

	protected abstract String getBundleNameParameter();
	
	protected abstract boolean hasCachedData(Bundle savedInstanceState); 

	protected abstract List<GraphObject> getCachedDataFromInstanceState(Bundle savedInstanceState);

	protected abstract void saveCachedDataToInstanceState(Bundle outState);

	protected abstract void initComponentsOnRequestSucces(List<GraphObject> result);
	
	protected String getElementId()
	{
		return id;
	}

	protected ImageView getHeaderPicture()
	{
		return mHeaderPicture;
	}

	protected ImageView getHeaderLogo()
	{
		return mHeaderLogo;
	}
	
	protected int getFakeHeaderHeight()
	{
		return fakeHeaderHeight;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		mHeader = null;
		mHeaderPicture = null;
		mHeaderLogo = null;
		headerNameButton = null;
		pageIndicator = null;
		mAlphaForegroundColorSpan = null;
		mSpannableString = null;
		mRect1	= null;
		mRect2	= null;
		mSmoothInterpolator = null;
		errorView = null;
		errorTextView = null;
		errorButton = null;
		
		isDestroyed = true;
	}
}
