package com.project.customs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.born.classUsing.GPSTracker;
import com.project.R;
import com.project.detail.Host;
import com.project.json.GetJson;
import com.project.page.MainPage;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterPage extends FragmentActivity {
	/*
	 * ID_Member Password Name Address Tel_Call Email Latitude Longitude
	 */
	Button okRe;
	EditText edUser;
	EditText edTell;
	EditText edPass;
	EditText edFName;
	EditText edLName;
	EditText edPass2;
	EditText edEmail;
	EditText edAddressNum;
	EditText edRoad;
	EditText edIDCardNumber;
	FrameLayout next1;
	Button btAddress;
	DatePicker dpBirthday;
	NumberPicker pDetailPerson;
	FrameLayout okDetailPerson;
	FrameLayout nextToPerson;

	TextView showDetailRegister;
	Button btEdit;
	GetJson gJ = new GetJson();
	String Birthday = "";// --
	String User = "";
	String Password = "";
	String FName = "";// --
	String LName = "";// --
	String AddressNum = "";// --
	String AddressProvince = "";// --
	String AddressAmphur = "";// --
	String AddressDistrict = "";// --
	String AddressRoad = "";// --
	String AddressZipCode = "";// --
	String TelCall = "";
	String Email = "";
	String perName = "";// --
	String IDCardNumber = "";

	int mSelected = -1;
	ArrayList<String> s;
	Dialog dialog;
	public static String temp;
	String urlLocal = Host.host + "/Getprovinces";

	String[] perNamex = new String[] { "นาย", "นาง", "นางสาว" };

	@Override
	protected void onStart() {
		showDialogDetailPerson();
		super.onStart();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.register_page);
		btEdit = (Button) findViewById(R.id.btEdit);
		btEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialogDetailPerson();

			}
		});
		okRe = (Button) findViewById(R.id.okRe);
		okRe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GPSTracker mGPS = new GPSTracker(getApplicationContext());
				double mLat = 0f;
				double mLong = 0f;
				if (mGPS.canGetLocation) {

					mLat = mGPS.getLatitude();
					mLong = mGPS.getLongitude();

				} else {
					// can't get the location
				}

				Log.v("la", mLat + "");
				Log.v("lo", mLong + "");

				String url = Host.host + "/insertregister";
				List<NameValuePair> params1 = new ArrayList<NameValuePair>();
				params1.add(new BasicNameValuePair("Birthday", Birthday));
				params1.add(new BasicNameValuePair("User", User));
				params1.add(new BasicNameValuePair("Password", Password));
				params1.add(new BasicNameValuePair("FName", FName));
				params1.add(new BasicNameValuePair("LName", LName));
				params1.add(new BasicNameValuePair("AddressNum", AddressNum));
				params1.add(new BasicNameValuePair("AddressProvince",
						AddressProvince));
				params1.add(new BasicNameValuePair("AddressAmphur",
						AddressAmphur));
				params1.add(new BasicNameValuePair("AddressDistrict",
						AddressDistrict));
				params1.add(new BasicNameValuePair("AddressRoad", AddressRoad));
				params1.add(new BasicNameValuePair("AddressZipCode",
						AddressZipCode));
				params1.add(new BasicNameValuePair("TelCall", TelCall));
				params1.add(new BasicNameValuePair("Email", Email));
				params1.add(new BasicNameValuePair("perName", perName));
				params1.add(new BasicNameValuePair("IDCardNumber", IDCardNumber));
				params1.add(new BasicNameValuePair("Latitude", mLat + ""));
				params1.add(new BasicNameValuePair("Longitude", mLong + ""));
				String tempa = gJ.getHttpPost(url, params1);
				JSONObject statusRegister;
				try {
					if (tempa.equals("1")) {
						
					
					statusRegister = new JSONObject(tempa);

					if (statusRegister.getString("StatusID").equals("1")) {
						Intent upIntent = new Intent(getApplicationContext(),
								RegisterPage.class);
						upIntent.putExtra("User", User);
						startActivity(upIntent);
					}else{
						AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPage.this);
						if (statusRegister.getString("Error").equals("1")) {
							builder.setMessage("มีชื่อสมาชิกนี้ในระบบแล้ว กรุณาใช้ชื่อใหม่")
						       .setCancelable(false)
						       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						                //do things
						           }
						       });
						}else if (statusRegister.getString("Error").equals("2")) {
							builder.setMessage("มี E-Mail นี้ในระบบแล้ว กรุณาใช้ E-Mail ใหม่")
						       .setCancelable(false)
						       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						                //do things
						           }
						       });
						}else if (statusRegister.getString("Error").equals("3")) {
							builder.setMessage("มีรหัสบัตรประจำตัวประชาชนนี้ในระบบแล้ว กรุณาใช้รหัสบัตรประจำตัวประชาชนใหม่")
						       .setCancelable(false)
						       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						                //do things
						           }
						       });
						}
						
						AlertDialog alert = builder.create();
						alert.show();
					}}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});

	}

	public ArrayList<String> getProvince() {
		ArrayList<String> Province = new ArrayList<String>();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Province", "a"));
		String tempa = gJ.getHttpPost(urlLocal, params);
		try {
			JSONArray data2 = new JSONArray(tempa);

			for (int i = 0; i < data2.length(); i++) {

				Province.add(data2.getJSONObject(i).getString("Province"));

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Province;
	}

	public ArrayList<String> getProvince(String Province) {
		ArrayList<String> Amphur = new ArrayList<String>();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Province", Province));
		String tempa = gJ.getHttpPost(urlLocal, params);
		try {
			JSONArray data2 = new JSONArray(tempa);

			for (int i = 0; i < data2.length(); i++) {

				Amphur.add(data2.getJSONObject(i).getString("Amphur"));

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Amphur;
	}

	public ArrayList<String> getProvince(String Province, String Amphur) {
		ArrayList<String> District = new ArrayList<String>();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Amphur", Amphur));
		params.add(new BasicNameValuePair("Province", Province));
		String tempa = gJ.getHttpPost(urlLocal, params);
		try {
			JSONArray data2 = new JSONArray(tempa);

			for (int i = 0; i < data2.length(); i++) {

				District.add(data2.getJSONObject(i).getString("Distric"));

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return District;
	}

	public String getZipCode(String Province, String Amphur, String District) {
		String ZipCode = "";

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Amphur", Amphur));
		params.add(new BasicNameValuePair("Province", Province));
		params.add(new BasicNameValuePair("Distric", District));
		String tempa = gJ.getHttpPost(urlLocal, params);
		try {
			JSONObject data2 = new JSONObject(tempa);

			for (int i = 0; i < data2.length(); i++) {

				ZipCode = data2.getString("Zip_Code");

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ZipCode;
	}

	public void showDialogProvince(final View v1) {

		final Builder build = new Builder(RegisterPage.this);
		build.setTitle("เลือกจังหวัด");
		build.setCancelable(true);
		build.setSingleChoiceItems(s.toArray(new String[s.size()]), -1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mSelected = which;

					}
				});

		build.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				String message = "";
				if (mSelected == -1) {
					message = "You didn't select anything.";
				} else {

					message = s.get(mSelected);

					AddressProvince = message;
					s = getProvince(AddressProvince.trim());
					showDialogAmphur(v1);

				}
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();

			}
		});
		build.show();

	}

	public void showDialogAmphur(final View v2) {

		final Builder build = new Builder(RegisterPage.this);
		build.setTitle("เลือกอำเภอ / เขต");
		build.setCancelable(true);
		build.setSingleChoiceItems(s.toArray(new String[s.size()]), -1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mSelected = which;

					}
				});

		build.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				String message = "";
				if (mSelected == -1) {
					message = "You didn't select anything.";
				} else {

					message = s.get(mSelected);

					AddressAmphur = message;

					s = getProvince(AddressProvince.trim(),
							AddressAmphur.trim());
					showDialogDistrict(v2);

				}
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();

			}
		});
		build.show();

	}

	public void showDialogDistrict(final View v3) {

		final Builder build = new Builder(RegisterPage.this);
		build.setTitle("เลือกตำบล / แขวง");
		build.setCancelable(true);
		build.setSingleChoiceItems(s.toArray(new String[s.size()]), -1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mSelected = which;

					}
				});

		build.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				String message = "";
				if (mSelected == -1) {
					message = "You didn't select anything.";
				} else {

					message = s.get(mSelected);

					AddressDistrict = message;
					AddressZipCode = getZipCode(AddressProvince.trim(),
							AddressAmphur.trim(), AddressDistrict.trim());
					showDetailRegister();

				}
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();

			}
		});
		build.show();

	}

	public void showDialogAddressNum() {
		dialog = new Dialog(RegisterPage.this);
		dialog.setContentView(R.layout.register_dialog_address_num);
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		dialog.setTitle("กรุณาใส่ที่อยู่ของท่าน");
		edAddressNum = (EditText) dialog.findViewById(R.id.edAddressNum);
		edRoad = (EditText) dialog.findViewById(R.id.edRoad);
		edEmail = (EditText) dialog.findViewById(R.id.edEmail);
		edTell = (EditText) dialog.findViewById(R.id.edTell);
		next1 = (FrameLayout) dialog.findViewById(R.id.next1);
		edAddressNum.setText(AddressNum);
		edRoad.setText(AddressRoad);
		edEmail.setText(Email);
		edTell.setText(TelCall);

		next1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
					next1.setBackgroundColor(0xA03F9FE0);
				} else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
					next1.setBackgroundResource(R.drawable.register);
				}

				return false;
			}
		});
		next1.setBackgroundColor(0x00000000);
		next1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AddressNum = edAddressNum.getText().toString();
				TelCall = edTell.getText().toString();
				Email = edEmail.getText().toString();
				AddressRoad = edRoad.getText().toString() + "";
				if (!AddressNum.equals("") && !Email.equals("")
						&& !TelCall.equals("")) {
					dialog.dismiss();
					s = getProvince();
					showDialogProvince(v);
				} else {
					Toast.makeText(getApplicationContext(),
							"กรุณากรอกข้อมูบบ้านเลขที่ให้ถูกต้อง",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		dialog.show();

	}

	public void showDialogDetailPerson() {

		dialog = new Dialog(RegisterPage.this);
		dialog.setContentView(R.layout.register_dialog_detail_person);
		dialog.setTitle("กรุณากรอกข้อมูลของท่าน");
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		pDetailPerson = (NumberPicker) dialog.findViewById(R.id.pDetailPerson);
		pDetailPerson.setMaxValue(2);
		pDetailPerson.setMinValue(0);
		pDetailPerson.setWrapSelectorWheel(false);
		pDetailPerson.setDisplayedValues(perNamex);
		okDetailPerson = (FrameLayout) dialog.findViewById(R.id.okDetailPerson);
		edFName = (EditText) dialog.findViewById(R.id.edFName);
		edLName = (EditText) dialog.findViewById(R.id.edLName);
		edFName.setText(FName);
		edLName.setText(LName);
		okDetailPerson.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
					okDetailPerson.setBackgroundColor(0xA03F9FE0);
				} else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
					okDetailPerson.setBackgroundResource(R.drawable.register);
				}
				return false;
			}
		});
		okDetailPerson.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				perName = perNamex[pDetailPerson.getValue()];
				FName = edFName.getText().toString();
				LName = edLName.getText().toString();
				if (!perName.equals("") && !FName.equals("")
						&& !LName.equals("")) {
					dialog.dismiss();
					showDialog(1);

				} else {
					Toast.makeText(getApplicationContext(),
							"กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		dialog.show();
	}

	public void showDialogUser() {
		dialog = new Dialog(RegisterPage.this);
		dialog.setContentView(R.layout.regoster_dialog_detail_user);
		dialog.setTitle("กรุณากรอกข้อมูลของท่าน");
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		edUser = (EditText) dialog.findViewById(R.id.edUser);
		edPass = (EditText) dialog.findViewById(R.id.edPass);
		edPass2 = (EditText) dialog.findViewById(R.id.edPass2);
		edIDCardNumber = (EditText) dialog.findViewById(R.id.edIDCard);
		nextToPerson = (FrameLayout) dialog.findViewById(R.id.nextToPerson);

		edUser.setText(User);
		edPass.setText(Password);
		edIDCardNumber.setText(IDCardNumber);
		nextToPerson.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
					nextToPerson.setBackgroundColor(0xA03F9FE0);
				} else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
					nextToPerson.setBackgroundResource(R.drawable.register);
				}
				return false;
			}
		});

		final TextWatcher mTextEditorWatcher = new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String passTemp1 = edUser.getText().toString();
				if (passTemp1.length() > 0) {
					char charTemp = passTemp1.charAt(passTemp1.length() - 1);
					if (!(charTemp >= 'a' && charTemp <= 'z')
							&& !(charTemp >= 'A' && charTemp <= 'Z')
							&& !(charTemp >= '0' && charTemp <= '9')
							&& charTemp != '.' && charTemp != '-'
							&& charTemp != '_') {
						edUser.setText(passTemp1.substring(0,
								passTemp1.length() - 1));
					}

				}
				edUser.setSelection(edUser.getText().length());
			}

			public void afterTextChanged(Editable s) {
			}
		};
		edUser.addTextChangedListener(mTextEditorWatcher);
		nextToPerson.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				User = edUser.getText().toString();
				String passTemp = edPass2.getText().toString();
				Password = edPass.getText().toString();
				IDCardNumber = edIDCardNumber.getText().toString();
				if (passTemp.equals(Password) && Password.length() >= 4) {
					if (!User.equals("") && !Password.equals("")
							&& !IDCardNumber.equals("") && User.length() > 3) {
						if (IDCardNumber.length() == 13) {
							dialog.dismiss();
							showDialogAddressNum();
						} else {
							Toast.makeText(
									getApplicationContext(),
									"กรุณาใส่รหัสบัตรประจำตัวประชาชนให้ถูกต้อง",
									Toast.LENGTH_LONG).show();
						}

					} else {
						Toast.makeText(getApplicationContext(),
								"กรุณาใส่ข้อมูลให้ถูกต้อง", Toast.LENGTH_LONG)
								.show();
					}

				} else {
					Toast.makeText(getApplicationContext(),
							"กรุณาใส่รหัสผ่านให้ถูกต้อง", Toast.LENGTH_LONG)
							.show();
				}

			}
		});

		dialog.show();
	}

	public void showDetailRegister() {
		showDetailRegister = (TextView) findViewById(R.id.tvShowDetailRegister);

		String temp = "ชื่อ : " + perName + " " + FName + " " + LName
				+ "  เกิดวันที่ : " + Birthday + "\n" + "ชื่อสมาชิก : " + User
				+ "\n" + "รหัสบัตรประจำตัวประชาชน : " + IDCardNumber + "\n"
				+ "เบอร์โทร : " + TelCall + "\n" + "Email : " + Email + "\n"
				+ "บ้านเลขที่ : " + AddressNum + "  ถนน : " + AddressRoad
				+ "\n" + "ตำบล/เขต : " + AddressDistrict + "   อำเภอ : "
				+ AddressAmphur + "\n" + "จังหวัด : " + AddressProvince + " "
				+ AddressZipCode;
		showDetailRegister.setText(temp);

	}

	@Override
	protected Dialog onCreateDialog(int id) {

		// set date picker as current date
		Calendar c = Calendar.getInstance();

		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		final DatePickerDialog a = new DatePickerDialog(this, null, year,
				month, day);

		a.getDatePicker().setCalendarViewShown(false);
		a.setTitle("เลือกวันเกิดของท่าน");
		a.setButton(DatePickerDialog.BUTTON_NEGATIVE, null,
				(DialogInterface.OnClickListener) null);
		a.setButton(DialogInterface.BUTTON_POSITIVE, "ถัดไป",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Birthday = a.getDatePicker().getDayOfMonth() + "-"
								+ a.getDatePicker().getMonth() + "-"
								+ a.getDatePicker().getYear();
						showDialogUser();
					}
				});
		a.getDatePicker().setMaxDate(c.getTimeInMillis());

		return a;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			Intent upIntent = new Intent(this, MainPage.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				// This activity is not part of the application's task, so
				// create a new task
				// with a synthesized back stack.
				TaskStackBuilder.from(this)
				// If there are ancestor activities, they should be added here.
						.addNextIntent(upIntent).startActivities();
				finish();
			} else {
				// This activity is part of the application's task, so simply
				// navigate up to the hierarchical parent activity.
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
