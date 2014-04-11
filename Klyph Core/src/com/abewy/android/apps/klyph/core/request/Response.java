package com.abewy.android.apps.klyph.core.request;

import java.util.List;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Response
{
	private String				returnedId;
	private RequestError		error;
	private GraphObject			graphObject;
	private List<GraphObject>	graphObjectList;

	public Response(String id)
	{
		this.returnedId = id;
	}

	public Response(RequestError error, GraphObject graphObject)
	{
		this(error, graphObject, null);
	}

	public Response(RequestError error, List<GraphObject> graphObjectList)
	{
		this(error, null, graphObjectList);
	}

	public Response(RequestError error, GraphObject graphObject, List<GraphObject> graphObjectList)
	{
		this.error = error;
		this.graphObject = graphObject;
		this.graphObjectList = graphObjectList;
	}

	public String getReturnedId()
	{
		return returnedId;
	}

	public RequestError getError()
	{
		return error;
	}

	public GraphObject getGraphObject()
	{
		return graphObject;
	}

	public List<GraphObject> getGraphObjectList()
	{
		return graphObjectList;
	}

}
