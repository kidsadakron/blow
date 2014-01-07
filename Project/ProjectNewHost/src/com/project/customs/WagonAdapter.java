package com.project.customs;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.born.classUsing.WagonDatabase;
import com.project.MainActivity;
import com.project.R;
import com.project.detail.Host;
import com.project.detail.ProductDetail;
import com.project.detail.StatusDetail;

public class WagonAdapter extends BaseAdapter {

	private ArrayList<ProductDetail> mList;
	private Context mCtx;
	LayoutInflater inflater;
	StatusDetail status;
	String image_URL;

	NumberPicker np;
	Dialog dialog;

	public WagonAdapter(Context context, ArrayList<ProductDetail> data) {
		this.mCtx = context;
		this.mList = data;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	private class ViewHolder {
		ImageView imageWagon;
		Button editWagon;
		TextView name;
		TextView type;
		TextView num;
		TextView pice;
		LinearLayout lv;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(R.layout.wagon_item, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.editWagon = (Button) v.findViewById(R.id.btEdit);
		holder.name = (TextView) v.findViewById(R.id.tvWagonName);
		holder.type = (TextView) v.findViewById(R.id.tvWagonType);
		holder.num = (TextView) v.findViewById(R.id.tvWagonNum);
		holder.lv = (LinearLayout) v.findViewById(R.id.lvWagonItem);
		holder.lv.setBackgroundResource(R.drawable.shape);
		holder.pice = (TextView) v.findViewById(R.id.tvWagonPice);
		holder.imageWagon = (ImageView) v.findViewById(R.id.ivWagonImage);
		ProductDetail product = mList.get(position);

		image_URL = Host.host + "" + product.getPhotoSmall();

		new DownloadImageTask(holder.imageWagon).execute(image_URL);

		holder.editWagon.setOnClickListener(new OnClickListener() {
			ProductDetail productTemp = mList.get(position);

			@Override
			public void onClick(View v) {
				dialog = new Dialog(mCtx);
				dialog.setContentView(R.layout.main_dialog);
				dialog.setTitle("ระบุจำนวนที่ต้องการ");
				np = (NumberPicker) dialog.findViewById(R.id.np);
				np.setMaxValue(100);
				np.setMinValue(1);
				np.setWrapSelectorWheel(false);
				Log.v("s", productTemp.getAmount());
				np.setValue(Integer.parseInt(productTemp.getAmount()));
				TextView td = (TextView) dialog.findViewById(R.id.td1);
				td.setText("หน่วย");
				td.setTextSize(20f);
				InputMethodManager imm = (InputMethodManager) mCtx
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(np.getApplicationWindowToken(), 0);

				Button dialogButton = (Button) dialog
						.findViewById(R.id.dialogButtonOK);
				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!Host.user.equals("")) {
							WagonDatabase wagon = new WagonDatabase();
							String ch = wagon.editAmountItem(Host.user,
									productTemp.getProductID(), np.getValue()
											+ "").trim();
							if (Boolean.parseBoolean(ch)) {
								Intent toWagon = new Intent(mCtx,
										MainActivity.class);
								toWagon.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								toWagon.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								toWagon.putExtra("Page", 3);
								mCtx.startActivity(toWagon);
								dialog.dismiss();
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
		holder.name.setText("ชื่อ : " + product.getName());
		holder.type.setText("ประเภท : " + product.getGroup());
		String numAmount = product.getAmount();
		holder.num.setText("จำนวนที่สั่งซื้อ : " + numAmount);
		holder.pice.setText("รวมราคา "
				+ numAmount
				+ " ชิ้น : "
				+ (Double.parseDouble(product.getPrice()) * Integer
						.parseInt(numAmount)) + " บาท");
		holder.imageWagon.setOnClickListener(new OnClickListener() {
			ProductDetail status = mList.get(position);

			@Override
			public void onClick(View v) {

				Intent inte = new Intent(mCtx.getApplicationContext(),
						viewImage.class);
				inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				inte.putExtra("MyClass", status);
				inte.putExtra("noAdd", true);
				mCtx.startActivity(inte);

			}
		});
		return v;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
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
