package com.yi4all.davidapp;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.yi4all.davidapp.db.CompanyModel;
import com.yi4all.davidapp.db.ContentType;
import com.yi4all.davidapp.db.dto.LinePerson;
import com.yi4all.davidapp.fragment.ContactusListFragment;
import com.yi4all.davidapp.fragment.FeedbackFragment;
import com.yi4all.davidapp.fragment.HallListFragment;
import com.yi4all.davidapp.fragment.IntroFragment;
import com.yi4all.davidapp.fragment.LoginFragment;
import com.yi4all.davidapp.fragment.MarketingListFragment;
import com.yi4all.davidapp.fragment.MemberMainFragment;
import com.yi4all.davidapp.fragment.OrderFragment;
import com.yi4all.davidapp.fragment.ServiceListFragment;
import com.yi4all.davidapp.util.Utils;
import com.yi4all.davidapp.view.InfiniteGallery;

public class MemberActivity extends BaseActivity {

	private final static String LOGTAG = "MemberActivity";
	
	public final static String EXTRA_DATA = "user";

	private final Object mClickLock = new Object();
	
	private int level = 0;

	private TextView titleTxt;

	private LinePerson currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_member);

		titleTxt = (TextView) findViewById(R.id.member_title_txt);
		
		ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				back();
				
			}
		});
		
		currentUser = (LinePerson) getIntent().getSerializableExtra(EXTRA_DATA); 

		goinFragment(new MemberMainFragment());
	}

	public LinePerson getCurrentUser() {
		return currentUser;
	}

	public void setTitleTxt(int resId) {
		final String title = getString(resId);
		setTitleTxt(title);
	}
	
	public void setTitleTxt(final String title) {
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				titleTxt.setText(title);
				titleTxt.invalidate();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

	}
	
	public void back(){
		if (level <= 1) {

			new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(R.string.quit)
	        .setMessage(R.string.really_quit)
	        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {

	                //Stop the activity
	                finish();    
	            }

	        })
	        .setNegativeButton(android.R.string.no, null)
	        .show();
		} else {
			gobackFragment();
		}
	}

	@Override
	public boolean onKeyDown(int keyCoder, KeyEvent event) {
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			back();
			
			return true;
		default:
			return false;
		}
	}

	public void goinFragment(Fragment f){
		if(f == null) return;
		synchronized (mClickLock) {
            final FragmentTransaction ft = getSupportFragmentManager().
            		beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            	ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);
            ft.add(R.id.memberzone, f);
            ft.addToBackStack(f.getClass().getName());
            ft.commit();
            
            level++;
            
            mClickLock.notifyAll();
		}            
	}
	
	
	public void gobackFragment(){
		synchronized (mClickLock) {
			final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		
		Fragment f = fm.findFragmentById(R.id.memberzone);
		ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit);
		ft.remove(f);
		ft.commit();
		
		level--;
		
		mClickLock.notifyAll();
	}
	}

}
