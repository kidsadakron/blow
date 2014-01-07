package com.project.page;

import java.util.ArrayList;

import com.project.R;
import com.project.customs.ListManutAdapter;
import com.project.json.GetJson;
import com.project.page.product.ProductPageNew;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListMenu extends Fragment {
	EditText searchEd;
	GetJson gJ = new GetJson();
	ArrayList<String> listMenu = new ArrayList<String>();
	ListView listMenuList;
	RelativeLayout serachLaout;
	FrameLayout menuLayout;
	String[] mListMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListMenu = getResources().getStringArray(R.array.listMenu);
		for (int i = 0; i < mListMenu.length; i++) {
			listMenu.add(mListMenu[i]);

		}
		

	}
@Override
public void onActivityCreated(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	menuLayout.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			menuLayout.requestFocus(); // use this to trigger the focus
										// listner
			// or use code below to set the keyboard to hidden
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getActivity().getWindow()
					.getCurrentFocus().getWindowToken(), 0);
		}
	});

	ListManutAdapter adapter = new ListManutAdapter(getActivity(),
			R.layout.list_costom, listMenu);
	listMenuList.setAdapter(adapter);

	listMenuList.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {

			Toast.makeText(getActivity(), "กรุณารอสักครู",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getActivity()
					.getApplicationContext(), ProductPageNew.class);
			intent.putExtra("type", listMenu.get(position));
			

			startActivity(intent);
		}
	});
	super.onActivityCreated(savedInstanceState);
}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("Start", "ok");
		View rootView = inflater.inflate(R.layout.list_manu, container, false);
		listMenuList = (ListView) rootView.findViewById(R.id.listManuItem);
		searchEd = (EditText) rootView.findViewById(R.id.search);
		serachLaout = (RelativeLayout) rootView.findViewById(R.id.reLayout);
		serachLaout.setBackgroundResource(R.drawable.shape);
		menuLayout = (FrameLayout) rootView.findViewById(R.id.layoutListManu);
		
		return rootView;
	}

	
}
