package com.project.customs;

import java.util.ArrayList;

import com.project.R;
import com.project.detail.StatusDetail;
import com.project.page.manager.DetailManagerPage;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ManagerAdapter extends BaseAdapter{
	private ArrayList<StatusDetail> mList;
	private Context mCtx;
	LayoutInflater inflater;
	StatusDetail status;
	Handler handler;
	ImageView pic;
	String image_URL;

	public ManagerAdapter(Context ctx, ArrayList<StatusDetail> list) {
		this.mCtx = ctx;
		this.mList = list;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	public void addTop(StatusDetail data) {
		mList.add(0, data);
	}

	@Override
	public Object getItem(int position) {

		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		status = mList.get(position);
		View v  = inflater.inflate(R.layout.main_item_status_job_idxml, parent,
					false);
			TextView tvMainJob = (TextView) v.findViewById(R.id.tvMainJob);
			LinearLayout llMainJob = (LinearLayout) v
					.findViewById(R.id.llMainJob);
			llMainJob.setBackgroundResource(R.drawable.shape);
			tvMainJob.setText("ใบเสร็จเลขที่ " + status.getJobID());
			llMainJob.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mCtx.getApplicationContext(),
							DetailManagerPage.class);
					StatusDetail statusCar = mList.get(position);
					intent.putExtra("Latitude", statusCar.getLatitude());
					intent.putExtra("Longitude", statusCar.getLongitude());
					intent.putExtra("JobID", statusCar.getJobID());
					intent.putExtra("StatusJob", statusCar.getStatusJob());
					mCtx.startActivity(intent);

				}
			});
	

		return v;
	}
}
