/*
 * Original code from Android MultiSelectListPreference
 * Modified to add a drag and drop behavior
 * 
 * Copyright (C) 2010 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.preference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.abewy.android.apps.klyph.R;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

/**
 * A {@link Preference} that displays a list of entries as
 * a dialog.
 * <p>
 * This preference will store a set of strings into the SharedPreferences. This
 * set will contain one or more values from the
 * {@link #setEntryValues(CharSequence[])} array.
 * 
 * @attr ref R.styleable#MultiSelectDragListPreference_entries
 * @attr ref R.styleable#MultiSelectDragListPreference_entryValues
 */
public class MultiSelectDragListPreference extends DialogPreference
{
	private CharSequence[]		mEntries;
	private CharSequence[]		mEntryValues;
	private List<String>		mValues		= new ArrayList<String>();
	private List<String>		mNewValues	= new ArrayList<String>();
	private boolean 				mPreferenceChanged;
	private DragSortListView	listView;

	public MultiSelectDragListPreference(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.MultiSelectDragListPreference, 0, 0);
		mEntries = a.getTextArray(R.styleable.MultiSelectDragListPreference_entries);
		mEntryValues = a.getTextArray(R.styleable.MultiSelectDragListPreference_entryValues);
		a.recycle();
	}

	public MultiSelectDragListPreference(Context context)
	{
		this(context, null);
	}

	/**
	 * Sets the human-readable entries to be shown in the list. This will be
	 * shown in subsequent dialogs.
	 * <p>
	 * Each entry must have a corresponding index in
	 * {@link #setEntryValues(CharSequence[])}.
	 * 
	 * @param entries The entries.
	 * @see #setEntryValues(CharSequence[])
	 */
	public void setEntries(CharSequence[] entries)
	{
		mEntries = entries;
	}

	/**
	 * @see #setEntries(CharSequence[])
	 * @param entriesResId The entries array as a resource.
	 */
	public void setEntries(int entriesResId)
	{
		setEntries(getContext().getResources().getTextArray(entriesResId));
	}

	/**
	 * The list of entries to be shown in the list in subsequent dialogs.
	 * 
	 * @return The list as an array.
	 */
	public CharSequence[] getEntries()
	{
		return mEntries;
	}

	/**
	 * The array to find the value to save for a preference when an entry from
	 * entries is selected. If a user clicks on the second item in entries, the
	 * second item in this array will be saved to the preference.
	 * 
	 * @param entryValues The array to be used as values to save for the
	 *            preference.
	 */
	public void setEntryValues(CharSequence[] entryValues)
	{
		mEntryValues = entryValues;
	}

	/**
	 * @see #setEntryValues(CharSequence[])
	 * @param entryValuesResId The entry values array as a resource.
	 */
	public void setEntryValues(int entryValuesResId)
	{
		setEntryValues(getContext().getResources().getTextArray(entryValuesResId));
	}

	/**
	 * Returns the array of values to be saved for the preference.
	 * 
	 * @return The array of values.
	 */
	public CharSequence[] getEntryValues()
	{
		return mEntryValues;
	}

	/**
	 * Sets the value of the key. This should contain entries in
	 * {@link #getEntryValues()}.
	 * 
	 * @param values The values to set for the key.
	 */
	public void setValues(List<String> values)
	{
		mValues.clear();
		mValues.addAll(values);
		
		persistStringSet(values);
	}

	/**
	 * Attempts to persist a set of Strings to the
	 * {@link android.content.SharedPreferences}.
	 * <p>
	 * This will check if this Preference is persistent, get an editor from the
	 * {@link PreferenceManager}, put in the strings, and check if we should
	 * commit (and commit if so).
	 * 
	 * @param values The values to persist.
	 * @return True if the Preference is persistent. (This is not whether the
	 *         value was persisted, since we may not necessarily commit if there
	 *         will be a batch commit later.)
	 * @see #getPersistedString(Set)
	 * 
	 * @hide Pending API approval
	 */
	protected boolean  persistStringSet(List<String> values)
	{
		if (shouldPersist())
		{
			// Shouldn't store null
			if (values.equals(getPersistedStringSet("")))
			{
				// It's already there, so the same as persisting
				return true;
			}

			SharedPreferences.Editor editor = mPreferenceManager.getSharedPreferences().edit();
			editor.putString(getKey(), StringUtils.join(values, ","));
			editor.commit();
			return true;
		}
		return false;
	}

	/**
	 * Checks whether, at the given time this method is called,
	 * this Preference should store/restore its value(s) into the
	 * {@link SharedPreferences}. This, at minimum, checks whether this
	 * Preference is persistent and it currently has a key. Before you
	 * save/restore from the {@link SharedPreferences}, check this first.
	 * 
	 * @return True if it should persist the value.
	 */
	protected boolean  shouldPersist()
	{
		return mPreferenceManager != null && isPersistent() && hasKey();
	}

	/**
	 * Retrieves the current value of the key.
	 */
	public List<String> getValues()
	{
		return mValues;
	}

	/**
	 * Returns the index of the given value (in the entry values array).
	 * 
	 * @param value The value whose index should be returned.
	 * @return The index of the value, or -1 if not found.
	 */
	public int findIndexOfValue(String value)
	{
		if (value != null && mEntryValues != null)
		{
			for (int i = mEntryValues.length - 1; i >= 0; i--)
			{
				if (mEntryValues[i].equals(value))
				{
					return i;
				}
			}
		}
		return -1;
	}

	private DragSortListView getListView()
	{
		return listView;
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder)
	{
		super.onPrepareDialogBuilder(builder);

		if (mEntries == null || mEntryValues == null)
		{
			throw new IllegalStateException("MultiSelectListPreference requires an entries array and "
											+ "an entryValues array.");
		}

		String[] entries = new String[mEntries.length];
		for (int i = 0; i < mEntries.length; i++)
		{
			entries[i] = mEntries[i].toString();
		}

		boolean [] selectedItems = getSelectedItems();

		ArrayList<String> orderedList = new ArrayList<String>();
		int n = selectedItems.length;
		
		for (String value : mValues)
		{
			int index = ArrayUtils.indexOf(mEntryValues, value);
			orderedList.add(mEntries[index].toString());
		}

		for (int i = 0; i < mEntries.length; i++)
		{
			if (!mValues.contains(mEntryValues[i]))
				orderedList.add(mEntries[i].toString());
		}

		adapter = new ArrayAdapter<String>(getContext(), R.layout.item_list_preference_multi_drag, R.id.text,
				orderedList);
		listView = new DragSortListView(getContext(), null);
		listView.setAdapter(adapter);

		listView.setDropListener(onDrop);
		listView.setDragEnabled(true);
		listView.setFloatAlpha(0.8f);

		DragSortController controller = new DragSortController(listView);
		controller.setDragHandleId(R.id.drag_handle);
		controller.setRemoveEnabled(false);
		controller.setSortEnabled(true);
		controller.setBackgroundColor(0xFFFFFF);
		controller.setDragInitMode(DragSortController.ON_DOWN);

		listView.setFloatViewManager(controller);
		listView.setOnTouchListener(controller);

		
		builder.setView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				mPreferenceChanged = true;
				refreshNewValues();
			}});
		
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		for (int i = 0; i < n; i++)
		{
			listView.setItemChecked(i, i < mValues.size());
		}

		/*
		 * boolean [] checkedItems = getSelectedItems();
		 * builder.setMultiChoiceItems(mEntries, checkedItems,
		 * new DialogInterface.OnMultiChoiceClickListener() {
		 * public void onClick(DialogInterface dialog, int which, boolean 
		 * isChecked) {
		 * if (isChecked) {
		 * mPreferenceChanged |= mNewValues.add(mEntryValues[which].toString());
		 * } else {
		 * mPreferenceChanged |=
		 * mNewValues.remove(mEntryValues[which].toString());
		 * }
		 * }
		 * });
		 */
		mNewValues.clear();
		mNewValues.addAll(mValues);
	}

	private ArrayAdapter<String>			adapter;

	private DragSortListView.DropListener	onDrop	= new DragSortListView.DropListener() {
														@Override
														public void drop(int from, int to)
														{
															if (from != to)
															{
																DragSortListView list = getListView();
																String item = adapter.getItem(from);
																adapter.remove(item);
																adapter.insert(item, to);
																list.moveCheckState(from, to);
																mPreferenceChanged = true;
																refreshNewValues();
															}
														}
													};

	private void refreshNewValues()
	{
		mNewValues.clear();
		int n = adapter.getCount();
		SparseBooleanArray checkedPositions = listView.getCheckedItemPositions();
		for (int i = 0; i < n; i++)
		{
			if (checkedPositions.get(i) == true)
			{
				mNewValues.add((String) mEntryValues[ArrayUtils.indexOf(mEntries, adapter.getItem(i))]);
			}
		}
	}

	private boolean [] getSelectedItems()
	{
		final CharSequence[] entries = mEntryValues;
		final int entryCount = entries.length;
		final List<String> values = mValues;
		boolean [] result = new boolean [entryCount];

		for (int i = 0; i < entryCount; i++)
		{
			result[i] = values.contains(entries[i].toString());
		}

		return result;
	}

	@Override
	protected void onDialogClosed(boolean  positiveResult)
	{
		super.onDialogClosed(positiveResult);

		if (positiveResult && mPreferenceChanged)
		{
			refreshNewValues();
			final List<String> values = mNewValues;
			if (callChangeListener(values) && values.size() > 0)
			{
				setValues(values);
			}
		}
		mPreferenceChanged = false;
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index)
	{
		final CharSequence[] defaultValues = a.getTextArray(index);
		final int valueCount = defaultValues.length;
		final List<String> result = new ArrayList<String>();

		for (int i = 0; i < valueCount; i++)
		{
			result.add(defaultValues[i].toString());
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onSetInitialValue(boolean  restoreValue, Object defaultValue)
	{
		List<String> v = (List<String>) defaultValue;
		String defValue = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getKey(), StringUtils.join(v.toArray(), ","));
		setValues(restoreValue ? getPersistedStringSet(StringUtils.join(mValues)) : Arrays.asList(defValue.split(",")));
	}

	PreferenceManager	mPreferenceManager;

	protected List<String> getPersistedStringSet(String defaultReturnValue)
	{
		if (!shouldPersist())
		{
			return Arrays.asList(defaultReturnValue.split(","));
		}

		return Arrays.asList(mPreferenceManager.getSharedPreferences().getString(getKey(), defaultReturnValue)
				.split(","));
	}

	protected void onAttachedToHierarchy(PreferenceManager preferenceManager)
	{
		super.onAttachedToHierarchy(preferenceManager);
		mPreferenceManager = preferenceManager;
	}

	@Override
	protected Parcelable onSaveInstanceState()
	{
		final Parcelable superState = super.onSaveInstanceState();
		if (isPersistent())
		{
			// No need to save instance state
			return superState;
		}

		final SavedState myState = new SavedState(superState);
		myState.values = StringUtils.join(getValues(), ",");
		return myState;
	}

	private static class SavedState extends BaseSavedState
	{
		String	values;

		public SavedState(Parcel source)
		{
			super(source);
			values = source.readString();
		}

		public SavedState(Parcelable superState)
		{
			super(superState);
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			super.writeToParcel(dest, flags);
			dest.writeString(values);
		}

		public static final Parcelable.Creator<SavedState>	CREATOR	= new Parcelable.Creator<SavedState>() {
																		public SavedState createFromParcel(Parcel in)
																		{
																			return new SavedState(in);
																		}

																		public SavedState[] newArray(int size)
																		{
																			return new SavedState[size];
																		}
																	};
	}
}
