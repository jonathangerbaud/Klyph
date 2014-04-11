/**
* @author Jonathan
*/

package com.abewy.android.apps.klyph;

import java.util.ArrayList;
import java.util.List;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class KlyphData
{
	private static List<GraphObject> lastStreams;
	private static List<GraphObject> allPhotos;
	private static List<GraphObject> friendLists;
	
	public static void setLastStreams(List<GraphObject> lastStreams)
	{
		KlyphData.lastStreams = lastStreams;
	}
	
	public static List<GraphObject> getLastStreams()
	{
		return lastStreams;
	}

	public static List<GraphObject> getAllPhotos()
	{
		return allPhotos;
	}

	public static void setAllPhotos(List<GraphObject> allPhotos)
	{
		KlyphData.allPhotos = allPhotos;
	}

	public static List<GraphObject> getFriendLists()
	{
		if (friendLists != null)
		{
			List<GraphObject> list = new ArrayList<GraphObject>();
			list.addAll(friendLists);
			return list;
		}
		
		return friendLists;
	}

	public static void setFriendLists(List<GraphObject> friendLists)
	{
		KlyphData.friendLists = friendLists;
	}
	
	public static void reset()
	{
		lastStreams = null;
		allPhotos = null;
		friendLists = null;
	}
}
