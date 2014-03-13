package com.speed.flickrsearchr;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class PhotoAdapter extends ArrayAdapter<Photo> {

	Context context;
	int layoutResourceId;
	ArrayList<Photo> data = null;

	public PhotoAdapter(Context context, int layoutResourceId,
			ArrayList<Photo> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	public void setData(ArrayList<Photo> data) {
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PhotoHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new PhotoHolder();
			holder.imgIcon = (ImageView) row.findViewById(R.id.image_thumbnail);
			holder.txtTitle = (TextView) row.findViewById(R.id.image_name);

			row.setTag(holder);
		} else {
			holder = (PhotoHolder) row.getTag();
		}

		Photo photo = data.get(position);
		holder.txtTitle.setText(photo.title);
		// holder.imgIcon.setImageResource(photo.photoRes);

		if (!data.get(position).id.equals("test")) {

			// get url for thumbnail
			String url = Utils.constructUrl(data.get(position), "t");

			DisplayImageOptions d = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisc(true)
					.showImageOnLoading(R.drawable.placeholder)
					.displayer(new FadeInBitmapDisplayer(300)).build();
			ImageLoader.getInstance().displayImage(url, holder.imgIcon, d,
					new MyImageLoadingListener(context));
		}

		return row;
	}

	static class PhotoHolder {
		ImageView imgIcon;
		TextView txtTitle;
		ProgressBar prog;
	}
}