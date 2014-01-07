package com.project.page;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.born.classUsing.WagonDatabase;
import com.project.MainActivity;
import com.project.R;
import com.project.customs.WagonAdapter;
import com.project.detail.Host;
import com.project.detail.ProductDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class WagonPage extends Fragment {

	TextView topic;
	Button deleteAll;
	Button submit;
	EditText edNum;
	ArrayList<ProductDetail> mList;
	ListView listAll;
	Double total = 0.0;
	String temp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mList = new ArrayList<ProductDetail>();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.wagon_page, container, false);
		listAll = (ListView) rootView.findViewById(R.id.lvWagon);
		topic = (TextView) rootView.findViewById(R.id.tvWagonTopic);
		deleteAll = (Button) rootView.findViewById(R.id.btDeleteAll);
		submit = (Button) rootView.findViewById(R.id.btWagonSubmit);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mList.clear();
		WagonDatabase wagon = new WagonDatabase();
		temp = wagon.selectItemAll(Host.user);
		Log.v("temp", temp);
		try {
			if (!temp.equals("1")) {
				JSONArray data2 = new JSONArray(temp);
				ArrayList<ProductDetail> dataTemp = new ArrayList<ProductDetail>();
				for (int i = 0; i < data2.length(); i++) {
					ProductDetail item = getProductDetail(data2
							.getJSONObject(i));
					Log.v("item.getAmount()", item.getAmount());
					total += (Double.parseDouble(item.getPrice()) * Integer
							.parseInt(item.getAmount())) ;
					dataTemp.add(item);
					mList.add(item);
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		WagonAdapter adapter = new WagonAdapter(getActivity(), mList);
		listAll.setDivider(null);
		listAll.setDividerHeight(0);
		listAll.setAdapter(adapter);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WagonDatabase wagon = new WagonDatabase();
				 String a =wagon.submitAll(Host.user);
					Intent toMain = new Intent(getActivity(), MainActivity.class);
					toMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					toMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(toMain);
			}
		});
		deleteAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WagonDatabase wagon = new WagonDatabase();
				wagon.deleteItemAll(Host.user);
				Intent toWagon = new Intent(getActivity(), MainActivity.class);
				toWagon.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				toWagon.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				toWagon.putExtra("Page", 3);
				startActivity(toWagon);
			}
		});
		topic.setText("ยอดรวมทั้งหมด : " + total.toString());

		if ("0.0".equals(total.toString())) {
			deleteAll.setVisibility(View.GONE);

			submit.setVisibility(View.GONE);
		}
		super.onActivityCreated(savedInstanceState);
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