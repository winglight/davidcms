package com.yi4all.davidapp;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
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

	private final Object mClickLock = new Object();
	
	private boolean isRoot = true;

	private TextView titleTxt;

	private Fragment currentFragment;

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

		goinFragment(new MemberMainFragment());
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
	
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public void back(){
		if (isRoot) {

			this.finish();
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
		synchronized (mClickLock) {
            final FragmentTransaction ft = getSupportFragmentManager().
            		beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            	ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);
            ft.replace(R.id.memberzone, f);
            ft.addToBackStack(null);
            ft.commit();
            
            currentFragment = f;
            
            mClickLock.notifyAll();
		}            
	}
	
	
	public void gobackFragment(){
		synchronized (mClickLock) {
			final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit);
		ft.remove(currentFragment);
		ft.commit();
		if(fm.getBackStackEntryCount() <= 1){
			isRoot = true;
		}
		
		mClickLock.notifyAll();
	}
	}

}
