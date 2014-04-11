package com.abewy.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import android.net.http.AndroidHttpClient;
import android.text.Html;
import android.util.Log;

public class HttpRequest2 {
	
	public static boolean HTML_TRANSFORM = true;
	public static boolean LOG_RESPONSE = false;
	
	private AndroidHttpClient client;
	private String url;
	private HashMap<String, String> params;
	
	public HttpRequest2(String url) 
	{
		this.url = url;
	}
	
	public HttpRequest2(String url, HashMap<String, String> params) 
	{
		this.url = url;
		this.params = params;
	}
	
	public String send() 
	{
		client = AndroidHttpClient.newInstance("Android");
	    HttpPost request = new HttpPost(url);
	    
	    if (params != null) 
	    {
	    	List<NameValuePair> postParams = new ArrayList<NameValuePair>();
	    	
	    	for (Map.Entry<String, String> entry : params.entrySet()) 
	    	{
	    		postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
	    	
	    	try {
		    	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams);
		        entity.setContentEncoding(HTTP.UTF_8);
		        request.setEntity(entity);
		    } catch (UnsupportedEncodingException e) {
		        Log.e("", "UnsupportedEncodingException: " + e);
		    }
	    }

    	ResponseHandler<String> responseHandler=new BasicResponseHandler();
    	
    	String response = "";
		
    	try {
    		response = client.execute(request, responseHandler);
    	}
    	catch (ClientProtocolException e) {
    		Log.e("HttpRequest2", e.toString());
    	}
    	catch (IOException e) {
    		Log.e("HttpRequest2", e.toString());
    	}
    	
    	if (LOG_RESPONSE == true)
			Log.i("HttpRequest2", response);
    	
    	if (HTML_TRANSFORM == true)
    		response = Html.fromHtml(response).toString();
    	
    	close();
    	
    	return response;
	}
	
	public void close() {
		client.close();
	}
}
