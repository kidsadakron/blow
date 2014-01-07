package com.project;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ArrayAdapter;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.project.R;
import com.project.customs.RegisterPage;
import com.project.customs.LoginPage;
import com.project.detail.Host;
import com.project.detail.ProductDetail;
import com.project.json.GetJson;
import com.project.page.ListMenu;
import com.project.page.MainPage;
import com.project.page.ManagerPage;
import com.project.page.WagonPage;

public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	int page = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		if (getIntent().hasExtra("Page")) {
			page = getIntent().getIntExtra("Page", 1);
		}
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());
 
		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);
		tabs.setIndicatorColor(0xFF00a0ff);
		tabs.setTextSize((int) getResources().getDimension(R.dimen.bar_text));
		tabs.setTextColor(0xFFFFFFFF);

		pager.setCurrentItem(page);
		pager.setOffscreenPageLimit(1);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		// Set up the dropdown list navigation in the action bar.
		GetJson getJson = new GetJson();

		ArrayList<String> sd = new ArrayList<String>();
		String temp = getJson.getHttpPost(Host.host + "/Branch_ID");
		
		try {
			if (!temp.equals("1")) {

				JSONArray data2 = new JSONArray(temp);
				for (int i = 0; i < data2.length(); i++) {
					sd.add(data2.getJSONObject(i).get("Name").toString());
					System.out.println(data2.getJSONObject(i).get("Name").toString());
					
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		String [] title = new String[sd.size()];
		for (int i = 0; i < sd.size(); i++) {
			title[i] = sd.get(i);
		}
		
		actionBar.setListNavigationCallbacks(
				new ArrayAdapter<String>(getActionBarThemedContextCompat(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, title), this);
	actionBar.setSelectedNavigationItem(Integer.parseInt(Host.bID)-1);
	System.out.println(Integer.parseInt(Host.bID)-1);

	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem menu1 = menu.findItem(R.id.loginBar);
		MenuItem menu2 = menu.findItem(R.id.registerBar);
		MenuItem menuBig = menu.findItem(R.id.btAddress);


		if (!Host.user.equals("")) {

			menu1.setTitle("ออกจากระบบ");
			menuBig.setIcon(R.drawable.ic_action_useri);
			menu2.setVisible(false);
		} else {
			menuBig.setIcon(R.drawable.ic_action_user);
			menu1.setTitle("เข้าสู่ระบบ");
		}
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		super.onOptionsItemSelected(item);
System.out.println(item.getItemId());
Log.d("item key bar", item.getItemId()+"");
		switch (item.getItemId()) {
		
		case R.id.loginBar:
			if (Host.user.equals("")) {
				Intent login = new Intent(getApplicationContext(),
						LoginPage.class);
				startActivity(login);
			} else {
				Intent intent = getIntent();
				Host.user = "";
				finish();
				startActivity(intent);
			}
			break;
		case R.id.registerBar:
			Intent toRegister = new Intent(getApplicationContext(),
					RegisterPage.class);
			startActivity(toRegister);
			break;
		}

		return true;

	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = getResources().getStringArray(
				R.array.textTitleS);

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			if (Host.user.equals("")) {
				return 2;
			} else {
				return TITLES.length;
			}
		}

		@Override
		public float getPageWidth(int position) {
			if (position == 0) {
				return (0.5f);
			} else {
				return (1.0f);
			}
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment;
			Bundle args;
			GetJson getJson = new GetJson();
			String url;
			List<NameValuePair> params;
			switch (position) {
			case 1:
				fragment = new MainPage();
				args = new Bundle();
				url = Host.host + "/Member";
				params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("User", Host.user));
				params.add(new BasicNameValuePair("LastKey", ""));
				params.add(new BasicNameValuePair("StatusScroll", "0"));
				params.add(new BasicNameValuePair("bID", Host.bID));
				args.putString("data", getJson.getHttpPost(url, params));
				
				fragment.setArguments(args);

				return fragment;
			case 0:
				fragment = new ListMenu();
				args = new Bundle();
				fragment.setArguments(args);
				return fragment;

			case 3:

				fragment = new WagonPage();
				args = new Bundle();
				fragment.setArguments(args);
				return fragment;

			default:
				url = Host.host + "/idjobsort";
				params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("User", Host.user));
				params.add(new BasicNameValuePair("LastKey", ""));
				params.add(new BasicNameValuePair("StatusScroll", "0"));

				fragment = new ManagerPage();
				args = new Bundle();
				args.putString("data", getJson.getHttpPost(url, params));
				fragment.setArguments(args);
				return fragment;

			}

		}
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		System.out.println(itemPosition);
		if (!((itemPosition+1)+"").equals(Host.bID)) {

			Host.bID = (itemPosition+1)+"";
			Intent intent = getIntent();
			finish();
			startActivity(intent);
	
		}
		return false;
	}

}