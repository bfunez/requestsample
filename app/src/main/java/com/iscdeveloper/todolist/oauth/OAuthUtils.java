package com.iscdeveloper.todolist.oauth;


import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OAuthUtils {
	public static final MediaType JSON
			= MediaType.parse("application/json; charset=utf-8");

	public static OkHttpClient client = new OkHttpClient();

	public static ApiResponse sendPost (String url, String json) throws IOException {

		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder()
				.url(url)
				.cacheControl(CacheControl.FORCE_NETWORK)
				.post(body)
				.build();
		Response response = client.newCall(request).execute();
		ApiResponse res = new ApiResponse();
		res.success = response.isSuccessful();
		res.message = response.body().string();
		return res;
	}

	public static ApiResponse sendPut (String url, String json) throws IOException {

		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder()
				.url(url)
				.cacheControl(CacheControl.FORCE_NETWORK)
				.put(body)
				.build();
		Response response = client.newCall(request).execute();
		ApiResponse res = new ApiResponse();
		res.success = response.isSuccessful();
		res.message = response.body().string();
		return res;
	}

	public static ApiResponse sendDelete (String url, String json) throws IOException {

		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder()
				.url(url)
				.cacheControl(CacheControl.FORCE_NETWORK)
				.delete(body)
				.build();
		Response response = client.newCall(request).execute();
		ApiResponse res = new ApiResponse();
		res.success = response.isSuccessful();
		res.message = response.body().string();
		return res;
	}

	public static ApiResponse sendGet (String url) throws IOException {
		Request request = new Request.Builder()
				.url(url)
				.cacheControl(CacheControl.FORCE_NETWORK)
				.get()
				.build();
		Response response = client.newCall(request).execute();
		ApiResponse res = new ApiResponse();
		res.success = response.isSuccessful();
		res.message = response.body().string();
		return res;
	}
}
