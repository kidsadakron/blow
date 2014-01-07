package com.project.customs;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.project.MainActivity;
import com.project.R;
import com.project.detail.Host;
import com.project.json.GetJson;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends FragmentActivity {
	EditText pass;
	EditText user;
	Button btLogin;
	Button btRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_dialog);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		pass = (EditText) findViewById(R.id.pass);
		user = (EditText) findViewById(R.id.user);
		btLogin = (Button) findViewById(R.id.btLogin);
		btRegister = (Button) findViewById(R.id.btRegister);

		btRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent toRegister = new Intent(getApplicationContext(),
						RegisterPage.class);
				startActivity(toRegister);

			}
		});
		btLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String url = Host.host + "/login";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("User", user.getText()
						.toString()));
				params.add(new BasicNameValuePair("Pass", pass.getText()
						.toString()));
				GetJson gJ = new GetJson();

				String resultServer = gJ.getHttpPost(url, params);

				/*** Default Value ***/
				String strStatusID = "0";
				String strMemberID = "0";

				JSONObject c;
				try {

					String strError = "Unknow Status!";
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
					user.requestFocus();
				} else {
					Toast.makeText(LoginPage.this, "Login OK", Toast.LENGTH_SHORT)
							.show();
					Host.user = strMemberID;
					Intent i = new Intent(getApplicationContext(),
							MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);
				}

			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
