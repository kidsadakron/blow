package com.project.page.manager;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.project.R;
import com.project.detail.ProductDetail;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class DetailJobId extends Fragment {
	TextView tvHead;
	TextView tvStatus;
	ListView lvProduct;
	TextView tvTotal;
	ArrayList<ProductDetail> list;
	String jobID;
	int statusJob;
	Double total = 0.00;
	JSONArray data;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		list = new ArrayList<ProductDetail>();
		jobID = getArguments().getString("JobID");
		statusJob = getArguments().getInt("StatusJob");
		try {
			data  = new JSONArray(getArguments().getString("Data"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.detail_manager_jobid_detail,
				container, false);
		tvHead = (TextView) rootView
				.findViewById(R.id.tvDetailManagerProductHead);
		tvStatus = (TextView) rootView
				.findViewById(R.id.tvDetailManagerProductStatus);
		lvProduct = (ListView) rootView
				.findViewById(R.id.lvDetailMagagerProduct);
		tvTotal = (TextView) rootView.findViewById(R.id.tvDetailManagerResult);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		try {
			
			for (int i = 0; i < data.length(); i++) {
				JSONObject detail = data.getJSONObject(i);
				String price = detail.get("Price").toString();
				ProductDetail statusDetail = new ProductDetail();
				statusDetail.setAmount(detail.get("AmountJob").toString());
				statusDetail.setName(detail.get("Name").toString());
				statusDetail.setPhoto(detail.get("Photo").toString());
				statusDetail.setBrand(detail.get("Brand").toString());
				statusDetail.setGroup(detail.get("Group").toString());
				statusDetail.setKind(detail.get("Kind").toString());
				statusDetail.setProductID(detail.get("ProductID").toString());
				statusDetail.setPrice(price);
				statusDetail.setPhotoSmall(detail.get("Picturesmall")
						.toString());
				total += Double.parseDouble(price);
				list.add(statusDetail);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tvTotal.setText("รวมทั้งสิ้น " + total + " บาท");
		tvHead.setText("ใบเสร็จสินค้าเลขที่ " + jobID);
		if (statusJob == 1) {
			tvStatus.setText("สถานะ : กำลังจัดส่ง");
		} else if (statusJob == 0) {
			tvStatus.setText("สถานะ : กำลังจัดเตรียมสินค้า");
		} else {
			tvStatus.setText("สถานะ : จัดส่งเสร็จเรียนร้อยแล้ว");
		}
		ProductDetailManagerAdapter adapter = new ProductDetailManagerAdapter(getActivity().getApplicationContext(), list);
		lvProduct.setAdapter(adapter);
	}

	
}
