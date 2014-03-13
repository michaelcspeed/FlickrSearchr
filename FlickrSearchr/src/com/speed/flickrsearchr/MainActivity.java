package com.speed.flickrsearchr;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends ListActivity {

	private static final int NO_OF_PHOTOS_TO_LOAD = 50;
	ArrayList<Photo> photoData = new ArrayList<Photo>();
	private PhotoAdapter adapter;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_main);

		//Add a blank row to the list
		photoData.add(new Photo("", "test",
				"test", "test", "test", R.drawable.ic_launcher));

		adapter = new PhotoAdapter(this, R.layout.photo_list_item, photoData);

		setListAdapter(adapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		url = Utils.constructUrl(photoData.get(position), "z");

		Intent openPictureIntent = new Intent(MainActivity.this, BigPictureActivity.class);
		openPictureIntent.putExtra("url", url); 
		MainActivity.this.startActivity(openPictureIntent);
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
		searchView.setIconifiedByDefault(true);

		SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {

				//Hide help text and show list				
				findViewById(R.id.goAheadText).setVisibility(View.GONE);
								
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

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					setProgressBarIndeterminateVisibility(true);
				}
			});

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

			for (int i = 0; i < NO_OF_PHOTOS_TO_LOAD; i++) {

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
			
			//Show the list
			getListView().setVisibility(View.VISIBLE);

			setProgressBarIndeterminateVisibility(false);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}