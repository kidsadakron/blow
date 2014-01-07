package com.project.customs;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.born.classUsing.WagonDatabase;
import com.project.R;
import com.project.detail.StatusDetail;
import com.project.detail.Host;
import com.project.page.MainPage;
import com.project.page.manager.DetailManagerPage;

public class MainPageAdapter extends BaseAdapter {
	private ArrayList<StatusDetail> mList;
	private Context mCtx;
	LayoutInflater inflater;
	StatusDetail status;
	Handler handler;
	ImageView pic;
	String image_URL;

	public MainPageAdapter(Context ctx, ArrayList<StatusDetail> list) {
		this.mCtx = ctx;
		this.mList = list;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	public void addTop(StatusDetail data) {
		mList.add(0, data);
	}

	@Override
	public Object getItem(int position) {

		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		status = mList.get(position);
		View v = null;
		if (status.getStatus() == true) {
			final StatusDetail statusProduct = mList.get(position);
			v = inflater.inflate(R.layout.main_item_status_product, parent,
					false);
			LinearLayout bigRow = (LinearLayout) v.findViewById(R.id.bigRow);
			TextView textName = (TextView) v.findViewById(R.id.name);
			//Button btPlus = (Button) v.findViewById(R.id.plus2);
			ImageView btPlus = (ImageView) v.findViewById(R.id.plus);
			FrameLayout frameImage = (FrameLayout) v
					.findViewById(R.id.framImage);
			pic = (ImageView) v.findViewById(R.id.imageShow);
			TextView textPrice = (TextView) v.findViewById(R.id.Price);
			RelativeLayout reLa = (RelativeLayout) v.findViewById(R.id.reLa);
			textName.setBackgroundResource(R.drawable.shape_in);
			textName.setText(status.getName());
			frameImage.setBackgroundResource(R.drawable.shape_in);
			bigRow.setBackgroundResource(R.drawable.shape);

			textPrice.setText(status.getPrice() + "");
			textPrice.setBackgroundResource(R.drawable.shape_in);
			if (!status.getPhoto().equals("")) {
				image_URL = Host.host + "" + status.getPhoto();
				new DownloadImageTask(pic).execute(image_URL);

				pic.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent inte = new Intent(mCtx.getApplicationContext(),
								viewImage.class);
						inte.putExtra("MyClass", statusProduct);
						mCtx.startActivity(inte);
					}
				});
			} else {
				bigRow.removeViewAt(1);
				reLa.removeViewAt(0);
			}
			btPlus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					final Dialog dialog = new Dialog(mCtx);
					dialog.setContentView(R.layout.main_dialog);
					dialog.setTitle("ระบุจำนวนที่ต้องการ");

					final NumberPicker np = (NumberPicker) dialog
							.findViewById(R.id.np);
					np.setMaxValue(100);
					np.setMinValue(1);
					np.setWrapSelectorWheel(false);
					TextView td = (TextView) dialog.findViewById(R.id.td1);
					td.setText("หน่วย");
					td.setTextSize(20f);
					InputMethodManager imm = (InputMethodManager) mCtx
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(np.getApplicationWindowToken(),
							0);

					Button dialogButton = (Button) dialog
							.findViewById(R.id.dialogButtonOK);
					// if button is clicked, close the custom dialog
					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (!Host.user.equals("")) {
								WagonDatabase wagon = new WagonDatabase();

								String ch = wagon.addItem(Host.user,
										statusProduct.getProductID(),
										np.getValue() + "").trim();
								if (Boolean.parseBoolean(ch)) {
									dialog.dismiss();
									wagon.dialogAddDatabase(mCtx,
											statusProduct.getName());
								} else {
									dialog.dismiss();
									wagon.dialogNoAddDatabase(mCtx);
								}
							} else {
								Intent toRegister = new Intent(mCtx,
										LoginPage.class);
								mCtx.startActivity(toRegister);
							}
							dialog.dismiss();
						}
					});

					dialog.show();

				}
			});
		} else {
			v = inflater.inflate(R.layout.main_item_status_job_idxml, parent,
					false);
			TextView tvMainJob = (TextView) v.findViewById(R.id.tvMainJob);
			LinearLayout llMainJob = (LinearLayout) v
					.findViewById(R.id.llMainJob);
			llMainJob.setBackgroundResource(R.drawable.shape);
			tvMainJob.setText("สถานะของใบเสร็จเลขที่ " + status.getJobID()
					+ " มีการอัฟเดท");
			llMainJob.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mCtx.getApplicationContext(),
							DetailManagerPage.class);
					StatusDetail statusCar = mList.get(position);
					intent.putExtra("Latitude", statusCar.getLatitude());
					intent.putExtra("Longitude", statusCar.getLongitude());
					intent.putExtra("JobID", statusCar.getJobID());
					intent.putExtra("StatusJob", statusCar.getStatusJob());
					mCtx.startActivity(intent);

				}
			});
		}

		return v;
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