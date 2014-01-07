package com.project.page.product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.born.classUsing.WagonDatabase;
import com.project.R;
import com.project.customs.ProductAdapter;
import com.project.customs.LoginPage;
import com.project.customs.viewImage;
import com.project.detail.ProductDetail;
import com.project.detail.Host;
import com.project.json.GetJson;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class ProductCustoms extends Fragment {
	GridView listAll;
	String url = Host.host + "/Productnew";
	ArrayList<ProductDetail> productList;
	final GetJson gJ = new GetJson();
	int cout = 1;
	boolean loadingMore = false;
	ProductAdapter adapter;
	private String type;
	NumberPicker np;
	Boolean isLastStatus = true;
	String lastKey = "";
	int mFirstVisibleItem = 0;
	int mVisibleItemCount = 0;
	int mTotalItemCount = 0;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		type = getArguments().getString("type");
		productList = new ArrayList<ProductDetail>();
		// productList = getArguments().getSerializable("data1");
		String temp = getArguments().getString("data");
		try {

			if (!temp.equals("1")) {

				JSONArray data2 = new JSONArray(temp);
				ArrayList<ProductDetail> dataTemp = new ArrayList<ProductDetail>();
				for (int i = 0; i < data2.length(); i++) {
					ProductDetail item = getProductDetail(data2
							.getJSONObject(i));

					dataTemp.add(item);
					productList.add(item);
				}
				lastKey = productList.get(productList.size() - 1)
						.getProductID();
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		adapter = new ProductAdapter(getActivity(), productList);
		listAll.setAdapter(adapter);

		listAll.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				mFirstVisibleItem = firstVisibleItem;
				mVisibleItemCount = visibleItemCount;
				mTotalItemCount = totalItemCount;
				int lastInScreen = firstVisibleItem + visibleItemCount;
				Log.v("test", lastInScreen + "," + (totalItemCount - 10));

				if ((lastInScreen >= totalItemCount - 10) && !(loadingMore)) {
					loadingMore = true;
					if (isLastStatus) {
						Log.v("type", lastKey);
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("LastKey", lastKey));
						params.add(new BasicNameValuePair("Type", type));
						params.add(new BasicNameValuePair("bID", Host.bID));
						new DownloadJsonTask().execute(url, params, true);

					}
				}
			}

		});
		listAll.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Animation inAnimation = AnimationUtils.loadAnimation(
						getActivity(), R.anim.dialog_product_in);
				// When clicked, show a toast with the TextView text
				final ProductDetail product = (ProductDetail) parent
						.getItemAtPosition(position);
				final Dialog dialog = new Dialog(getActivity());
				dialog.setContentView(R.layout.product_dialog);
				dialog.setTitle("เลือกสิ่งที่คุณต้องการ");

				np = (NumberPicker) dialog.findViewById(R.id.np);
				np.setMaxValue(100);
				np.setMinValue(1);
				np.setWrapSelectorWheel(false);
				TextView td = (TextView) dialog.findViewById(R.id.td1);
				Button selectBuy = (Button) dialog.findViewById(R.id.seBuy);

				Button detailItem = (Button) dialog.findViewById(R.id.seDetail);
				detailItem.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent inte = new Intent(getActivity()
								.getApplicationContext(), viewImage.class);
						inte.putExtra("MyClass", product);
						startActivity(inte);

					}
				});
				Button ok = (Button) dialog.findViewById(R.id.dialogButtonOK);
				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!Host.user.equals("")) {

							WagonDatabase wagon = new WagonDatabase();
							String ch = wagon.addItem(Host.user,
									product.getProductID(), np.getValue() + "").trim();
							if (Boolean.parseBoolean(ch)) {
								dialog.dismiss();
								wagon.dialogAddDatabase(getActivity(),
										product.getName());
							}else {
								dialog.dismiss();
								wagon.dialogNoAddDatabase(getActivity());
							}
						} else {
							Intent toRegister = new Intent(getActivity(),
									LoginPage.class);
							startActivity(toRegister);
						}

					}
				});
				final LinearLayout seLayout = (LinearLayout) dialog
						.findViewById(R.id.se_layout);

				selectBuy.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						seLayout.startAnimation(inAnimation);

					}
				});
				inAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

						seLayout.setVisibility(View.VISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {

					}
				});
				td.setText("หน่วย");
				td.setTextSize(20f);
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(np.getApplicationWindowToken(), 0);
				dialog.show();
			}
		});

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.product_show, container,
				false);
		listAll = (GridView) rootView.findViewById(R.id.gridView1);

		return rootView;
	}

	class DownloadJsonTask extends AsyncTask<Object, Void, String> {
		ProgressDialog doalogProgress;
		StringBuilder str;
		HttpClient client;
		HttpPost httpPost;
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@Override
		protected String doInBackground(Object... urls) {
			httpPost = new HttpPost((String) urls[0]);
			client = new DefaultHttpClient();
			str = new StringBuilder();
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(
						(List<? extends NameValuePair>) urls[1], HTTP.UTF_8));
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
					Log.v("download", "NO");
					return "1";
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String a = Html.fromHtml(str.toString()).toString();
			loadingMore = true;

			if (a.trim().equals("")) {
				Log.v("download", "NO");
				return "1";
			}
			if (a.charAt(0) == ']') {
				Log.v("download", "NO");
				return "1";
			} else {
				Log.v("download", "OK");
				return a.toString();
			}

		}

		protected void onPostExecute(String result) {

			try {

				if (result.equals("1")) {
					isLastStatus = false;
				} else {
					JSONArray data2 = new JSONArray(result);

					for (int i = 0; i < data2.length(); i++) {
						ProductDetail item = getProductDetail(data2

						.getJSONObject(i));

						productList.add(item);

						if (data2.length() - 1 == i) {

							lastKey = item.getProductID();

						}

					}
				}
			} catch (JSONException e) {

				e.printStackTrace();
			}

			adapter.notifyDataSetChanged();

			loadingMore = false;

		}

	}

	public ProductDetail getProductDetail(JSONObject jsonObject) {
		ProductDetail statusDetail = new ProductDetail();

		try {
			statusDetail.setAmount(jsonObject.get("Amount").toString());
			statusDetail.setName(jsonObject.get("Name").toString());
			statusDetail.setPhoto(jsonObject.get("Photo").toString());
			statusDetail.setBrand(jsonObject.get("Brand").toString());
			statusDetail.setGroup(jsonObject.get("Group").toString());
			statusDetail.setKind(jsonObject.get("Kind").toString());
			statusDetail.setProductID(jsonObject.get("ProductID").toString());
			statusDetail.setPrice(jsonObject.get("Price").toString());
			statusDetail.setPhotoSmall(jsonObject.get("Picturesmall")
					.toString());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return statusDetail;

	}
}
