package com.speed.flickrsearchr;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

public class MainActivity extends ListActivity {

	ArrayList<Photo> photoData = new ArrayList<Photo>();
	private PhotoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		photoData.add(new Photo("Go ahead and search for some images.", "test",
				"test", "test", "test", R.drawable.ic_launcher));

		adapter = new PhotoAdapter(this, R.layout.photo_list_item, photoData);

		setListAdapter(adapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();

		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);

		SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {
				// this is your adapter that will be filtered
				// getListFilter().filter(newText);
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				// this is your adapter that will be filtered
				// adapter.getFilter().filter(query);

				new DownloadImagesTask().execute(query);

				return true;
			}
		};
		searchView.setOnQueryTextListener(queryTextListener);

		return super.onCreateOptionsMenu(menu);

	}

	private class DownloadImagesTask extends
			AsyncTask<String, Void, JSONObject> {

		protected void onPostExecute(JSONObject result) {

			createPhotoObjects(result);

		}

		@Override
		protected JSONObject doInBackground(String... query) {

			JSONObject json = WebServiceAdapter.getJSON(query[0],
					getApplicationContext());

			return json;
		}

	}

	public void createPhotoObjects(JSONObject result) {

		ArrayList<Photo> photos = new ArrayList<Photo>();

		try {
			JSONObject photosObj = result.getJSONObject("photos");
			JSONArray photosArray = photosObj.getJSONArray("photo");

			for (int i = 0; i < photosArray.length(); i++) {

				String title = photosArray.getJSONObject(i).getString("title");
				String secret = photosArray.getJSONObject(i)
						.getString("secret");
				String farmid = photosArray.getJSONObject(i).getString("farm");
				String serverid = photosArray.getJSONObject(i).getString(
						"server");
				String id = photosArray.getJSONObject(i).getString("id");
				int photoRes = R.drawable.ic_launcher;

				photos.add(new Photo(title, farmid, serverid, id, secret,
						photoRes));

			}
			
			adapter.clear();
			adapter.addAll(photos);
			adapter.setNotifyOnChange(true);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}