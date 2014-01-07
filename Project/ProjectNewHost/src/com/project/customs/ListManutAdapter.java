package com.project.customs;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.R;

public class ListManutAdapter extends ArrayAdapter<String>

{
	Context context;
	ArrayList<String> listMamu_data;
	LayoutInflater inflater;

	public ListManutAdapter(Context context, int textViewResourceId,
			ArrayList<String> data) {
		super(context, textViewResourceId, data);
		this.listMamu_data = new ArrayList<String>();
		this.listMamu_data.addAll(data);
		this.context = context;

	}

	private class ViewHolder {

		TextView item;
	}

	public void add(String Product) {

		this.listMamu_data.add(Product);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_costom, null);
			holder = new ViewHolder();
			holder.item = (TextView) convertView.findViewById(R.id.textv1);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String temp = this.listMamu_data.get(position).trim();
		if (temp.equals("all")) {
			holder.item.setText("สินค้าทั้งหมด");

		} else {
			holder.item.setText("  - " + temp);
		}
		return convertView;
	}
}