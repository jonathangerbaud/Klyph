package com.abewy.android.apps.klyph.core.request;

public class RequestError
{
	private int errorCode;
	private String type;
	private String message;
	
	public RequestError(int errorCode, String type, String message)
	{
		this.errorCode = errorCode;
		this.type = type;
		this.message = message;
	}

	public int getErrorCode()
	{
		return errorCode;
	}

	public String getType()
	{
		return type;
	}

	public String getMessage()
	{
		return message;
	}
	
	public boolean isError()
	{
		return errorCode != 0;
	}
	
	@Override
	public String toString()
	{
		return errorCode + " " + type + " " + message;
	}
}
