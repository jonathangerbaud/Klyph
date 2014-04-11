package com.abewy.android.apps.klyph.fragment;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import com.abewy.android.apps.klyph.KlyphData;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.core.fql.Album;
import com.abewy.android.apps.klyph.core.fql.FriendList;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class NewAlbumDialog extends DialogFragment
{
	private enum Privacy
	{
		EVERYONE,
		ALL_FRIENDS,
		SELF,
		CUSTOM
	}

	private TextView	name;
	private TextView	location;
	private TextView	description;
	private Spinner		privacy;

	public interface NewAlbumDialogListener
	{
		public void createAlbum(Album album, String privacy);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_new_album, null);

		name = (TextView) view.findViewById(R.id.name);
		location = (TextView) view.findViewById(R.id.location);
		description = (TextView) view.findViewById(R.id.description);
		privacy = (Spinner) view.findViewById(R.id.privacy);

		/*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.privacy,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
		
		MultiObjectAdapter adapter = new MultiObjectAdapter(null, SpecialLayout.DROP_DOWN_ITEM);
		FriendList publicFL = new FriendList();
		publicFL.setName(getActivity().getString(R.string.menu_privacy_public));
		FriendList friendsFL = new FriendList();
		friendsFL.setName(getActivity().getString(R.string.menu_privacy_friends));
		FriendList selfFL = new FriendList();
		selfFL.setName(getActivity().getString(R.string.menu_privacy_self));
		
		adapter.add(publicFL);
		adapter.add(friendsFL);
		adapter.add(selfFL);
		
		List<GraphObject> friendLists = KlyphData.getFriendLists();
		if (friendLists != null)
		{
			for (GraphObject graphObject : friendLists)
			{
				adapter.add(graphObject);
			}
		}
		
		privacy.setAdapter(adapter);
		
		privacy.setSelection(KlyphPreferences.getPrivacy());

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(view);
		builder.setTitle(R.string.new_album).setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id)
			{
				createAlbum();
			}
		}).setNegativeButton(R.string.cancel, null);

		return builder.create();
	}

	private void createAlbum()
	{
		Album album = new Album();
		album.setName(name.getText().toString());
		album.setDescription(description.getText().toString());
		album.setLocation(location.getText().toString());

		if (getTargetFragment() != null && getTargetFragment() instanceof NewAlbumDialogListener)
		{
			((NewAlbumDialogListener) getTargetFragment()).createAlbum(album, getPrivacyParam());
		}
	}
	
	private String getPrivacyParam()
	{
		JSONObject json = new JSONObject();
		String privacyString = Privacy.CUSTOM.toString();
		
		int selectedIndex = privacy.getSelectedItemPosition();
		if (selectedIndex == 0)
		{
			privacyString = Privacy.EVERYONE.toString();
		}
		else if (selectedIndex == 1)
		{
			privacyString = Privacy.ALL_FRIENDS.toString();
		}
		else if (selectedIndex == 2)
		{
			privacyString = Privacy.SELF.toString();
		}
		else
		{
			privacyString = Privacy.CUSTOM.toString();
			
			FriendList fl = (FriendList) privacy.getSelectedItem();
			try
			{
				json.put("allow", fl.getFlid());
				json.put("deny", "");
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		
		try
		{
			json.putOpt("value", privacyString);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return json.toString();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		name = null;
		location = null;
		description = null;
		privacy = null;
	}
	
	
}
