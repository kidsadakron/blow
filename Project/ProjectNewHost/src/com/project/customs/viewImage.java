package com.project.customs;

import com.born.classUsing.ImageLoader;
import com.born.classUsing.WagonDatabase;
import com.project.R;
import com.project.detail.ProductDetail;
import com.project.detail.Host;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

public class viewImage extends FragmentActivity {
	ImageView im;
	FrameLayout flImage;
	LinearLayout showDetail;
	// RelativeLayout rl;
	LinearLayout showButton;
	Button btAdd;
	TextView deName;
	TextView deBrand;
	TextView deUnit;
	TextView dePrice;
	Context mCont = this;
	boolean togle = true;
	FrameLayout fl;
	Animation inAnimation;
	Animation outAnimation;
	ImageView imadd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_dialog_view_image);

		fl = (FrameLayout) findViewById(R.id.fl);
		showButton = (LinearLayout) findViewById(R.id.showButton);
		showDetail = (LinearLayout) findViewById(R.id.showDetail);
		imadd = (ImageView) findViewById(R.id.imbtadd);
		inAnimation = AnimationUtils.loadAnimation(this,
				R.anim.anim_view_image_in);
		outAnimation = AnimationUtils.loadAnimation(this,
				R.anim.anim_view_image_out);
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		im = (ImageView) findViewById(R.id.vZoom);
		deBrand = (TextView) showDetail.findViewById(R.id.deBrand);
		deName = (TextView) showDetail.findViewById(R.id.deName);
		dePrice = (TextView) showDetail.findViewById(R.id.deP);
		deUnit = (TextView) showDetail.findViewById(R.id.deUnit);
		// btAdd = (Button) showButton.findViewById(R.id.vBtAdd);

		final ProductDetail status = (ProductDetail) getIntent()
				.getSerializableExtra("MyClass");
		Boolean noAdd = getIntent().getBooleanExtra("noAdd", false);
		String image_URL = Host.host + "" + status.getPhoto();
		ImageLoader imgLoader = new ImageLoader(getApplicationContext());
		deName.setText("ชื่อสินค่า : " + status.getName());
		deUnit.setText("หน่วยของสินค้า : " + status.getKind());
		deBrand.setText("ยี่ห้อ : " + status.getBrand());
		dePrice.setText("" + status.getPrice());
		if (noAdd) {
			btAdd.setVisibility(View.GONE);
		}

		int loader = R.drawable.w;
		imgLoader.DisplayImage(image_URL, loader, im);
		toggleShow();
		imadd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(mCont);
				dialog.setContentView(R.layout.main_dialog);
				dialog.setTitle("ระบุจำนวนที่ต้องการ");

				final NumberPicker np = (NumberPicker) dialog
						.findViewById(R.id.np);
				np.setMaxValue(100);
				np.setMinValue(1);
				np.setWrapSelectorWheel(false);
				TextView td = (TextView) dialog.findViewById(R.id.td1);
				td.setText("หน่วย");
				td.setTextSize(20f);
				InputMethodManager imm = (InputMethodManager) mCont
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(np.getApplicationWindowToken(), 0);

				Button dialogButton = (Button) dialog
						.findViewById(R.id.dialogButtonOK);
				dialogButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!Host.user.equals("")) {

							WagonDatabase wagon = new WagonDatabase();
							String ch = wagon.addItem(Host.user,
									status.getProductID(), np.getValue() + "")
									.trim();
							if (Boolean.parseBoolean(ch)) {
								dialog.dismiss();

								wagon.dialogAddDatabase(viewImage.this,
										status.getName());

							} else {
								dialog.dismiss();
								wagon.dialogNoAddDatabase(viewImage.this);
							}
						} else {
							Intent toRegister = new Intent(
									getApplicationContext(), LoginPage.class);
							startActivity(toRegister);
						}

					}
				});
				dialog.show();

			}
		});

		inAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				fl.setVisibility(View.VISIBLE);
				togle = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub

			}
		});
		outAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				fl.setVisibility(View.GONE);
				togle = true;

			}
		});
		im.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleShow();
			}
		});

	}

	public void toggleShow() {
		if (togle) {
			fl.startAnimation(inAnimation);
		} else {
			fl.startAnimation(outAnimation);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
