package com.project.page.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.project.MainActivity;
import com.project.R;
import com.project.customs.RegisterPage;
import com.project.detail.ProductDetail;
import com.project.detail.Host;
import com.project.json.GetJson;

public class ProductPageNew extends FragmentActivity {

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	EditText pass;
	EditText user;
	Button btLogin;
	Button btRegister;
	Context ct;
	String type;
	int coPage = 0;
	String[] listData;
	HashMap<String, String> data = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_page);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		ct = this;
		type = getIntent().getExtras().getString("type");
		listData = getResources().getStringArray(R.array.listMenu);
		for (int i = 0; i < listData.length; i++) {
			if (listData[i].equals(type)) {
				coPage = i;
				i = listData.length;
			}
		}
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabsP);
		pager = (ViewPager) findViewById(R.id.pagerP);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		pager.setOffscreenPageLimit(0);
		tabs.setViewPager(pager);
		tabs.setIndicatorColor(0xFF00a0ff);
		tabs.setTextSize((int) getResources().getDimension(R.dimen.bar_text));
		tabs.setTextColor(0xFFFFFFFF);

		pager.setCurrentItem(coPage);

		Log.d("i", coPage + "");
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// changeColor(currentColor);
		new AsyncTask<Integer, Integer, Boolean>() {
			ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				/*
				 * This is executed on UI thread before doInBackground(). It is
				 * the perfect place to show the progress dialog.
				 */
				progressDialog = ProgressDialog.show(ProductPageNew.this, "",
						"Loading...");
			}

			@Override
			protected Boolean doInBackground(Integer... params) {
				if (params == null) {
					return false;
				}
				try {

					Thread.sleep(params[0]);

				} catch (Exception e) {
					Log.e("tag", e.getMessage());
					return false;
				}

				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				progressDialog.dismiss();

			}
		}.execute(2000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem menu1 = menu.findItem(R.id.loginBar);
		MenuItem menuBig = menu.findItem(R.id.btAddress);

		MenuItem menu2 = menu.findItem(R.id.registerBar);
		if (!Host.user.equals("")) {

			menu1.setTitle("ออกจากระบบ");
			menuBig.setIcon(R.drawable.ic_action_useri);
			menu2.setVisible(false);
		} else {
			menuBig.setIcon(R.drawable.ic_action_user);
			menu1.setTitle("เข้าสู่ระบบ");

		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case android.R.id.home:
			
			Intent i = new Intent(getApplicationContext(),
					MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			return true;
		case R.id.loginBar:
			if (Host.user.equals("")) {
				final Dialog dialog = new Dialog(this);
				dialog.setContentView(R.layout.login_dialog);
				pass = (EditText) dialog.findViewById(R.id.pass);
				user = (EditText) dialog.findViewById(R.id.user);
				btLogin = (Button) dialog.findViewById(R.id.btLogin);
				btRegister = (Button) dialog.findViewById(R.id.btRegister);

				btLogin.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						String url = Host.host + "/login";
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("User", user
								.getText().toString()));
						params.add(new BasicNameValuePair("Pass", pass
								.getText().toString()));
						GetJson gJ = new GetJson();

						String resultServer = gJ.getHttpPost(url, params);

						/*** Default Value ***/
						String strStatusID = "0";
						String strMemberID = "0";
						String strError = "Unknow Status!";

						JSONObject c;
						try {
							c = new JSONObject(resultServer);
							strStatusID = c.getString("StatusID");
							strMemberID = c.getString("MemberID");
							strError = c.getString("Error");

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// Prepare Login
						if (strStatusID.equals("0")) {
							// Dialog
							user.setText("");
							pass.setText("");
						} else {
							Toast.makeText(ProductPageNew.this, "Login OK",
									Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							Intent intent = getIntent();
							Host.user = strMemberID;
							finish();
							startActivity(intent);
						}

					}
				});

				dialog.show();
			} else {
				Intent intent = getIntent();
				Host.user = "";
				finish();
				startActivity(intent);
			}
			break;
		case R.id.registerBar:
			Intent toRegister = new Intent(ct, RegisterPage.class);
			startActivity(toRegister);
			break;
		}

		return true;

	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = getResources().getStringArray(
				R.array.listMenu);

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String type = TITLES[position];

			return type;
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		public Fragment productPageGo(int position) {
			Fragment fragment = new ProductCustoms();
			Bundle args = new Bundle();

			GetJson getJson = new GetJson();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("LastKey", ""));
			params.add(new BasicNameValuePair("Type", listData[position]));
			params.add(new BasicNameValuePair("bID", Host.bID));
			String url = Host.host + "/Productnew";
			String temp = getJson.getHttpPost(url, params);
			args.putString("type", listData[position]);
			args.putString("data", temp);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public Fragment getItem(int position) {
			return productPageGo(position);
		}
	}

	public ProductDetail getProductDetail(JSONObject jsonObject) {
		ProductDetail statusDetail = new ProductDetail();

		try {
			statusDetail.setAmount(jsonObject.get("Amount").toString());
			statusDetail.setName(jsonObject.get("Name").toString());
			statusDetail.setPhoto(jsonObject.get("Photo").toString());
			statusDetail.setBrand(jsonObject.get("Brand").toString());
			// statusDetail.setDate(StatusDetail.get("date").toString());
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
