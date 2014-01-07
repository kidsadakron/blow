package com.project.page;

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
import com.project.R;
import com.project.customs.MainPageAdapter;
import com.project.detail.StatusDetail;
import com.project.detail.Host;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class MainPage extends Fragment {

	ListView a;
	Button bt;
	ArrayList<StatusDetail> list;
	boolean loadingMore = false;
	String lastKey = "";
	String topKey = "";
	int mCout = 0;
	int mScrollState = 0;
	int mFirstVisibleItem = 0;
	int mVisibleItemCount = 0;
	int mTotalItemCount = 0;
	Boolean isLastStatus = true;
	MainPageAdapter adapter;
	Boolean isF = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = new ArrayList<StatusDetail>();
		String temp = getArguments().getString("data");
	
		try {
			if (!temp.equals("1")) {

				JSONArray data2 = new JSONArray(temp);

				for (int i = 0; i < data2.length(); i++) {
					StatusDetail status;

					status = getStatusDetail(data2.getJSONObject(i));
					System.out.println(temp);
					list.add(status);

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		adapter = new MainPageAdapter(getActivity(), list);
		a.setAdapter(adapter);
		a.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mScrollState = scrollState;
				getScroll();
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				mFirstVisibleItem = firstVisibleItem;
				mVisibleItemCount = visibleItemCount;
				mTotalItemCount = totalItemCount;
				int lastInScreen = firstVisibleItem + visibleItemCount;
				if ((lastInScreen >= totalItemCount - 3) && !(loadingMore)) {
					loadingMore = true;
					if (isLastStatus) {
						if (list.get(list.size() - 1).getStatus() == true) {
							lastKey = list.get(list.size() - 1).getProductID();
						} else {
							lastKey = list.get(list.size() - 1).getJobID();
						}
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("User", Host.user));
						params.add(new BasicNameValuePair("LastKey", lastKey));
						params.add(new BasicNameValuePair("StatusScroll", "0"));
						params.add(new BasicNameValuePair("bID", Host.bID));
						setStatusScroll(params, false);

					}
				}
			}

		});
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.main_page, container, false);
		bt = (Button) rootView.findViewById(R.id.seDetail);
		a = (ListView) rootView.findViewById(R.id.lvMain);
		return rootView;
	}

	public void getScroll() {
		if (mScrollState == 0 && mFirstVisibleItem == 0) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("User", Host.user));
			if (list.get(0).getStatus() == true) {
				topKey = list.get(0).getProductID();
			} else {
				topKey = list.get(0).getJobID();
			}

			params.add(new BasicNameValuePair("LastKey", topKey));
			params.add(new BasicNameValuePair("StatusScroll", "1"));
			params.add(new BasicNameValuePair("bID", Host.bID));
			setStatusScroll(params, true);

		}

	}

	public void setStatusScroll(List<NameValuePair> params, Boolean statusScroll) {

		String url = Host.host + "/Member";
		new DownloadJsonTask().execute(url, params, statusScroll);

	}

	class DownloadJsonTask extends AsyncTask<Object, Void, String> {

		StringBuilder str;
		HttpClient client;
		HttpPost httpPost;
		Boolean statusScroll;

		@Override
		protected String doInBackground(Object... urls) {
			httpPost = new HttpPost((String) urls[0]);
			statusScroll = (Boolean) urls[2];
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
				if (result.equals("1") && !statusScroll) {
					isLastStatus = false;
				} else {

					if (!result.equals("1")) {

						JSONArray data2 = new JSONArray(result);

						for (int i = 0; i < data2.length(); i++) {
							StatusDetail status = getStatusDetail(data2
									.getJSONObject(i));
							if (statusScroll) {
								list.add(0, status);
								Log.v("download", "OK");
							} else {
								list.add(status);
							}

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

	public StatusDetail getStatusDetail(JSONObject StatusDetail) {
		StatusDetail statusDetail = new StatusDetail();

		try {
			Boolean isProduct = StatusDetail.getBoolean("Status");
			statusDetail.setStatus(isProduct);
			if (isProduct == true) {
				statusDetail.setAmount(StatusDetail.get("Amount").toString());
				statusDetail.setName(StatusDetail.get("Name").toString());
				statusDetail.setPhoto(StatusDetail.get("Photo").toString());
				statusDetail.setBrand(StatusDetail.get("Brand").toString());
				// statusDetail.setDate(StatusDetail.get("date").toString());
				statusDetail.setGroup(StatusDetail.get("Group").toString());
				statusDetail.setKind(StatusDetail.get("Kind").toString());
				statusDetail.setProductID(StatusDetail.get("ProductID")
						.toString());
				statusDetail.setPrice(StatusDetail.get("Price").toString());
				statusDetail.setPhotoSmall(StatusDetail.get("Picturesmall")
						.toString());
			} else {
				statusDetail
						.setCarID(StatusDetail.getString("CarRegistration"));
				statusDetail.setJobID(StatusDetail.getString("IDjob"));
				statusDetail.setLatitude(StatusDetail.getString("Latitude"));
				statusDetail.setLongitude(StatusDetail.getString("Longitude"));
				statusDetail.setStatusJob(StatusDetail.getInt("StatusJob"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return statusDetail;

	}

}