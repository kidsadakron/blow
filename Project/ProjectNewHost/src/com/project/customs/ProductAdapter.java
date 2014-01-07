package com.project.customs;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.project.R;
import com.project.detail.Host;
import com.project.detail.ProductDetail;

public class ProductAdapter extends BaseAdapter {
	Context context;
	ArrayList<ProductDetail> product_data;
	LayoutInflater inflater;

	public ProductAdapter(Context context,
			ArrayList<ProductDetail> data) {
		this.product_data = data;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private class ViewHolder {

		TextView name;
		TextView pice;
		ImageView image;
		LinearLayout ll;
	}

	@Override
	public int getCount() {
		return product_data.size();
	}

	@Override
	public Object getItem(int position) {
		return product_data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
		
			convertView = inflater.inflate(R.layout.product_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.imageItem);
			holder.name = (TextView) convertView
					.findViewById(R.id.nameItem);
			holder.pice = (TextView) convertView
					.findViewById(R.id.priceItem);
			holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ProductDetail product = this.product_data.get(position);

		holder.name.setText(product.getName());
		holder.pice.setText(product.getPrice() + " บาท");
	//	ImageLoader imgLoader = new ImageLoader(
	//			context.getApplicationContext());
	//	int loader = R.drawable.w;
		String url = Host.host + product.getPhoto();
	//	holder.image.setImageResource(loader);
		//imgLoader.DisplayImage(url, loader, holder.image);
		new DownloadImageTask(holder.image).execute(url);
		holder.ll.setBackgroundResource(R.drawable.shape);
		return convertView;

	}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
}