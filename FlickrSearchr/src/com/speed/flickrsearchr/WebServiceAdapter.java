package com.speed.flickrsearchr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class WebServiceAdapter {

	public static JSONObject getJSON(String query, Context c) {

		query = query.replace(' ', '+');

		String url = String
				.format("http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=%s&text=%s&format=json&nojsoncallback=1",
						c.getString(R.string.api_key), query);
		// API key: "bd29e274e9626faabb31ab494ba6c84b",

		//Log.d("flik", url);

		if (hasConnection(c)) {
			return retrieveInfo(url);
		}

		return null;
	}

	private static boolean hasConnection(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}

		return false;
	}

	private static JSONObject retrieveInfo(String url) {

		StringBuilder sb = new StringBuilder();
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();

			request.setURI(new URI(url));

			HttpResponse response = client.execute(request);

			InputStream ips = response.getEntity().getContent();
			BufferedReader buf = new BufferedReader(new InputStreamReader(ips,
					"UTF-8"));

			String s;
			while (true) {
				s = buf.readLine();
				if (s == null || s.length() == 0)
					break;
				sb.append(s);

			}
			buf.close();
			ips.close();
			// return sb.toString();retrieveInfo

		} catch (Exception e) {
			System.out.println("0");
		} finally {
			// any cleanup code...
		}

		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(sb.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObj;
	}

}
