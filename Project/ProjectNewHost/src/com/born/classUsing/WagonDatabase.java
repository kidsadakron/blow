package com.born.classUsing;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.project.MainActivity;
import com.project.detail.Host;
import com.project.json.GetJson;

public class WagonDatabase {
	String key = "";
	List<NameValuePair> params;
	GetJson getJson = new GetJson();
	String url = Host.host + "/wagon";

	public String addItem(String user, String productID, String amount) {
		key = "addItem";
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("User", user));
		params.add(new BasicNameValuePair("ProductID", productID));
		params.add(new BasicNameValuePair("Amount", amount));
		params.add(new BasicNameValuePair("Key", key));
		params.add(new BasicNameValuePair("bID", Host.bID));
		return getJson.getHttpPost(url, params);
	}

	public String deleteItem(String user, String productID) {
		key = "deleteItem";
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("User", user));
		params.add(new BasicNameValuePair("ProductID", productID));
		params.add(new BasicNameValuePair("Key", key));
		params.add(new BasicNameValuePair("bID", Host.bID));
		return getJson.getHttpPost(url, params);
	}

	public String deleteItemAll(String user) {
		key = "deleteItemAll";
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("User", user));
		params.add(new BasicNameValuePair("Key", key));
		params.add(new BasicNameValuePair("bID", Host.bID));
		return getJson.getHttpPost(url, params);
	}

	public String selectItemAll(String user) {
		key = "selectItemAll";
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("User", user));
		params.add(new BasicNameValuePair("Key", key));
		params.add(new BasicNameValuePair("bID", Host.bID));
		return getJson.getHttpPost(url, params);

	}

	public String editAmountItem(String user, String productID, String amount) {
		key = "editAmountItem";
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("User", user));
		params.add(new BasicNameValuePair("ProductID", productID));
		params.add(new BasicNameValuePair("Amount", amount));
		params.add(new BasicNameValuePair("Key", key));
		params.add(new BasicNameValuePair("bID", Host.bID));
		return getJson.getHttpPost(url, params);
	}

	public void dialogNoAddDatabase(Context ctx) {
		final Context mCtx = ctx;
		AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
		builder.setIcon(android.R.drawable.ic_popup_disk_full);
		builder.setTitle("คำเตือน");
		builder.setMessage("ไม่สามารถเพิ่มเข้าไปในรถเข็นได้เนื่องจากในรถเข็นมีรายการนี้แล้ว");
		builder.setPositiveButton("ไปหน้ารถเข็น",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						Intent toWagon = new Intent(mCtx
								.getApplicationContext(), MainActivity.class);
						toWagon.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						toWagon.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						toWagon.putExtra("Page", 3);
						mCtx.startActivity(toWagon);
					}
				});
		builder.setNegativeButton("กลับ",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

					}
				});

		builder.show();

	}

	public String submitAll(String user) {
		key = "Create";
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("User", user));
		params.add(new BasicNameValuePair("Key", key));
		params.add(new BasicNameValuePair("bID", Host.bID));
		return getJson.getHttpPost(url, params);

	}
	
	public void dialogAddDatabase(Context ctx,String productName) {
		final Context mCtx = ctx;
		AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
	
		  builder.setMessage(productName+ " อยู่ในรถเข็นเรียบร้อยแล้ว")
          .setPositiveButton("ok", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                  // FIRE ZE MISSILES!
              }
          });
		builder.show();

	}
}
