package com.versionone.selenium;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class V1HttpClient {
	
	private static final String USER_AGENT = "clarity-versionone-connector/1.0";
	
	public static String executePostQuery(String url, String body, String contentType) throws ClientProtocolException, IOException {
		
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type", contentType);
		StringEntity xmlPayload = new StringEntity(body);
		post.setEntity(xmlPayload);
		HttpResponse response = httpClient.execute(post);
		
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity, "UTF-8");
	}
	
	public static String executePostQuery(String url, String body, String username, String password, String contentType) throws ClientProtocolException, IOException {
		
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		
		String authString = username + ":" + password;
		byte[] authEncodedBytes = Base64.encodeBase64(authString.getBytes());
		String authEncodedString = new String(authEncodedBytes);
		
		post.setHeader("Authorization", "Basic " + authEncodedString);
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type", contentType);
		StringEntity xmlPayload = new StringEntity(body);
		post.setEntity(xmlPayload);
		HttpResponse response = httpClient.execute(post);
		
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity, "UTF-8");
	}
	
	public static String executeGetQuery(String url, String username, String password) throws ClientProtocolException, IOException {
		
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		
		String authString = username + ":" + password;
		byte[] authEncodedBytes = Base64.encodeBase64(authString.getBytes());
		String authEncodedString = new String(authEncodedBytes);
		
		get.setHeader("Authorization", "Basic " + authEncodedString);
		get.setHeader("User-Agent", USER_AGENT);
		HttpResponse response = httpClient.execute(get);
		
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity, "UTF-8");
	}	

}
