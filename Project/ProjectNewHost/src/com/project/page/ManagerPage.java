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

import com.google.android.gms.maps.GoogleMap;
import com.project.LocalCarPage;
import com.project.R;
import com.project.customs.ManagerAdapter;
import com.project.detail.Host;
import com.project.detail.StatusDetail;
import com.project.json.GetJson;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class ManagerPage extends Fragment {
	GoogleMap mMap;
	GetJson gJ = new GetJson();
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
	ManagerAdapter adapter;
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

		adapter = new ManagerAdapter(getActivity(), list);
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
				Log.v("test", lastInScreen + "," + (totalItemCount - 3));
				if ((lastInScreen >= totalItemCount - 3) && !(loadingMore)) {
					loadingMore = true;
					if (isLastStatus&&list.size()>0) {
						Log.v("test", list.size()+"");
						lastKey = list.get(list.size()-1).getJobID();
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("User", Host.user));
						params.add(new BasicNameValuePair("LastKey", lastKey));
						params.add(new BasicNameValuePair("StatusScroll", "0"));
						setStatusScroll(params, false);

					}
				}
			}

		});
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<String> listSYDNEY = new ArrayList<String>();
				ArrayList<String> listJobID = new ArrayList<String>();
				Intent inte = new Intent(getActivity(), LocalCarPage.class);
				for (int i = 0; i < list.size(); i++) {
					StatusDetail data = list.get(i);

					if (data.getStatusJob() == 1 ) {
						listSYDNEY.add(data.getLatitude() + ","
								+ data.getLongitude());

						listJobID.add(data.getJobID());

					}

				}
				inte.putStringArrayListExtra("SYDNEY", listSYDNEY);

				inte.putStringArrayListExtra("JobID", listJobID);
				startActivity(inte);

			}
		});

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.manager_page, container,
				false);
		bt = (Button) rootView.findViewById(R.id.btSeeAll);
		a = (ListView) rootView.findViewById(R.id.lvManager);
		return rootView;
	}

	public void getScroll() {
		if (mScrollState == 0 && mFirstVisibleItem == 0) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("User", Host.user));

			topKey = list.get(0).getJobID();

			params.add(new BasicNameValuePair("LastKey", topKey));
			params.add(new BasicNameValuePair("StatusScroll", "1"));
			setStatusScroll(params, true);

		}

	}

	public void setStatusScroll(List<NameValuePair> params, Boolean statusScroll) {

		String url = Host.host + "/idjobsort";
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
			statusDetail.setCarID(StatusDetail.getString("CarRegistration"));
			statusDetail.setJobID(StatusDetail.getString("IDjob"));
			statusDetail.setLatitude(StatusDetail.getString("Latitude"));
			statusDetail.setLongitude(StatusDetail.getString("Longitude"));
			statusDetail.setStatusJob(StatusDetail.getInt("StatusJob"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return statusDetail;

	}
}
