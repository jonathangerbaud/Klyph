package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.KlyphApplication;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.fql.Album;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.fql.Photo.Image;
import com.abewy.android.apps.klyph.core.fql.Profile;
import com.abewy.android.apps.klyph.core.fql.User;
import com.abewy.android.apps.klyph.core.fql.serializer.AlbumDeserializer;
import com.abewy.android.apps.klyph.core.fql.serializer.PhotoDeserializer;
import com.abewy.android.apps.klyph.core.fql.serializer.ProfileDeserializer;
import com.abewy.android.apps.klyph.core.fql.serializer.UserDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class ElementAlbumRequest extends KlyphQuery
{
	private String	id;

	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		this.id = id;

		String query1 = "SELECT aid, backdated_time, can_backdate, can_upload, comment_info, cover_object_id, cover_pid, created, description, edit_link, like_info, link, location, modified, modified_major, name, object_id, owner, owner_cursor, photo_count, place_id, type, video_count, visible";
		query1 += " FROM album WHERE (photo_count > 0 OR video_count > 0) AND owner = " + id;

		if (isOffsetDefined(offset))
		{
			query1 += " AND owner_cursor > \"" + offset + "\"";
		}

		query1 += " LIMIT 25";

		String query2 = "SELECT pid, images from photo where pid in (select cover_pid from #query1 WHERE strlen(cover_pid) > 0)";

		if (isOffsetDefined(offset))
		{
			return multiQuery(query1, query2);
		}

		String query3 = "select pid, object_id, images from photo where object_id in (select object_id from photo_tag where subject = " + id
						+ ") LIMIT 1000";

		String query4 = "SELECT uid, first_name, name FROM user where uid = \"" + id + "\"";
		String query5 = "SELECT id, name FROM profile where id = " + id;

		String query6 = "SELECT vid, thumbnail_link, owner FROM video WHERE owner = " + id;

		return multiQuery(query1, query2, query3, query4, query5, query6);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray data = result[0];
		JSONArray photos = result[1];

		assocData(data, photos, "cover_pid", "pid", "cover_images", "images");

		int n = 25;

		Album taggedAlbum = null;
		Album videoAlbum = null;

		if (result.length == 6)
		{
			JSONArray tagged = result[2];
			JSONArray user = result[3];
			JSONArray profile = result[4];

			PhotoDeserializer pd = new PhotoDeserializer();
			List<GraphObject> taggedPhotos = pd.deserializeArray(tagged);

			int nt = taggedPhotos.size();
			if (nt > 0)
			{
				taggedAlbum = new Album();
				taggedAlbum.setOwner(id);
				taggedAlbum.setPhoto_count(nt);
				taggedAlbum.setVideo_count(0);
				taggedAlbum.setIsTaggedAlbum(true);
				taggedAlbum.setIs_video_album(false);

				String eName = "";
				if (user.length() > 0)
				{
					UserDeserializer ud = new UserDeserializer();
					User u = (User) ud.deserializeArray(user).get(0);
					eName = u.getFirst_name().length() > 0 ? u.getFirst_name() : u.getName();
				}
				else if (profile.length() > 0)
				{
					ProfileDeserializer prd = new ProfileDeserializer();
					Profile p = (Profile) prd.deserializeArray(profile).get(0);
					eName = p.getName();
				}
				taggedAlbum.setName(KlyphApplication.getInstance().getString(R.string.tagged_photos_of, eName));

				Photo photo = (Photo) taggedPhotos.get(0);
				taggedAlbum.setCover_pid(photo.getPid());
				taggedAlbum.setCover_images(photo.getImages());
			}

			JSONArray videos = result[5];

			if (videos.length() > 0)
			{
				videoAlbum = new Album();
				videoAlbum.setOwner(id);
				videoAlbum.setPhoto_count(nt);
				videoAlbum.setVideo_count(0);
				videoAlbum.setIsTaggedAlbum(false);
				videoAlbum.setIs_video_album(true);

				for (int i = 0; i < videos.length(); i++)
				{
					JSONObject v = videos.optJSONObject(i);

					if (v != null && v.optString("thumbnail_link") != null)
					{
						Image cover = new Photo.Image();
						cover.setSource(v.optString("thumbnail_link"));

						List<Image> images = new ArrayList<Photo.Image>();
						images.add(cover);

						videoAlbum.setOwner(v.optString("owner"));
						videoAlbum.setCover_images(images);
						break;
					}
				}
			}
		}

		AlbumDeserializer deserializer = new AlbumDeserializer();

		ArrayList<GraphObject> albums = (ArrayList<GraphObject>) deserializer.deserializeArray(data);

		if (videoAlbum != null)
			albums.add(0, videoAlbum);
		
		if (taggedAlbum != null)
			albums.add(0, taggedAlbum);

		setHasMoreData(albums.size() >= n);

		return albums;
	}
}
