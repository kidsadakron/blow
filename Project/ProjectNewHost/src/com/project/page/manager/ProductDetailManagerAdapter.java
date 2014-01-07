package com.project.page.manager;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.R;
import com.project.customs.viewImage;
import com.project.detail.Host;
import com.project.detail.ProductDetail;

public class ProductDetailManagerAdapter extends BaseAdapter {
	Context context;
	ArrayList<ProductDetail> product_data;
	LayoutInflater inflater;

	public ProductDetailManagerAdapter(Context context, ArrayList<ProductDetail> data) {
		this.product_data = new ArrayList<ProductDetail>();
		this.product_data.addAll(data);
		this.context = context;

	}

	private class ViewHolder {

		TextView name;
		TextView pice;
		TextView num;
		TextView type;
		ImageView image;
		LinearLayout layoutManagetItem;
	}

	public void add(ProductDetail Product) {
		// Log.v("AddView", Product.getName());

		this.product_data.add(Product);

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
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.detail_manager_jobid_detail_item, null);
			holder = new ViewHolder();

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.image = (ImageView) convertView.findViewById(R.id.ivManagetItem);
		holder.layoutManagetItem = (LinearLayout) convertView
				.findViewById(R.id.layoutManagetItem);
		holder.name = (TextView) convertView
				.findViewById(R.id.tvManagetItemName);
		holder.num = (TextView) convertView.findViewById(R.id.tvManagetItemNum);
		holder.pice = (TextView) convertView
				.findViewById(R.id.tvManagetItemPice);
		holder.type = (TextView) convertView
				.findViewById(R.id.tvManagetItemType);
		 ProductDetail product = product_data.get(position);
		String image_URL = Host.host + "" + product.getPhotoSmall();
		new DownloadImageTask(holder.image).execute(image_URL);

		holder.name.setText("ชื่อ : " +product.getName());
		holder.type.setText("ประเภท : "+product.getGroup());
		String numAmount = product.getAmount();
		holder.num.setText("จำนวนที่สั่งซื้อ : "+numAmount);
		holder.layoutManagetItem.setBackgroundResource(R.drawable.shape);
		holder.pice.setText("รวมราคา " +numAmount+" ชิ้น : "+(Double.parseDouble(product.getPrice()) * Integer
				.parseInt(numAmount)) + " บาท");
		holder.layoutManagetItem.setOnClickListener(new OnClickListener() {
			ProductDetail status = product_data.get(position);
			@Override
			public void onClick(View v) {
				
				Intent inte = new Intent(context.getApplicationContext(),
						viewImage.class);
				inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				inte.putExtra("MyClass", status);
				inte.putExtra("noAdd", true);
				context.startActivity(inte);
				
			}
		});
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