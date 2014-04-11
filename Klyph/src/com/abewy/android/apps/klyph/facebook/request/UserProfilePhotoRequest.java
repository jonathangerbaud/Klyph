package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.fql.Photo.Image;
import com.abewy.android.apps.klyph.core.fql.ProfilePic;
import com.abewy.android.apps.klyph.core.fql.serializer.PhotoDeserializer;
import com.abewy.android.apps.klyph.core.fql.serializer.ProfilePicDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class UserProfilePhotoRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT object_id, images " + "FROM photo "
						+ "WHERE album_object_id IN (SELECT object_id from album WHERE owner = " + id
						+ " AND type = \"profile\") LIMIT 500";

		String query2 = "SELECT id, url FROM profile_pic WHERE id = " + id;

		return multiQuery(query1, query2);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		PhotoDeserializer deserializer = new PhotoDeserializer();
		ArrayList<GraphObject> photos = (ArrayList<GraphObject>) deserializer.deserializeArray(result[0]);
		Log.d("UserProfileRequest", "photos " + photos.size());
		ProfilePicDeserializer ppDeserializer = new ProfilePicDeserializer();
		ArrayList<GraphObject> pictures = (ArrayList<GraphObject>) ppDeserializer.deserializeArray(result[1]);
		Log.d("UserProfileRequest", "pictures " + pictures.size());
		ArrayList<GraphObject> selectedPhoto = new ArrayList<GraphObject>();

		if (pictures.size() > 0)
		{
			ProfilePic pic = (ProfilePic) pictures.get(0);
			String url = pic.getUrl();
			url = url.substring(url.indexOf("_") + 1);
			url = url.substring(0, url.lastIndexOf("_"));

			for (GraphObject graphObject : photos)
			{
				Photo photo = (Photo) graphObject;

				for (Image image : photo.getImages())
				{
					String imageUrl = image.getSource();
					imageUrl = imageUrl.substring(imageUrl.indexOf("_") + 1);
					imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf("_"));
					Log.d("UserProfileRequest", url + " " + imageUrl);
					if (url.equals(imageUrl))
					{
						selectedPhoto.add(photo);
						Log.d("UserProfileRequest", "id " + photo.getObject_id());
						setHasMoreData(false);
						return selectedPhoto;
					}
				}
			}
		}

		setHasMoreData(false);

		return selectedPhoto;
	}
}
