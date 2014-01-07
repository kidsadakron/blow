package com.project.page.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.project.MainActivity;
import com.project.R;
import com.project.detail.Host;
import com.project.json.GetJson;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.MenuItem;

public class DetailManagerPage extends FragmentActivity {
	String jobId;
	double longitude;
	double latitude;
	int statusJob;
	int coPage = 0;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter myAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_manager_page);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		String temp = getIntent().getExtras().getString("Longitude");
		if (!temp.equals("")) {
			longitude = Double.parseDouble(temp);
			temp = getIntent().getExtras().getString("Latitude");
			latitude = Double.parseDouble(temp);
			
		}
		jobId = getIntent().getExtras().getString("JobID");
		statusJob = getIntent().getIntExtra("StatusJob", 0);
		if (getIntent().hasExtra("Page")) {
			coPage = getIntent().getIntExtra("Page", 1);
		}
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabDetailManaget);
		pager = (ViewPager) findViewById(R.id.pagerDetailManaget);
		myAdapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(myAdapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);
		tabs.setIndicatorColor(0xFF00a0ff);
		tabs.setTextSize((int) getResources().getDimension(R.dimen.bar_text));
		tabs.setTextColor(0xFFFFFFFF);

		pager.setCurrentItem(coPage);
		pager.setOffscreenPageLimit(1);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case android.R.id.home:

			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			return true;
		}
		return true;
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = getResources().getStringArray(
				R.array.textManager);

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			if (statusJob == 1) {
				return TITLES.length;
			} else {
				return 1;
			}

		}

		public Fragment productPageGo(int position) {
			Fragment fragment;
			Bundle args;
			switch (position) {
			case 0:
				fragment = new DetailJobId();
				args = new Bundle();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("IDjob", jobId));
				String url = Host.host + "/whereidjob";
				GetJson gJ = new GetJson();
				String temp = gJ.getHttpPost(url, params);
				args.putString("Data", temp);
				args.putString("JobID", jobId);
				args.putInt("StatusJob", statusJob);
				fragment.setArguments(args);
				return fragment;

			default:
				fragment = LocalCarNew.newInstance(jobId, longitude, latitude,
						statusJob);
				args = new Bundle();
				args.putString("JobID", jobId);
				args.putDouble("Longitude", longitude);
				args.putDouble("Latitude", latitude);
				args.putInt("StatusJob", statusJob);
				fragment.setArguments(args);
				return fragment;
			}

		}

		@Override
		public Fragment getItem(int position) {
			return productPageGo(position);
		}
	}

}
