package com.project.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.text.Html;
import android.util.Log;

public class GetJson {

	public String getHttpPost(String url, List<NameValuePair> params) {

		StringBuilder str = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpResponse response = client.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) { // Status OK
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));

				String line;
				while ((line = reader.readLine()) != null) {
					str.append(line + "\n");
				}
			} else {
				return "1";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String a = Html.fromHtml(str.toString()).toString();
		
		if (a.trim().equals("")) {
			return "1";
		}
		if (a.charAt(0) == ']') {
			return "1";
		} else {
			return a.toString(); 
		}
	}

	public String getHttpPost(String url) {

		String a = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		try {

			HttpGet httpget = new HttpGet(url);
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream is = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {

						sb.append(line + "\n");

					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				a = Html.fromHtml(sb.toString()).toString();
				int b = a.indexOf("Web hosting by Somee.com");
				a = a.substring(0, b - 1);
			}
		} catch (Exception e) {
			Log.e("Error", e.toString());
		}
		return a;
	}
}
