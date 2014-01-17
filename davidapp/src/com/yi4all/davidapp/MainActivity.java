package com.yi4all.davidapp;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.yi4all.davidapp.fragment.OrderFragment;
import com.yi4all.davidapp.fragment.ServiceListFragment;
import com.yi4all.davidapp.util.Utils;
import com.yi4all.davidapp.view.InfiniteGallery;

public class MainActivity extends BaseActivity {

	private final static String LOGTAG = "MainActivity";

	private String[] pageTitle;

	private boolean isTwiceQuit;

    private TextView carouselTxt;
    
    private TextView oldTab;
    
    private int currentTabPos;
    
    private int newTabPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main);

		pageTitle = getResources().getStringArray(R.array.main_tab_label);

        carouselTxt = (TextView) findViewById(R.id.carouselTxt);
        
        final ImageView splashImg = (ImageView) findViewById(R.id.splash_img);

		final InfiniteGallery indicator = (InfiniteGallery) findViewById(R.id.indicator);
		
		indicator.setTabs(pageTitle);
		indicator.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, final View view,
					final int position, long arg3) {
				newTabPos = position;
				
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						if(newTabPos != position) return;
						
						//set older tab text appearance
						if(oldTab != null){
						oldTab.setTextColor(getResources().getColor(R.color.tab_text_color));
						oldTab.setTextSize(getResources().getDimensionPixelSize(R.dimen.tab_text_dimen));
						}
						
						Fragment f = getItem(position%pageTitle.length);
						
						addFragment(f, pageTitle[position%pageTitle.length], currentTabPos > position);
						
						//set new tab text appearance
						currentTabPos = position;
						TextView newTab = (TextView) view; 
						newTab.setTextColor(getResources().getColor(R.color.tab_selected_text_color));
						newTab.setTextSize(getResources().getDimensionPixelSize(R.dimen.tab_selected_text_dimen));
						
//						indicator.invalidate();
						
						oldTab = newTab;
					}
				}, 500);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		indicator.setSelection(getMiddlePos());
		
		//show and hide splash screen
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				indicator.setSelection(getMiddlePos());
				
				splashImg.setVisibility(View.GONE);
			}
		}, 1500);
	}
	
	private int getMiddlePos(){
		int res = Integer.MAX_VALUE/2;
		int add = res%pageTitle.length;
		res = res + (3 - add);
		
		return res;
	}

    @Override
    protected void onResume() {
        super.onResume();

        CompanyModel company = getService().getDefaultCompany();

        if(company != null){
        carouselTxt.setText(company.getMarquee());
        }
    }

	@Override
	public boolean onKeyDown(int keyCoder, KeyEvent event) {
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (isTwiceQuit) {

				this.finish();
			} else {
				Utils.toastMsg(this, R.string.sure_quit_app);
				isTwiceQuit = true;

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						isTwiceQuit = false;

					}
				}, 2000);
			}
			return true;
		default:
			return false;
		}
	}
	
	public Fragment getItem(int position) {
        Fragment f = new IntroFragment();
        switch (position){
            case 0:
                //資料查詢
            	f = new LoginFragment();
                break;
            case 1:
                //推廣諮訊
            	f= new MarketingListFragment();
                break;
            case 2:
                //聽會介紹
            	f = new HallListFragment();
                break;
            case 3:
                //大衛集團
                f = new IntroFragment();
                break;
            case 4:
                //線上預訂
            	f = new OrderFragment();
                break;
            case 5:
                //服務項目
                f = new ServiceListFragment();
                break;
            case 6:
                //聯繫我們
            	f = new ContactusListFragment();
                break;
            case 7:
                //意見箱
            	f= new FeedbackFragment();
                break;

        }
		return  f;
	}

}
